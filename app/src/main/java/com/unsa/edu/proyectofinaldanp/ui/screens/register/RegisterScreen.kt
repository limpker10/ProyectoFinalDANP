package com.unsa.edu.proyectofinaldanp.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unsa.edu.proyectofinaldanp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navController: NavHostController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(36.dp)
    ) {
        Register(Modifier.align(Alignment.TopCenter), registerViewModel,navController)
    }
}

@Composable
fun Register(
    modifier: Modifier,
    registerViewModel: RegisterViewModel,
    navController: NavHostController
) {

    val email: String by registerViewModel.email.observeAsState(initial = "")
    val password: String by registerViewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by registerViewModel.loginEnable.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier) {
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(5.dp))
        EmailField(email) { registerViewModel.onRegisterChanged(it, password) }
        Spacer(modifier = Modifier.padding(12.dp))
        PasswordField(password) { registerViewModel.onRegisterChanged(email, it) }
        Spacer(modifier = Modifier.padding(20.dp))
        RegisterButton(loginEnable) {
            coroutineScope.launch {
                if (registerViewModel.register(email, password)) {
                    navController.navigate("reset_page")
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        ToLoginButton(){navController.navigate("login_page")}
    }

}

@Composable
fun RegisterButton(loginEnable:Boolean,onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            disabledContentColor = Color.White,
        ),enabled = loginEnable
    ) {
        Text(text = "Registrar sesión")
    }
}

@Composable
fun ToLoginButton(onLoginSelected: () -> Unit) {
    Button(
        onClick = { onLoginSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Black,
            disabledContentColor = Color.White
        ),
    ) {
        Text(text = "Back to login")
    }
}
@Composable
fun HeaderImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.register_interface),
        contentDescription = "Header",
        modifier = modifier
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(password: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = password, onValueChange = { onTextFieldChanged(it) },
        placeholder = { Text(text = "Contraseña") },
        label = { Text("Enter password") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(email: String, onTextFieldChanged: (String) -> Unit) {
    TextField(
        value = email, onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        label = { Text("Enter Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
    )
}

