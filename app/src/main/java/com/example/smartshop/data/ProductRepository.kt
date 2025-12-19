package com.example.smartshop.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class ProductRepository(
    private val productDao: ProductDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // Collection Firebase
    private val productsCollection = firestore.collection("products")

    // Flow pour observer les produits en temps réel
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    // ========== Opérations locales ==========

    suspend fun insertProduct(product: Product): String {
        return withContext(Dispatchers.IO) {
            val productId = if (product.id.isEmpty()) {
                UUID.randomUUID().toString()
            } else {
                product.id
            }

            val productWithId = product.copy(
                id = productId,
                updatedAt = System.currentTimeMillis()
            )

            productDao.insertProduct(productWithId)
            productId
        }
    }

    suspend fun updateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            val updatedProduct = product.copy(
                updatedAt = System.currentTimeMillis(),
                syncedWithFirebase = false
            )
            productDao.updateProduct(updatedProduct)
        }
    }

    suspend fun deleteProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.deleteProduct(product)
            // Supprimer aussi de Firebase
            try {
                productsCollection.document(product.id).delete().await()
            } catch (e: Exception) {
                // Gérer l'erreur silencieusement
            }
        }
    }

    suspend fun getProductById(id: String): Product? {
        return withContext(Dispatchers.IO) {
            productDao.getProductById(id)
        }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }

    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }

    // ========== Statistiques ==========

    suspend fun getProductCount(): Int {
        return withContext(Dispatchers.IO) {
            productDao.getProductCount()
        }
    }

    suspend fun getTotalStockValue(): Double {
        return withContext(Dispatchers.IO) {
            productDao.getTotalStockValue() ?: 0.0
        }
    }

    // ========== Synchronisation Firebase ==========

    suspend fun syncWithFirebase(product: Product) {
        withContext(Dispatchers.IO) {
            try {
                productsCollection
                    .document(product.id)
                    .set(product.toMap())
                    .await()

                // Marquer comme synchronisé
                productDao.markAsSynced(product.id)
            } catch (e: Exception) {
                throw Exception("Erreur de synchronisation: ${e.message}")
            }
        }
    }

    suspend fun syncAllUnsyncedProducts() {
        withContext(Dispatchers.IO) {
            val unsyncedProducts = productDao.getUnsyncedProducts()
            unsyncedProducts.forEach { product ->
                try {
                    syncWithFirebase(product)
                } catch (e: Exception) {
                    // Log l'erreur mais continue
                }
            }
        }
    }

    suspend fun fetchProductsFromFirebase() {
        withContext(Dispatchers.IO) {
            try {
                val snapshot = productsCollection.get().await()
                val products = snapshot.documents.mapNotNull { doc ->
                    doc.data?.let { Product.fromMap(it, doc.id) }
                }

                productDao.insertProducts(products)
            } catch (e: Exception) {
                throw Exception("Erreur de récupération Firebase: ${e.message}")
            }
        }
    }

    // Écouter les changements en temps réel de Firebase
    fun listenToFirebaseChanges(onUpdate: (List<Product>) -> Unit) {
        productsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            snapshot?.let {
                val products = it.documents.mapNotNull { doc ->
                    doc.data?.let { data -> Product.fromMap(data, doc.id) }
                }
                onUpdate(products)
            }
        }
    }
}