package com.example.lacantera.ui.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.data.model.Usuario

@Composable
fun UsersScreen(
    onBackClick: () -> Unit,
    onUserClick: (Int) -> Unit,
    onCreateUserClick: () -> Unit,
    refreshRequested: Boolean,
    onRefreshConsumed: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: UsersViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(refreshRequested) {
        if (refreshRequested) {
            viewModel.refreshUsers()
            onRefreshConsumed()
        }
    }

    LaunchedEffect(uiState.sessionExpired) {
        if (uiState.sessionExpired) {
            viewModel.consumeSessionExpired()
            onSessionExpired()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        UsersHeader(
            onBackClick = onBackClick,
            onCreateUserClick = onCreateUserClick
        )

        UsersFilters(
            search = uiState.search,
            selectedRole = uiState.selectedRole,
            selectedStatus = uiState.selectedStatus,
            onSearchChanged = viewModel::onSearchChanged,
            onRoleSelected = viewModel::selectRole,
            onStatusSelected = viewModel::selectStatus,
            onClearFilters = viewModel::clearFilters
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null &&
                    uiState.users.isEmpty() -> {
                UsersErrorContent(
                    message = uiState.errorMessage
                        ?: "Error desconocido",
                    accessDenied = uiState.accessDenied,
                    onRetry = viewModel::refreshUsers,
                    onBackClick = onBackClick
                )
            }

            uiState.users.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron usuarios."
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Total de usuarios: ${uiState.count}",
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    itemsIndexed(
                        items = uiState.users,
                        key = { _, user ->
                            user.id
                        }
                    ) { index, user ->
                        UserCard(
                            user = user,
                            onClick = {
                                onUserClick(user.id)
                            }
                        )

                        if (
                            index == uiState.users.lastIndex &&
                            uiState.hasNextPage &&
                            !uiState.isLoadingMore
                        ) {
                            LaunchedEffect(
                                uiState.currentPage,
                                uiState.users.size
                            ) {
                                viewModel.loadNextPage()
                            }
                        }
                    }

                    if (uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment =
                                    Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    uiState.errorMessage?.let { message ->
                        item {
                            Text(
                                text = message,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                color =
                                    MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UsersHeader(
    onBackClick: () -> Unit,
    onCreateUserClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
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
                text = "Gestión de usuarios",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onCreateUserClick
        ) {
            Text("Nuevo")
        }
    }
}

@Composable
private fun UsersFilters(
    search: String,
    selectedRole: String?,
    selectedStatus: String?,
    onSearchChanged: (String) -> Unit,
    onRoleSelected: (String?) -> Unit,
    onStatusSelected: (String?) -> Unit,
    onClearFilters: () -> Unit
) {
    var roleMenuExpanded by remember {
        mutableStateOf(false)
    }

    var statusMenuExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
    ) {
        OutlinedTextField(
            value = search,
            onValueChange = onSearchChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Buscar usuario")
            },
            placeholder = {
                Text("Nombre, correo o usuario")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {}
            )
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        roleMenuExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = roleDisplayName(
                            selectedRole
                        )
                    )
                }

                DropdownMenu(
                    expanded = roleMenuExpanded,
                    onDismissRequest = {
                        roleMenuExpanded = false
                    }
                ) {
                    UserRole.entries.forEach { role ->
                        DropdownMenuItem(
                            text = {
                                Text(role.displayName)
                            },
                            onClick = {
                                roleMenuExpanded = false
                                onRoleSelected(role.apiValue)
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        statusMenuExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = statusDisplayName(
                            selectedStatus
                        )
                    )
                }

                DropdownMenu(
                    expanded = statusMenuExpanded,
                    onDismissRequest = {
                        statusMenuExpanded = false
                    }
                ) {
                    UserStatus.entries.forEach { status ->
                        DropdownMenuItem(
                            text = {
                                Text(status.displayName)
                            },
                            onClick = {
                                statusMenuExpanded = false
                                onStatusSelected(
                                    status.apiValue
                                )
                            }
                        )
                    }
                }
            }
        }

        if (
            search.isNotBlank() ||
            selectedRole != null ||
            selectedStatus != null
        ) {
            OutlinedButton(
                onClick = onClearFilters,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Limpiar filtros")
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}

@Composable
private fun UserCard(
    user: Usuario,
    onClick: () -> Unit
) {
    val displayName = user.nombreCorto.ifBlank {
        listOf(
            user.firstName,
            user.lastName
        ).filter {
            it.isNotBlank()
        }.joinToString(" ").ifBlank {
            user.username
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = displayName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "@${user.username}",
                color = MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
            )

            if (user.email.isNotBlank()) {
                Text(
                    text = user.email,
                    color = MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = userTypeDisplayName(
                        user.tipoUsuario.ifBlank {
                            user.rol
                        }
                    ),
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = if (user.isActive) {
                        "Activo"
                    } else {
                        "Inactivo"
                    },
                    color = if (user.isActive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (user.isCapitan) {
                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = "Capitán de equipo",
                    fontSize = 13.sp,
                    color = MaterialTheme
                        .colorScheme
                        .secondary
                )
            }
        }
    }
}

@Composable
private fun UsersErrorContent(
    message: String,
    accessDenied: Boolean,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = if (accessDenied) {
                onBackClick
            } else {
                onRetry
            }
        ) {
            Text(
                if (accessDenied) {
                    "Regresar"
                } else {
                    "Reintentar"
                }
            )
        }
    }
}

private enum class UserRole(
    val displayName: String,
    val apiValue: String?
) {
    ALL(
        displayName = "Todos los roles",
        apiValue = null
    ),
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

private enum class UserStatus(
    val displayName: String,
    val apiValue: String?
) {
    ALL(
        displayName = "Todos los estados",
        apiValue = null
    ),
    ACTIVE(
        displayName = "Activos",
        apiValue = "activo"
    ),
    INACTIVE(
        displayName = "Inactivos",
        apiValue = "inactivo"
    )
}

private fun roleDisplayName(
    selectedRole: String?
): String {
    return UserRole.entries
        .firstOrNull {
            it.apiValue == selectedRole
        }
        ?.displayName
        ?: "Todos los roles"
}

private fun statusDisplayName(
    selectedStatus: String?
): String {
    return UserStatus.entries
        .firstOrNull {
            it.apiValue == selectedStatus
        }
        ?.displayName
        ?: "Todos los estados"
}

private fun userTypeDisplayName(
    type: String
): String {
    return when (type.lowercase()) {
        "superadmin" -> "Superadministrador"
        "staff" -> "Staff"
        "admin_principal" ->
            "Administrador principal"
        "admin" -> "Administrador"
        "finanzas" -> "Finanzas"
        "arbitro" -> "Árbitro"
        "capitan" -> "Capitán"
        "jugador" -> "Jugador"
        else -> "Sin rol"
    }
}