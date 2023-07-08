package com.unsa.edu.proyectofinaldanp.ui.screens.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidPassword(password: String): Boolean = password.length > 4

    private fun isValidEmail(email: String): Boolean  = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    suspend fun onLoginSelected(email: String, password: String, navController: NavHostController) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Login successful
                        Log.d("Login", "Ingreso exitoso")
                        navController.navigate("Home_page")
                    } else {
                        // Login failed
                        Log.d("Login", "Fallo el inicio de sesión")
                        _isLoading.value = true
                        //Toast.makeText(this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle the exception
                    Log.d("Login", "Excepción en el inicio de sesión: ${exception.localizedMessage}")
                }
        } catch (e: Exception) {
            // Handle the exception
            Log.d("Login", "Excepción en el inicio de sesión: ${e.localizedMessage}")
        }
        delay(3000)
        _isLoading.value = false
    }


    fun sigInWithGoogleCredenctial(credential : AuthCredential, home:() -> Unit) = viewModelScope.launch {
        try {
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Loggin","Logeado con exitoso")
                    home()
                }
            }.addOnFailureListener {
                Log.d("Loggin","Fallo Login Google")
            }
        }
        catch (ex: Exception) {
            Log.d("Loggin","EXCEPCION Login Google" + "${ex.localizedMessage}")
        }
    }

}