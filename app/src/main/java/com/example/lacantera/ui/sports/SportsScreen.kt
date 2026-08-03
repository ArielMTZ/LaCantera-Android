package com.example.lacantera.ui.sports

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.data.model.CategoryItem
import com.example.lacantera.data.model.PositionItem
import com.example.lacantera.data.model.SportItem

@Composable
fun SportsScreen(
    onBackClick: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: SportsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var showCreateSportDialog by remember {
        mutableStateOf(false)
    }

    var sportToEdit by remember {
        mutableStateOf<SportItem?>(null)
    }

    var sportToDelete by remember {
        mutableStateOf<SportItem?>(null)
    }

    var sportForNewCategory by remember {
        mutableStateOf<SportItem?>(null)
    }

    var categoryToEdit by remember {
        mutableStateOf<CategoryItem?>(null)
    }

    var categoryToDelete by remember {
        mutableStateOf<CategoryItem?>(null)
    }

    var sportForNewPosition by remember {
        mutableStateOf<SportItem?>(null)
    }

    var positionToDelete by remember {
        mutableStateOf<PositionItem?>(null)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBackClick
            ) {
                Text("Regresar")
            }

            Text(
                text = "Deportes y categorías",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                showCreateSportDialog = true
            },
            enabled = !uiState.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                )
        ) {
            Text("Nuevo deporte")
        }

        uiState.successMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )
        }

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.sports.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {
                    Text("No hay deportes registrados.")

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    OutlinedButton(
                        onClick = viewModel::loadSports
                    ) {
                        Text("Recargar")
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = uiState.sports,
                        key = { sport ->
                            sport.id
                        }
                    ) { sport ->
                        SportCard(
                            sport = sport,
                            enabled = !uiState.isSaving,
                            onEditSport = {
                                sportToEdit = sport
                            },
                            onDeleteSport = {
                                sportToDelete = sport
                            },
                            onAddCategory = {
                                sportForNewCategory = sport
                            },
                            onEditCategory = { category ->
                                categoryToEdit = category
                            },
                            onDeleteCategory = { category ->
                                categoryToDelete = category
                            },
                            onAddPosition = {
                                sportForNewPosition = sport
                            },
                            onDeletePosition = { position ->
                                positionToDelete = position
                            }
                        )
                    }
                }
            }
        }
    }

    if (uiState.isSaving) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (showCreateSportDialog) {
        SportFormDialog(
            title = "Nuevo deporte",
            initialName = "",
            confirmText = "Crear",
            onDismiss = {
                showCreateSportDialog = false
            },
            onConfirm = { nombre ->
                viewModel.createSport(nombre)
                showCreateSportDialog = false
            }
        )
    }

    sportToEdit?.let { sport ->
        SportFormDialog(
            title = "Editar deporte",
            initialName = sport.nombre,
            confirmText = "Guardar",
            onDismiss = {
                sportToEdit = null
            },
            onConfirm = { nombre ->
                viewModel.updateSport(
                    sportId = sport.id,
                    nombre = nombre
                )

                sportToEdit = null
            }
        )
    }

    sportToDelete?.let { sport ->
        DeleteConfirmationDialog(
            title = "Eliminar deporte",
            message = (
                    "¿Deseas eliminar el deporte " +
                            "'${sport.nombre}'?"
                    ),
            onDismiss = {
                sportToDelete = null
            },
            onConfirm = {
                viewModel.deleteSport(
                    sportId = sport.id
                )

                sportToDelete = null
            }
        )
    }

    sportForNewCategory?.let { sport ->
        CategoryFormDialog(
            title = "Nueva categoría",
            initialCategory = null,
            onDismiss = {
                sportForNewCategory = null
            },
            onConfirm = {
                    nombre,
                    sexo,
                    edadMinima,
                    edadMaxima ->

                viewModel.createCategory(
                    sportId = sport.id,
                    nombre = nombre,
                    sexo = sexo,
                    edadMinima = edadMinima,
                    edadMaxima = edadMaxima
                )

                sportForNewCategory = null
            }
        )
    }

    categoryToEdit?.let { category ->
        CategoryFormDialog(
            title = "Editar categoría",
            initialCategory = category,
            onDismiss = {
                categoryToEdit = null
            },
            onConfirm = {
                    nombre,
                    sexo,
                    edadMinima,
                    edadMaxima ->

                viewModel.updateCategory(
                    categoryId = category.id,
                    nombre = nombre,
                    sexo = sexo,
                    edadMinima = edadMinima,
                    edadMaxima = edadMaxima
                )

                categoryToEdit = null
            }
        )
    }

    categoryToDelete?.let { category ->
        DeleteConfirmationDialog(
            title = "Eliminar categoría",
            message = (
                    "¿Deseas eliminar la categoría " +
                            "'${category.nombre}'?"
                    ),
            onDismiss = {
                categoryToDelete = null
            },
            onConfirm = {
                viewModel.deleteCategory(
                    categoryId = category.id
                )

                categoryToDelete = null
            }
        )
    }

    sportForNewPosition?.let { sport ->
        PositionFormDialog(
            sportName = sport.nombre,
            onDismiss = {
                sportForNewPosition = null
            },
            onConfirm = { nombre ->
                viewModel.createPosition(
                    sportId = sport.id,
                    nombre = nombre
                )

                sportForNewPosition = null
            }
        )
    }

    positionToDelete?.let { position ->
        DeleteConfirmationDialog(
            title = "Eliminar posición",
            message = (
                    "¿Deseas eliminar la posición " +
                            "'${position.nombre}'?"
                    ),
            onDismiss = {
                positionToDelete = null
            },
            onConfirm = {
                viewModel.deletePosition(
                    positionId = position.id
                )

                positionToDelete = null
            }
        )
    }
}

