package com.example.lacantera.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.R
import com.example.lacantera.viewmodel.LoginViewModel

private val LoginNavy = Color(0xFF102B67)
private val LoginNavyDark = Color(0xFF091F51)
private val LoginBackground = Color(0xFFF1F7FF)
private val LoginFieldBackground = Color(0xFFF3F8FF)
private val LoginFieldBorder = Color(0xFFB8D9FF)
private val LoginPrimaryText = Color(0xFF0D1B3E)
private val LoginSecondaryText = Color(0xFF64708A)
private val LoginCardBackground = Color.White

@Composable
fun LoginScreen(
    onLoginSuccess: (DashboardType) -> Unit,
    onBackToHome: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(
        uiState.loginSuccess,
        uiState.dashboardType
    ) {
        val dashboardType = uiState.dashboardType

        if (
            uiState.loginSuccess &&
            dashboardType != null
        ) {
            viewModel.clearLoginSuccess()
            onLoginSuccess(dashboardType)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LoginBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = 22.dp,
                    vertical = 18.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LoginCardBackground
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 7.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 26.dp,
                            vertical = 30.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(
                            id = R.drawable.logo
                        ),
                        contentDescription = "Logo de La Cantera",
                        modifier = Modifier
                            .fillMaxWidth(0.76f)
                            .height(130.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "LA CANTERA",
                        color = LoginNavyDark,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Badge,
                            contentDescription = null,
                            tint = LoginNavy,
                            modifier = Modifier.size(25.dp)
                        )

                        Spacer(modifier = Modifier.width(9.dp))

                        Text(
                            text = "Acceso miembros",
                            color = LoginNavyDark,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "Ingresa con tu usuario y contraseña",
                        modifier = Modifier.fillMaxWidth(),
                        color = LoginNavy,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "USUARIO",
                        modifier = Modifier.fillMaxWidth(),
                        color = LoginPrimaryText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.username,
                        onValueChange = viewModel::onUsernameChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(26.dp),
                        placeholder = {
                            Text(
                                text = "Ingresa tu usuario",
                                color = LoginSecondaryText.copy(
                                    alpha = 0.65f
                                )
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = LoginFieldBackground,
                            unfocusedContainerColor = LoginFieldBackground,
                            disabledContainerColor = LoginFieldBackground,
                            focusedBorderColor = LoginFieldBorder,
                            unfocusedBorderColor = LoginFieldBorder,
                            disabledBorderColor = LoginFieldBorder,
                            cursorColor = LoginNavy,
                            focusedTextColor = LoginPrimaryText,
                            unfocusedTextColor = LoginPrimaryText
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "CONTRASEÑA",
                        modifier = Modifier.fillMaxWidth(),
                        color = LoginPrimaryText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(26.dp),
                        placeholder = {
                            Text(
                                text = "Ingresa tu contraseña",
                                color = LoginSecondaryText.copy(
                                    alpha = 0.65f
                                )
                            )
                        },
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                },
                                enabled = !uiState.isLoading
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Filled.VisibilityOff
                                    } else {
                                        Icons.Filled.Visibility
                                    },
                                    contentDescription = if (passwordVisible) {
                                        "Ocultar contraseña"
                                    } else {
                                        "Mostrar contraseña"
                                    },
                                    tint = LoginSecondaryText,
                                    modifier = Modifier.size(23.dp)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = LoginFieldBackground,
                            unfocusedContainerColor = LoginFieldBackground,
                            disabledContainerColor = LoginFieldBackground,
                            focusedBorderColor = LoginFieldBorder,
                            unfocusedBorderColor = LoginFieldBorder,
                            disabledBorderColor = LoginFieldBorder,
                            cursorColor = LoginNavy,
                            focusedTextColor = LoginPrimaryText,
                            unfocusedTextColor = LoginPrimaryText
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.login()
                            }
                        )
                    )

                    uiState.errorMessage?.let { error ->
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = error,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.login()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LoginNavy,
                            contentColor = Color.White,
                            disabledContainerColor = LoginNavy.copy(
                                alpha = 0.55f
                            ),
                            disabledContentColor = Color.White.copy(
                                alpha = 0.75f
                            )
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 18.dp
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(23.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Login,
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "INGRESAR",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = onForgotPasswordClick,
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = LoginSecondaryText,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = LoginSecondaryText.copy(
                                alpha = 0.5f
                            )
                        ),
                        elevation = null,
                        contentPadding = PaddingValues(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                    ) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    OutlinedButton(
                        onClick = onBackToHome,
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(26.dp),
                        border = null,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = LoginNavy,
                            contentColor = Color.White,
                            disabledContainerColor = LoginNavy.copy(
                                alpha = 0.55f
                            ),
                            disabledContentColor = Color.White.copy(
                                alpha = 0.7f
                            )
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 22.dp,
                            vertical = 12.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = null,
                            modifier = Modifier.size(21.dp)
                        )

                        Spacer(modifier = Modifier.width(9.dp))

                        Text(
                            text = "Regresar a Inicio",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "La Cantera · centro deportivo",
                        color = Color(0xFF6586B8),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}