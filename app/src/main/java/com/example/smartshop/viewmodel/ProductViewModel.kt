package com.example.smartshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshop.data.Product
import com.example.smartshop.data.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // États
    sealed class ProductState {
        object Idle : ProductState()
        object Loading : ProductState()
        object Success : ProductState()
        data class Error(val message: String) : ProductState()
    }

    // State Flow pour l'état de l'UI
    private val _state = MutableStateFlow<ProductState>(ProductState.Idle)
    val state: StateFlow<ProductState> = _state.asStateFlow()

    // Liste des produits
    val products: StateFlow<List<Product>> = repository.allProducts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Statistiques
    private val _productCount = MutableStateFlow(0)
    val productCount: StateFlow<Int> = _productCount.asStateFlow()

    private val _totalStockValue = MutableStateFlow(0.0)
    val totalStockValue: StateFlow<Double> = _totalStockValue.asStateFlow()

    // Recherche
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<Product>> = searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.allProducts
            } else {
                repository.searchProducts(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Charger les statistiques
        loadStatistics()
        // Synchroniser avec Firebase au démarrage
        syncWithFirebase()
        // Écouter les changements Firebase
        listenToFirebaseChanges()
    }

    // ========== Opérations CRUD ==========

    fun addProduct(
        name: String,
        description: String,
        price: Double,
        quantity: Int,
        category: String
    ) {
        if (name.isBlank() || price <= 0 || quantity < 0 || category.isBlank()) {
            _state.value = ProductState.Error("Données invalides")
            return
        }

        _state.value = ProductState.Loading
        viewModelScope.launch {
            try {
                val product = Product(
                    name = name,
                    description = description,
                    price = price,
                    quantity = quantity,
                    category = category
                )

                val productId = repository.insertProduct(product)
                // Synchroniser avec Firebase
                repository.syncWithFirebase(product.copy(id = productId))

                _state.value = ProductState.Success
                loadStatistics()
            } catch (e: Exception) {
                _state.value = ProductState.Error(e.message ?: "Erreur lors de l'ajout")
            }
        }
    }

    fun updateProduct(product: Product) {
        if (!product.isValid()) {
            _state.value = ProductState.Error("Données invalides")
            return
        }

        _state.value = ProductState.Loading
        viewModelScope.launch {
            try {
                repository.updateProduct(product)
                repository.syncWithFirebase(product)

                _state.value = ProductState.Success
                loadStatistics()
            } catch (e: Exception) {
                _state.value = ProductState.Error(e.message ?: "Erreur de mise à jour")
            }
        }
    }

    fun deleteProduct(product: Product) {
        _state.value = ProductState.Loading
        viewModelScope.launch {
            try {
                repository.deleteProduct(product)
                _state.value = ProductState.Success
                loadStatistics()
            } catch (e: Exception) {
                _state.value = ProductState.Error(e.message ?: "Erreur de suppression")
            }
        }
    }

    // ========== Recherche ==========

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // ========== Statistiques ==========

    private fun loadStatistics() {
        viewModelScope.launch {
            try {
                _productCount.value = repository.getProductCount()
                _totalStockValue.value = repository.getTotalStockValue()
            } catch (e: Exception) {
                // Gérer l'erreur silencieusement
            }
        }
    }

    // ========== Synchronisation Firebase ==========

    fun syncWithFirebase() {
        viewModelScope.launch {
            try {
                repository.fetchProductsFromFirebase()
                repository.syncAllUnsyncedProducts()
            } catch (e: Exception) {
                _state.value = ProductState.Error("Erreur de synchronisation")
            }
        }
    }

    private fun listenToFirebaseChanges() {
        repository.listenToFirebaseChanges { firebaseProducts ->
            viewModelScope.launch {
                // Mettre à jour la base locale avec les données Firebase
                firebaseProducts.forEach { product ->
                    repository.insertProduct(product)
                }
                loadStatistics()
            }
        }
    }

    fun resetState() {
        _state.value = ProductState.Idle
    }
}