@Composable
private fun SportCard(
    sport: SportItem,
    enabled: Boolean,
    onEditSport: () -> Unit,
    onDeleteSport: () -> Unit,
    onAddCategory: () -> Unit,
    onEditCategory: (CategoryItem) -> Unit,
    onDeleteCategory: (CategoryItem) -> Unit,
    onAddPosition: () -> Unit,
    onDeletePosition: (PositionItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                text = sport.nombre,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEditSport,
                    enabled = enabled,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }

                OutlinedButton(
                    onClick = onDeleteSport,
                    enabled = enabled,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar")
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = (
                            "Categorías " +
                                    "(${sport.categories.size})"
                            ),
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold
                )

                TextButton(
                    onClick = onAddCategory,
                    enabled = enabled
                ) {
                    Text("Agregar")
                }
            }

            if (sport.categories.isEmpty()) {
                Text(
                    text = "Sin categorías.",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                sport.categories.forEach { category ->
                    CategoryRow(
                        category = category,
                        enabled = enabled,
                        onEdit = {
                            onEditCategory(category)
                        },
                        onDelete = {
                            onDeleteCategory(category)
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Text(
                    text = (
                            "Posiciones " +
                                    "(${sport.positions.size})"
                            ),
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold
                )

                TextButton(
                    onClick = onAddPosition,
                    enabled = enabled
                ) {
                    Text("Agregar")
                }
            }

            if (sport.positions.isEmpty()) {
                Text(
                    text = "Sin posiciones.",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                sport.positions.forEach { position ->
                    PositionRow(
                        position = position,
                        enabled = enabled,
                        onDelete = {
                            onDeletePosition(position)
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryRow(
    category: CategoryItem,
    enabled: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = category.nombre,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = category.sexDisplay
            )

            val edad = when {
                category.edadMinima != null &&
                        category.edadMaxima != null -> {
                    "${category.edadMinima} a " +
                            "${category.edadMaxima} años"
                }

                category.edadMinima != null -> {
                    "Desde ${category.edadMinima} años"
                }

                category.edadMaxima != null -> {
                    "Hasta ${category.edadMaxima} años"
                }

                else -> {
                    "Sin límite de edad"
                }
            }

            Text(
                text = edad,
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.End
            ) {
                TextButton(
                    onClick = onEdit,
                    enabled = enabled
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = onDelete,
                    enabled = enabled
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
private fun PositionRow(
    position: PositionItem,
    enabled: Boolean,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            Text(
                text = position.nombre,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onDelete,
                enabled = enabled
            ) {
                Text("Eliminar")
            }
        }
    }
}

@Composable
private fun SportFormDialog(
    title: String,
    initialName: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var nombre by remember(initialName) {
        mutableStateOf(initialName)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                },
                label = {
                    Text("Nombre")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(nombre)
                },
                enabled = nombre.isNotBlank()
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun CategoryFormDialog(
    title: String,
    initialCategory: CategoryItem?,
    onDismiss: () -> Unit,
    onConfirm: (
        nombre: String,
        sexo: String,
        edadMinima: Int?,
        edadMaxima: Int?
    ) -> Unit
) {
    var nombre by remember(initialCategory) {
        mutableStateOf(
            initialCategory?.nombre.orEmpty()
        )
    }

    var sexo by remember(initialCategory) {
        mutableStateOf(
            initialCategory?.sexo ?: "X"
        )
    }

    var edadMinima by remember(initialCategory) {
        mutableStateOf(
            initialCategory
                ?.edadMinima
                ?.toString()
                .orEmpty()
        )
    }

    var edadMaxima by remember(initialCategory) {
        mutableStateOf(
            initialCategory
                ?.edadMaxima
                ?.toString()
                .orEmpty()
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                    },
                    label = {
                        Text("Nombre")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Tipo de categoría",
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(6.dp)
                ) {
                    SexoButton(
                        text = "Hombres",
                        selected = sexo == "H",
                        onClick = {
                            sexo = "H"
                        },
                        modifier = Modifier.weight(1f)
                    )

                    SexoButton(
                        text = "Mujeres",
                        selected = sexo == "M",
                        onClick = {
                            sexo = "M"
                        },
                        modifier = Modifier.weight(1f)
                    )

                    SexoButton(
                        text = "Mixto",
                        selected = sexo == "X",
                        onClick = {
                            sexo = "X"
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = edadMinima,
                    onValueChange = { value ->
                        if (
                            value.isEmpty() ||
                            value.all(Char::isDigit)
                        ) {
                            edadMinima = value
                        }
                    },
                    label = {
                        Text("Edad mínima")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = edadMaxima,
                    onValueChange = { value ->
                        if (
                            value.isEmpty() ||
                            value.all(Char::isDigit)
                        ) {
                            edadMaxima = value
                        }
                    },
                    label = {
                        Text("Edad máxima")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        nombre.trim(),
                        sexo,
                        edadMinima.toIntOrNull(),
                        edadMaxima.toIntOrNull()
                    )
                },
                enabled = nombre.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun SexoButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(
                text = text,
                fontSize = 11.sp
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Text(
                text = text,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun PositionFormDialog(
    sportName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var nombre by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Nueva posición")
        },
        text = {
            Column {
                Text(
                    text = "Deporte: $sportName"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                    },
                    label = {
                        Text("Nombre de la posición")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(nombre)
                },
                enabled = nombre.isNotBlank()
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun DeleteConfirmationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(title)
        },
        text = {
            Text(message)
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}