package com.example.lacantera.ui.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateUserScreen(
    onBackClick: () -> Unit,
    onCreateCompleted: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: CreateUserViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.createCompleted) {
        if (uiState.createCompleted) {
            onCreateCompleted()
        }
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            viewModel.consumeSessionExpired()
            onSessionExpired()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBackClick
            ) {
                Text("Regresar")
            }

            Text(
                text = "Nuevo usuario",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Tipo de usuario",
            fontWeight = FontWeight.Bold
        )

        SelectionOption(
            text = "Mexicano",
            selected =
                uiState.tipoUsuario == "mexicano",
            onClick = {
                viewModel.onTipoUsuarioChanged(
                    "mexicano"
                )
            }
        )

        SelectionOption(
            text = "Extranjero",
            selected =
                uiState.tipoUsuario == "extranjero",
            onClick = {
                viewModel.onTipoUsuarioChanged(
                    "extranjero"
                )
            }
        )

        if (uiState.tipoUsuario == "mexicano") {
            CreateUserField(
                value = uiState.username,
                label = "CURP",
                onValueChange =
                    viewModel::onUsernameChanged
            )
        } else {
            Text(
                text = (
                        "La CURP se generará " +
                                "automáticamente."
                        ),
                color = MaterialTheme
                    .colorScheme
                    .onSurfaceVariant,
                modifier = Modifier.padding(
                    bottom = 12.dp
                )
            )
        }

        CreateUserField(
            value = uiState.firstName,
            label = "Nombre",
            onValueChange =
                viewModel::onFirstNameChanged
        )

        CreateUserField(
            value = uiState.lastName,
            label = "Primer apellido",
            onValueChange =
                viewModel::onLastNameChanged
        )

        CreateUserField(
            value = uiState.segundoApellido,
            label = "Segundo apellido",
            onValueChange =
                viewModel::onSegundoApellidoChanged
        )

        CreateUserField(
            value = uiState.email,
            label = "Correo electrónico",
            onValueChange =
                viewModel::onEmailChanged
        )

        CreateUserField(
            value = uiState.telefono,
            label = "Teléfono",
            onValueChange =
                viewModel::onTelefonoChanged
        )

        CreateUserField(
            value = uiState.fechaNacimiento,
            label = "Fecha de nacimiento (AAAA-MM-DD)",
            onValueChange =
                viewModel::onFechaNacimientoChanged
        )

        CreateUserField(
            value = uiState.nacionalidad,
            label = "Nacionalidad",
            onValueChange =
                viewModel::onNacionalidadChanged
        )

        CreateUserField(
            value = uiState.estadoNacimiento,
            label = "Estado de nacimiento",
            onValueChange =
                viewModel::onEstadoNacimientoChanged
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Sexo",
            fontWeight = FontWeight.Bold
        )

        SelectionOption(
            text = "Hombre",
            selected = uiState.sexo == "H",
            onClick = {
                viewModel.onSexoChanged("H")
            }
        )

        SelectionOption(
            text = "Mujer",
            selected = uiState.sexo == "M",
            onClick = {
                viewModel.onSexoChanged("M")
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Rol",
            fontWeight = FontWeight.Bold
        )

        CreateUserRole.entries.forEach { role ->
            SelectionOption(
                text = role.displayName,
                selected = uiState.rol == role.apiValue,
                onClick = {
                    viewModel.onRoleChanged(
                        role.apiValue
                    )
                }
            )
        }

        if (uiState.rol == "arbitro") {
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Datos del árbitro",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            CreateUserField(
                value = uiState.certificacion,
                label = "Certificación",
                onValueChange =
                    viewModel::onCertificacionChanged
            )

            CreateUserField(
                value = uiState.aniosExperiencia,
                label = "Años de experiencia",
                onValueChange =
                    viewModel::onAniosExperienciaChanged
            )
        }

        if (uiState.rol == "jugador") {
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Datos del jugador",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            CreateUserField(
                value = uiState.numeroCamiseta,
                label = "Número de camiseta",
                onValueChange =
                    viewModel::onNumeroCamisetaChanged
            )

            CreateUserField(
                value = uiState.posicion,
                label = "Posición",
                onValueChange =
                    viewModel::onPosicionChanged
            )
        }

        OutlinedTextField(
            value = uiState.password,
            onValueChange =
                viewModel::onPasswordChanged,
            label = {
                Text("Contraseña temporal")
            },
            supportingText = {
                Text(
                    "Si queda vacía, se utilizará la CURP."
                )
            },
            visualTransformation =
                PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(
                    vertical = 12.dp
                )
            )
        }

        Button(
            onClick = viewModel::createUser,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Crear usuario")
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
private fun CreateUserField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    )
}

@Composable
private fun SelectionOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )

        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

private enum class CreateUserRole(
    val displayName: String,
    val apiValue: String
) {
    ADMIN_PRINCIPAL(
        displayName = "Administrador principal",
        apiValue = "admin_principal"
    ),
    ADMIN(
        displayName = "Administrador",
        apiValue = "admin"
    ),
    FINANZAS(
        displayName = "Finanzas",
        apiValue = "finanzas"
    ),
    ARBITRO(
        displayName = "Árbitro",
        apiValue = "arbitro"
    ),
    JUGADOR(
        displayName = "Jugador",
        apiValue = "jugador"
    )
}