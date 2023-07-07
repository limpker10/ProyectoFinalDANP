package com.unsa.edu.proyectofinaldanp.ui.screens.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    fun onRegisterChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
    }
    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean = password.length > 4

    private fun isValidEmail(email: String): Boolean  = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun register(email:String,password: String) : Boolean {
        var isLoading = false

        if (!email.isNullOrBlank() && !password.isNullOrBlank()) {
            viewModelScope.launch {
                try {
                    val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    if (authResult.isSuccessful) {
                        Log.d("Registro", "El registro fue exitoso")
                        isLoading = true
                    }
                } catch (e: Exception) {
                    Log.d("Loggin","EXCEPCION Login Google" + "${e.localizedMessage}")
                }
            }
        }
        return isLoading
    }
}