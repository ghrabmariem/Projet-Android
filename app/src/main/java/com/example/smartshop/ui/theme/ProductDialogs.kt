package com.example.smartshop.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartshop.data.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onAddProduct: (String, String, Double, Int, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ajouter un produit") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = it.isBlank()
                    },
                    label = { Text("Nom du produit *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError,
                    supportingText = {
                        if (nameError) Text("Le nom est requis")
                    }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        priceError = it.toDoubleOrNull()?.let { p -> p <= 0 } ?: true
                    },
                    label = { Text("Prix (€) *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = priceError,
                    supportingText = {
                        if (priceError) Text("Prix invalide (> 0)")
                    }
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it
                        quantityError = it.toIntOrNull()?.let { q -> q < 0 } ?: true
                    },
                    label = { Text("Quantité *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = quantityError,
                    supportingText = {
                        if (quantityError) Text("Quantité invalide (≥ 0)")
                    }
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = {
                        category = it
                        categoryError = it.isBlank()
                    },
                    label = { Text("Catégorie *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = categoryError,
                    supportingText = {
                        if (categoryError) Text("La catégorie est requise")
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priceValue = price.toDoubleOrNull() ?: 0.0
                    val quantityValue = quantity.toIntOrNull() ?: 0

                    if (name.isNotBlank() &&
                        priceValue > 0 &&
                        quantityValue >= 0 &&
                        category.isNotBlank()) {
                        onAddProduct(name, description, priceValue, quantityValue, category)
                    } else {
                        nameError = name.isBlank()
                        priceError = priceValue <= 0
                        quantityError = quantityValue < 0
                        categoryError = category.isBlank()
                    }
                }
            ) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(
    product: Product,
    onDismiss: () -> Unit,
    onUpdateProduct: (Product) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var description by remember { mutableStateOf(product.description) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var quantity by remember { mutableStateOf(product.quantity.toString()) }
    var category by remember { mutableStateOf(product.category) }

    var nameError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var quantityError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modifier le produit") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = it.isBlank()
                    },
                    label = { Text("Nom du produit *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError,
                    supportingText = {
                        if (nameError) Text("Le nom est requis")
                    }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        priceError = it.toDoubleOrNull()?.let { p -> p <= 0 } ?: true
                    },
                    label = { Text("Prix (€) *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = priceError,
                    supportingText = {
                        if (priceError) Text("Prix invalide (> 0)")
                    }
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it
                        quantityError = it.toIntOrNull()?.let { q -> q < 0 } ?: true
                    },
                    label = { Text("Quantité *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = quantityError,
                    supportingText = {
                        if (quantityError) Text("Quantité invalide (≥ 0)")
                    }
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = {
                        category = it
                        categoryError = it.isBlank()
                    },
                    label = { Text("Catégorie *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = categoryError,
                    supportingText = {
                        if (categoryError) Text("La catégorie est requise")
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val priceValue = price.toDoubleOrNull() ?: 0.0
                    val quantityValue = quantity.toIntOrNull() ?: 0

                    if (name.isNotBlank() &&
                        priceValue > 0 &&
                        quantityValue >= 0 &&
                        category.isNotBlank()) {

                        val updatedProduct = product.copy(
                            name = name,
                            description = description,
                            price = priceValue,
                            quantity = quantityValue,
                            category = category
                        )

                        onUpdateProduct(updatedProduct)
                    } else {
                        nameError = name.isBlank()
                        priceError = priceValue <= 0
                        quantityError = quantityValue < 0
                        categoryError = category.isBlank()
                    }
                }
            ) {
                Text("Modifier")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}