package com.example.smartshop.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String = "", // Utiliser String pour Firebase ID
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val category: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncedWithFirebase: Boolean = false
) {
    // Validation
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                price > 0 &&
                quantity >= 0 &&
                category.isNotBlank()
    }

    // Valeur totale (prix * quantité)
    fun totalValue(): Double = price * quantity

    // Conversion vers Map pour Firebase
    fun toMap(): Map<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "description" to description,
            "price" to price,
            "quantity" to quantity,
            "category" to category,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt
        )
    }

    companion object {
        // Conversion depuis Map Firebase
        fun fromMap(map: Map<String, Any>, id: String): Product {
            return Product(
                id = id,
                name = map["name"] as? String ?: "",
                description = map["description"] as? String ?: "",
                price = (map["price"] as? Number)?.toDouble() ?: 0.0,
                quantity = (map["quantity"] as? Number)?.toInt() ?: 0,
                category = map["category"] as? String ?: "",
                createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                updatedAt = (map["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                syncedWithFirebase = true
            )
        }
    }
}