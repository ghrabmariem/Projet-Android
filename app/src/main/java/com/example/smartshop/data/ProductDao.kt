package com.example.smartshop.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // Récupérer tous les produits (ordre décroissant par date)
    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    fun getAllProducts(): Flow<List<Product>>

    // Récupérer un produit par ID
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: String): Product?

    // Rechercher des produits par nom
    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchQuery || '%' ORDER BY createdAt DESC")
    fun searchProducts(searchQuery: String): Flow<List<Product>>

    // Filtrer par catégorie
    @Query("SELECT * FROM products WHERE category = :category ORDER BY createdAt DESC")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    // Insérer un produit
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    // Insérer plusieurs produits
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    // Mettre à jour un produit
    @Update
    suspend fun updateProduct(product: Product)

    // Supprimer un produit
    @Delete
    suspend fun deleteProduct(product: Product)

    // Supprimer tous les produits
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    // Compter le nombre total de produits
    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    // Calculer la valeur totale du stock
    @Query("SELECT SUM(price * quantity) FROM products")
    suspend fun getTotalStockValue(): Double?

    // Obtenir les produits non synchronisés avec Firebase
    @Query("SELECT * FROM products WHERE syncedWithFirebase = 0")
    suspend fun getUnsyncedProducts(): List<Product>

    // Marquer comme synchronisé
    @Query("UPDATE products SET syncedWithFirebase = 1 WHERE id = :productId")
    suspend fun markAsSynced(productId: String)
}