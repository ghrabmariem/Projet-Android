package com.example.smartshop.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    sealed class SignUpState {
        object Idle : SignUpState()
        object Loading : SignUpState()
        object Success : SignUpState()
        data class Error(val message: String) : SignUpState()
    }

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val state = _state.asStateFlow()

    fun signUp(email: String, password: String, confirmPassword: String) {
        // Validation
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _state.value = SignUpState.Error("Veuillez remplir tous les champs")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.value = SignUpState.Error("Email invalide")
            return
        }

        if (password.length < 6) {
            _state.value = SignUpState.Error("Le mot de passe doit contenir au moins 6 caractères")
            return
        }

        if (password != confirmPassword) {
            _state.value = SignUpState.Error("Les mots de passe ne correspondent pas")
            return
        }

        _state.value = SignUpState.Loading

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _state.value = SignUpState.Success
            } catch (e: Exception) {
                _state.value = SignUpState.Error(
                    when {
                        e.message?.contains("already in use") == true ->
                            "Cet email est déjà utilisé"
                        e.message?.contains("network") == true ->
                            "Erreur de connexion réseau"
                        else -> e.message ?: "Erreur lors de l'inscription"
                    }
                )
            }
        }
    }

    fun resetState() {
        _state.value = SignUpState.Idle
    }
}