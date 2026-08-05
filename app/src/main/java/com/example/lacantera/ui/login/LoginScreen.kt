package com.example.lacantera.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.R
import com.example.lacantera.viewmodel.LoginViewModel

/*
 * Colores del acceso.
 *
 * Se mantienen como constantes privadas para que posteriormente puedas
 * moverlos fácilmente al tema general de la aplicación.
 */
private val LoginNavy = Color(0xFF123272)
private val LoginNavyDark = Color(0xFF071C48)
private val LoginBlue = Color(0xFF275AA8)

private val LoginBackground = Color(0xFFF3F7FD)
private val LoginCardBackground = Color(0xFFFFFFFF)
private val LoginInputBackground = Color(0xFFF7F9FD)

private val LoginPrimaryText = Color(0xFF101B36)
private val LoginSecondaryText = Color(0xFF68748D)

private val LoginBorder = Color(0xFFD7E0EF)
private val LoginDivider = Color(0xFFE4E9F2)

private val LoginErrorBackground = Color(0xFFFFF1F1)
private val LoginErrorText = Color(0xFFB3261E)

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
    val passwordFocusRequester = remember {
        FocusRequester()
    }

    /*
     * Mantiene exactamente la navegación que ya tenías.
     * Cuando el login termina correctamente, limpia el estado
     * y abre el dashboard correspondiente.
     */
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LoginNavyDark,
                        LoginNavy,
                        LoginBackground,
                        LoginBackground
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        /*
         * Detecta celulares pequeños o pantallas con poca altura.
         * No necesita WindowSizeClass ni dependencias adicionales.
         */
        val isCompactHeight = maxHeight < 720.dp
        val isSmallWidth = maxWidth < 360.dp
        val isCompact = isCompactHeight || isSmallWidth

        val horizontalPadding = if (isSmallWidth) {
            14.dp
        } else {
            22.dp
        }

        /*
         * Elementos decorativos del fondo.
         */
        Box(
            modifier = Modifier
                .size(230.dp)
                .offset(
                    x = (-105).dp,
                    y = (-115).dp
                )
                .background(
                    color = Color.White.copy(alpha = 0.055f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(170.dp)
                .offset(
                    x = 72.dp,
                    y = 72.dp
                )
                .background(
                    color = Color.White.copy(alpha = 0.045f),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = horizontalPadding,
                    vertical = if (isCompact) 14.dp else 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginBrandHeader(
                compact = isCompact
            )

            Spacer(
                modifier = Modifier.height(
                    if (isCompact) 16.dp else 24.dp
                )
            )

            LoginFormCard(
                uiState = uiState,
                passwordVisible = passwordVisible,
                compact = isCompact,
                passwordFocusRequester = passwordFocusRequester,
                onUsernameChange = viewModel::onUsernameChange,
                onPasswordChange = viewModel::onPasswordChange,
                onPasswordVisibilityChange = {
                    passwordVisible = !passwordVisible
                },
                onLoginClick = {
                    focusManager.clearFocus()
                    viewModel.login()
                },
                onUsernameNext = {
                    passwordFocusRequester.requestFocus()
                },
                onForgotPasswordClick = onForgotPasswordClick,
                onBackToHome = onBackToHome
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "La Cantera · Centro deportivo",
                color = if (isCompact) {
                    LoginSecondaryText
                } else {
                    Color.White.copy(alpha = 0.76f)
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LoginBrandHeader(
    compact: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 460.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(
                if (compact) 84.dp else 100.dp
            ),
            shape = RoundedCornerShape(
                if (compact) 22.dp else 26.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        if (compact) 8.dp else 10.dp
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.logo
                    ),
                    contentDescription = "Logo de La Cantera",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(
            modifier = Modifier.height(
                if (compact) 10.dp else 14.dp
            )
        )

        Text(
            text = "ACCESO PARA MIEMBROS",
            color = Color.White,
            fontSize = if (compact) 18.sp else 21.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Ingresa a tu cuenta para continuar",
            color = Color.White.copy(alpha = 0.76f),
            fontSize = if (compact) 12.sp else 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoginFormCard(
    uiState: LoginUiState,
    passwordVisible: Boolean,
    compact: Boolean,
    passwordFocusRequester: FocusRequester,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onLoginClick: () -> Unit,
    onUsernameNext: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onBackToHome: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 460.dp),
        shape = RoundedCornerShape(
            if (compact) 25.dp else 30.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = LoginCardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = if (compact) 19.dp else 25.dp,
                    vertical = if (compact) 21.dp else 27.dp
                )
        ) {
            Text(
                text = "Bienvenido",
                color = LoginPrimaryText,
                fontSize = if (compact) 23.sp else 27.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Escribe tus datos para ingresar a La Cantera.",
                color = LoginSecondaryText,
                fontSize = if (compact) 13.sp else 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(
                modifier = Modifier.height(
                    if (compact) 20.dp else 26.dp
                )
            )

            OutlinedTextField(
                value = uiState.username,
                onValueChange = onUsernameChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                singleLine = true,
                shape = RoundedCornerShape(17.dp),
                label = {
                    Text(
                        text = "Usuario"
                    )
                },
                placeholder = {
                    Text(
                        text = "Escribe tu usuario"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                },
                colors = loginTextFieldColors(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onUsernameNext()
                    }
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
                enabled = !uiState.isLoading,
                singleLine = true,
                shape = RoundedCornerShape(17.dp),
                label = {
                    Text(
                        text = "Contraseña"
                    )
                },
                placeholder = {
                    Text(
                        text = "Escribe tu contraseña"
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(21.dp)
                    )
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(
                        onClick = onPasswordVisibilityChange,
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
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = loginTextFieldColors(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onLoginClick()
                    }
                )
            )

            uiState.errorMessage?.let { errorMessage ->
                Spacer(modifier = Modifier.height(13.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = LoginErrorBackground,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 11.dp
                        ),
                        color = LoginErrorText,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(
                    if (compact) 19.dp else 23.dp
                )
            )

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LoginNavy,
                    contentColor = Color.White,
                    disabledContainerColor = LoginNavy.copy(
                        alpha = 0.58f
                    ),
                    disabledContentColor = Color.White.copy(
                        alpha = 0.82f
                    )
                ),
                contentPadding = PaddingValues(
                    horizontal = 20.dp
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(21.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Ingresando...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Login,
                        contentDescription = null,
                        modifier = Modifier.size(21.dp)
                    )

                    Spacer(modifier = Modifier.width(9.dp))

                    Text(
                        text = "INGRESAR",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = LoginBlue,
                    disabledContentColor = LoginSecondaryText.copy(
                        alpha = 0.5f
                    )
                )
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            LoginDivider()

            Spacer(modifier = Modifier.height(17.dp))

            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(17.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = LoginBorder
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = LoginNavy,
                    disabledContainerColor = Color.White,
                    disabledContentColor = LoginSecondaryText.copy(
                        alpha = 0.5f
                    )
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Volver al inicio",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Tu acceso está protegido y vinculado con tu perfil.",
                modifier = Modifier.fillMaxWidth(),
                color = LoginSecondaryText.copy(alpha = 0.8f),
                fontSize = 11.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun LoginDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(LoginDivider)
        )

        Text(
            text = "o",
            modifier = Modifier.padding(horizontal = 12.dp),
            color = LoginSecondaryText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(LoginDivider)
        )
    }
}

@Composable
private fun loginTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = LoginInputBackground,
        unfocusedContainerColor = LoginInputBackground,
        disabledContainerColor = LoginInputBackground,

        focusedBorderColor = LoginBlue,
        unfocusedBorderColor = LoginBorder,
        disabledBorderColor = LoginBorder.copy(alpha = 0.65f),

        focusedTextColor = LoginPrimaryText,
        unfocusedTextColor = LoginPrimaryText,
        disabledTextColor = LoginSecondaryText,

        focusedLabelColor = LoginNavy,
        unfocusedLabelColor = LoginSecondaryText,

        focusedLeadingIconColor = LoginNavy,
        unfocusedLeadingIconColor = LoginSecondaryText,

        focusedTrailingIconColor = LoginNavy,
        unfocusedTrailingIconColor = LoginSecondaryText,

        cursorColor = LoginNavy
    )