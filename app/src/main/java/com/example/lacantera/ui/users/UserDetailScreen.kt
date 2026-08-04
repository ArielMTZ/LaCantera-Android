package com.example.lacantera.ui.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UserDetailScreen(
    userId: Int,
    onBackClick: () -> Unit,
    onUpdateCompleted: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: UserDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUser(
            userId = userId
        )
    }

    LaunchedEffect(uiState.updateCompleted) {
        if (uiState.updateCompleted) {
            onUpdateCompleted()
        }
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            viewModel.consumeSessionExpired()
            onSessionExpired()
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
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
                text = "Editar usuario",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        uiState.usuario?.let { user ->
            Text(
                text = "@${user.username}",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )
        }

        UserTextField(
            value = uiState.firstName,
            label = "Nombre",
            onValueChange =
                viewModel::onFirstNameChanged
        )

        UserTextField(
            value = uiState.lastName,
            label = "Primer apellido",
            onValueChange =
                viewModel::onLastNameChanged
        )

        UserTextField(
            value = uiState.segundoApellido,
            label = "Segundo apellido",
            onValueChange =
                viewModel::onSegundoApellidoChanged
        )

        UserTextField(
            value = uiState.email,
            label = "Correo electrónico",
            onValueChange =
                viewModel::onEmailChanged
        )

        UserTextField(
            value = uiState.telefono,
            label = "Teléfono",
            onValueChange =
                viewModel::onTelefonoChanged
        )

        UserTextField(
            value = uiState.fechaNacimiento,
            label = "Fecha de nacimiento (AAAA-MM-DD)",
            onValueChange =
                viewModel::onFechaNacimientoChanged
        )

        UserTextField(
            value = uiState.nacionalidad,
            label = "Nacionalidad",
            onValueChange =
                viewModel::onNacionalidadChanged
        )

        UserTextField(
            value = uiState.sexo,
            label = "Sexo",
            onValueChange =
                viewModel::onSexoChanged
        )

        UserTextField(
            value = uiState.estadoNacimiento,
            label = "Estado de nacimiento",
            onValueChange =
                viewModel::onEstadoNacimientoChanged
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Rol",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        EditableUserRole.entries.forEach { role ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected =
                            uiState.rol == role.apiValue,
                        onClick = {
                            viewModel.onRoleChanged(
                                role.apiValue
                            )
                        },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 5.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                RadioButton(
                    selected =
                        uiState.rol == role.apiValue,
                    onClick = null
                )

                Text(
                    text = role.displayName,
                    modifier = Modifier.padding(
                        start = 8.dp
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Cuenta activa",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (uiState.isActive) {
                        "El usuario puede iniciar sesión."
                    } else {
                        "El usuario está desactivado."
                    },
                    color = MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
                )
            }

            Switch(
                checked = uiState.isActive,
                onCheckedChange =
                    viewModel::onActiveChanged
            )
        }

        uiState.errorMessage?.let { message ->
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            onClick = viewModel::updateUser,
            enabled = !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator()
            } else {
                Text("Guardar cambios")
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
private fun UserTextField(
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        singleLine = true
    )
}

private enum class EditableUserRole(
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