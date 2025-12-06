package com.example.smartshop.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    fun signUp(email: String, password: String) {
        _state.value = SignUpState.Loading
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { _state.value = SignUpState.Success }
                .addOnFailureListener { _state.value = SignUpState.Error(it.message ?: "Erreur inconnue") }
        }
    }
}
