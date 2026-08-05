package com.example.lacantera.ui.sports

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lacantera.data.model.CategoryItem
import com.example.lacantera.data.model.PositionItem
import com.example.lacantera.data.model.SportItem

/*
 * =========================================================
 * COLORES
 * =========================================================
 */

private val SportsBackground = Color(0xFFF3F6FB)
private val SportsSurface = Color(0xFFFFFFFF)

private val SportsNavy = Color(0xFF071E4B)
private val SportsNavyLight = Color(0xFF153F7C)
private val SportsBlue = Color(0xFF2463B6)

private val SportsText = Color(0xFF111C35)
private val SportsMuted = Color(0xFF68748A)
private val SportsBorder = Color(0xFFDCE4EF)

private val SportsSoftBlue = Color(0xFFEAF2FF)
private val SportsSoftGreen = Color(0xFFE9F8F0)
private val SportsSoftOrange = Color(0xFFFFF1E2)
private val SportsSoftRed = Color(0xFFFFEAEA)
private val SportsSoftPurple = Color(0xFFF2ECFF)

private val SportsGreen = Color(0xFF168052)
private val SportsOrange = Color(0xFFB76500)
private val SportsRed = Color(0xFFC52D33)
private val SportsPurple = Color(0xFF6741B5)

/*
 * =========================================================
 * PANTALLA PRINCIPAL
 * =========================================================
 */

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

    val totalSports = uiState.sports.size

    val totalCategories = uiState.sports.sumOf { sport ->
        sport.categories.size
    }

    val totalPositions = uiState.sports.sumOf { sport ->
        sport.positions.size
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(SportsBackground)
    ) {
        val compactScreen = maxWidth < 370.dp

        val horizontalPadding = if (compactScreen) {
            14.dp
        } else {
            18.dp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            SportsTopBar(
                onBackClick = onBackClick,
                compactScreen = compactScreen
            )

            when {
                uiState.isLoading -> {
                    SportsLoadingState()
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = horizontalPadding,
                            end = horizontalPadding,
                            top = 16.dp,
                            bottom = 28.dp
                        ),
                        verticalArrangement =
                            Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            SportsOverviewCard(
                                totalSports = totalSports,
                                totalCategories = totalCategories,
                                totalPositions = totalPositions,
                                compactScreen = compactScreen,
                                enabled = !uiState.isSaving,
                                onCreateSport = {
                                    showCreateSportDialog = true
                                }
                            )
                        }

                        uiState.successMessage?.let { message ->
                            item {
                                SportsMessageCard(
                                    message = message,
                                    isError = false
                                )
                            }
                        }

                        uiState.errorMessage?.let { message ->
                            item {
                                SportsMessageCard(
                                    message = message,
                                    isError = true
                                )
                            }
                        }

                        item {
                            SportsDirectoryHeader(
                                totalSports = totalSports
                            )
                        }

                        if (uiState.sports.isEmpty()) {
                            item {
                                EmptySportsState(
                                    enabled = !uiState.isSaving,
                                    onCreateSport = {
                                        showCreateSportDialog = true
                                    },
                                    onReload = viewModel::loadSports
                                )
                            }
                        } else {
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
        }

        if (uiState.isSaving) {
            SavingOverlay()
        }
    }

    /*
     * =====================================================
     * DIÁLOGO: CREAR DEPORTE
     * =====================================================
     */

    if (showCreateSportDialog) {
        SportFormDialog(
            title = "Nuevo deporte",
            initialName = "",
            confirmText = "Crear deporte",
            onDismiss = {
                showCreateSportDialog = false
            },
            onConfirm = { nombre ->
                viewModel.createSport(nombre)
                showCreateSportDialog = false
            }
        )
    }

    /*
     * =====================================================
     * DIÁLOGO: EDITAR DEPORTE
     * =====================================================
     */

    sportToEdit?.let { sport ->
        SportFormDialog(
            title = "Editar deporte",
            initialName = sport.nombre,
            confirmText = "Guardar cambios",
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

    /*
     * =====================================================
     * DIÁLOGO: ELIMINAR DEPORTE
     * =====================================================
     */

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

    /*
     * =====================================================
     * DIÁLOGO: CREAR CATEGORÍA
     * =====================================================
     */

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

    /*
     * =====================================================
     * DIÁLOGO: EDITAR CATEGORÍA
     * =====================================================
     */

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

    /*
     * =====================================================
     * DIÁLOGO: ELIMINAR CATEGORÍA
     * =====================================================
     */

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

    /*
     * =====================================================
     * DIÁLOGO: CREAR POSICIÓN
     * =====================================================
     */

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

    /*
     * =====================================================
     * DIÁLOGO: ELIMINAR POSICIÓN
     * =====================================================
     */

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

/*
 * =========================================================
 * BARRA SUPERIOR
 * =========================================================
 */

@Composable
private fun SportsTopBar(
    onBackClick: () -> Unit,
    compactScreen: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SportsSurface)
            .padding(
                horizontal = if (compactScreen) {
                    14.dp
                } else {
                    18.dp
                },
                vertical = 12.dp
            )
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.clickable(
                onClick = onBackClick
            ),
            shape = RoundedCornerShape(15.dp),
            color = SportsBackground,
            border = BorderStroke(
                width = 1.dp,
                color = SportsBorder
            )
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 9.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "‹",
                    color = SportsNavy,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Volver",
                    color = SportsNavy,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Gestión deportiva",
                color = SportsText,
                fontSize = if (compactScreen) {
                    17.sp
                } else {
                    19.sp
                },
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Deportes y categorías",
                color = SportsMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(14.dp),
            color = SportsNavy
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "LC",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

/*
 * =========================================================
 * TARJETA DE RESUMEN
 * =========================================================
 */

@Composable
private fun SportsOverviewCard(
    totalSports: Int,
    totalCategories: Int,
    totalPositions: Int,
    compactScreen: Boolean,
    enabled: Boolean,
    onCreateSport: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = SportsNavy
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            SportsNavy,
                            SportsNavyLight,
                            SportsBlue
                        )
                    )
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 21.dp
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "CONFIGURACIÓN DE LA LIGA",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Organiza la estructura deportiva",
                    color = Color.White,
                    fontSize = if (compactScreen) {
                        20.sp
                    } else {
                        23.sp
                    },
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = (
                            "Administra los deportes, categorías " +
                                    "y posiciones disponibles."
                            ),
                    color = Color.White.copy(alpha = 0.72f),
                    fontSize = 11.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(9.dp)
                ) {
                    SportsMetric(
                        value = totalSports,
                        label = "Deportes",
                        modifier = Modifier.weight(1f)
                    )

                    SportsMetric(
                        value = totalCategories,
                        label = "Categorías",
                        modifier = Modifier.weight(1f)
                    )

                    SportsMetric(
                        value = totalPositions,
                        label = "Posiciones",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onCreateSport,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = enabled,
                    shape = RoundedCornerShape(17.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = SportsNavy,
                        disabledContainerColor =
                            Color.White.copy(alpha = 0.55f),
                        disabledContentColor =
                            SportsNavy.copy(alpha = 0.65f)
                    )
                ) {
                    Text(
                        text = "+  NUEVO DEPORTE",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SportsMetric(
    value: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.12f),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.16f)
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 13.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value.toString(),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = label,
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

/*
 * =========================================================
 * ENCABEZADO DEL DIRECTORIO
 * =========================================================
 */

@Composable
private fun SportsDirectoryHeader(
    totalSports: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Directorio deportivo",
                color = SportsText,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Gestiona cada deporte y su configuración.",
                color = SportsMuted,
                fontSize = 12.sp
            )
        }

        Surface(
            shape = RoundedCornerShape(50.dp),
            color = SportsSoftBlue
        ) {
            Text(
                text = "$totalSports registrados",
                modifier = Modifier.padding(
                    horizontal = 11.dp,
                    vertical = 7.dp
                ),
                color = SportsBlue,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

/*
 * =========================================================
 * TARJETA DE DEPORTE
 * =========================================================
 */

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
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = SportsSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = SportsBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            /*
             * Encabezado del deporte.
             */

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(19.dp),
                    color = SportsSoftBlue
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = sport.nombre
                                .trim()
                                .take(1)
                                .uppercase()
                                .ifBlank {
                                    "D"
                                },
                            color = SportsBlue,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(13.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = sport.nombre,
                        color = SportsText,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(7.dp)
                    ) {
                        CountBadge(
                            text = "${sport.categories.size} categorías",
                            backgroundColor = SportsSoftGreen,
                            contentColor = SportsGreen
                        )

                        CountBadge(
                            text = "${sport.positions.size} posiciones",
                            backgroundColor = SportsSoftOrange,
                            contentColor = SportsOrange
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {
                SecondaryActionButton(
                    text = "Editar deporte",
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    onClick = onEditSport
                )

                DangerActionButton(
                    text = "Eliminar",
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    onClick = onDeleteSport
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            HorizontalDivider(
                color = SportsBorder
            )

            Spacer(modifier = Modifier.height(18.dp))

            /*
             * Categorías.
             */

            SubsectionHeader(
                title = "Categorías",
                subtitle = (
                        "${sport.categories.size} " +
                                if (sport.categories.size == 1) {
                                    "categoría registrada"
                                } else {
                                    "categorías registradas"
                                }
                        ),
                buttonText = "Agregar",
                buttonColor = SportsBlue,
                buttonBackground = SportsSoftBlue,
                enabled = enabled,
                onClick = onAddCategory
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (sport.categories.isEmpty()) {
                EmptySubsection(
                    title = "Sin categorías",
                    description = (
                            "Agrega la primera categoría " +
                                    "para este deporte."
                            ),
                    accentColor = SportsBlue,
                    backgroundColor = SportsSoftBlue
                )
            } else {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {
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
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            /*
             * Posiciones.
             */

            SubsectionHeader(
                title = "Posiciones",
                subtitle = (
                        "${sport.positions.size} " +
                                if (sport.positions.size == 1) {
                                    "posición registrada"
                                } else {
                                    "posiciones registradas"
                                }
                        ),
                buttonText = "Agregar",
                buttonColor = SportsPurple,
                buttonBackground = SportsSoftPurple,
                enabled = enabled,
                onClick = onAddPosition
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (sport.positions.isEmpty()) {
                EmptySubsection(
                    title = "Sin posiciones",
                    description = (
                            "Todavía no existen posiciones " +
                                    "para este deporte."
                            ),
                    accentColor = SportsPurple,
                    backgroundColor = SportsSoftPurple
                )
            } else {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    sport.positions.forEach { position ->
                        PositionRow(
                            position = position,
                            enabled = enabled,
                            onDelete = {
                                onDeletePosition(position)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountBadge(
    text: String,
    backgroundColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 6.dp
            ),
            color = contentColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1
        )
    }
}

/*
 * =========================================================
 * ENCABEZADOS DE CATEGORÍAS Y POSICIONES
 * =========================================================
 */

@Composable
private fun SubsectionHeader(
    title: String,
    subtitle: String,
    buttonText: String,
    buttonColor: Color,
    buttonBackground: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = SportsText,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = SportsMuted,
                fontSize = 10.sp
            )
        }

        Surface(
            modifier = Modifier.clickable(
                enabled = enabled,
                onClick = onClick
            ),
            shape = RoundedCornerShape(14.dp),
            color = if (enabled) {
                buttonBackground
            } else {
                SportsBorder.copy(alpha = 0.55f)
            }
        ) {
            Text(
                text = "+ $buttonText",
                modifier = Modifier.padding(
                    horizontal = 13.dp,
                    vertical = 9.dp
                ),
                color = if (enabled) {
                    buttonColor
                } else {
                    SportsMuted
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

/*
 * =========================================================
 * TARJETA DE CATEGORÍA
 * =========================================================
 */

@Composable
private fun CategoryRow(
    category: CategoryItem,
    enabled: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val ageText = buildAgeText(
        minimumAge = category.edadMinima,
        maximumAge = category.edadMaxima
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FBFE)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = SportsBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(42.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = SportsSoftGreen
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.nombre
                                .trim()
                                .take(1)
                                .uppercase()
                                .ifBlank {
                                    "C"
                                },
                            color = SportsGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(11.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = category.nombre,
                        color = SportsText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(7.dp)
                    ) {
                        CategoryDetailBadge(
                            text = category.sexDisplay.ifBlank {
                                category.sexo
                            },
                            backgroundColor = SportsSoftBlue,
                            contentColor = SportsBlue
                        )

                        CategoryDetailBadge(
                            text = ageText,
                            backgroundColor = SportsSoftGreen,
                            contentColor = SportsGreen
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(13.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                SecondaryActionButton(
                    text = "Editar",
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    onClick = onEdit
                )

                DangerActionButton(
                    text = "Eliminar",
                    enabled = enabled,
                    modifier = Modifier.weight(1f),
                    onClick = onDelete
                )
            }
        }
    }
}

@Composable
private fun CategoryDetailBadge(
    text: String,
    backgroundColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 5.dp
            ),
            color = contentColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

/*
 * =========================================================
 * FILA DE POSICIÓN
 * =========================================================
 */

@Composable
private fun PositionRow(
    position: PositionItem,
    enabled: Boolean,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        color = SportsSoftPurple,
        border = BorderStroke(
            width = 1.dp,
            color = SportsPurple.copy(alpha = 0.14f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 13.dp,
                vertical = 10.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = SportsPurple.copy(alpha = 0.13f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "P",
                        color = SportsPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = position.nombre,
                modifier = Modifier.weight(1f),
                color = SportsText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            TextButton(
                onClick = onDelete,
                enabled = enabled,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = SportsRed,
                    disabledContentColor =
                        SportsMuted.copy(alpha = 0.45f)
                )
            ) {
                Text(
                    text = "Eliminar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

/*
 * =========================================================
 * BOTONES
 * =========================================================
 */

@Composable
private fun SecondaryActionButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) {
                SportsBorder
            } else {
                SportsBorder.copy(alpha = 0.45f)
            }
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SportsSurface,
            contentColor = SportsNavy,
            disabledContainerColor =
                SportsSurface.copy(alpha = 0.65f),
            disabledContentColor =
                SportsMuted.copy(alpha = 0.45f)
        )
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun DangerActionButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(46.dp),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = SportsSoftRed,
            contentColor = SportsRed,
            disabledContainerColor =
                SportsSoftRed.copy(alpha = 0.45f),
            disabledContentColor =
                SportsRed.copy(alpha = 0.40f)
        )
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

/*
 * =========================================================
 * ESTADOS VACÍOS
 * =========================================================
 */

@Composable
private fun EmptySubsection(
    title: String,
    description: String,
    accentColor: Color,
    backgroundColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        color = backgroundColor.copy(alpha = 0.58f),
        border = BorderStroke(
            width = 1.dp,
            color = accentColor.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 14.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(14.dp),
                color = accentColor.copy(alpha = 0.12f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = accentColor,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(11.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = SportsText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = description,
                    color = SportsMuted,
                    fontSize = 10.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun EmptySportsState(
    enabled: Boolean,
    onCreateSport: () -> Unit,
    onReload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 560.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = SportsSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = SportsBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(66.dp),
                shape = RoundedCornerShape(22.dp),
                color = SportsSoftBlue
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = SportsBlue,
                        fontSize = 31.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No hay deportes registrados",
                color = SportsText,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = (
                        "Crea un deporte para comenzar a registrar " +
                                "categorías y posiciones."
                        ),
                color = SportsMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onCreateSport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(49.dp),
                enabled = enabled,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SportsNavy,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Crear deporte",
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onReload,
                enabled = enabled
            ) {
                Text(
                    text = "Recargar información",
                    color = SportsBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/*
 * =========================================================
 * MENSAJES
 * =========================================================
 */

@Composable
private fun SportsMessageCard(
    message: String,
    isError: Boolean
) {
    val backgroundColor = if (isError) {
        SportsSoftRed
    } else {
        SportsSoftGreen
    }

    val contentColor = if (isError) {
        SportsRed
    } else {
        SportsGreen
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor,
        border = BorderStroke(
            width = 1.dp,
            color = contentColor.copy(alpha = 0.16f)
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 13.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(31.dp),
                shape = CircleShape,
                color = contentColor.copy(alpha = 0.12f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isError) {
                            "!"
                        } else {
                            "✓"
                        },
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = message,
                modifier = Modifier.weight(1f),
                color = contentColor,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/*
 * =========================================================
 * CARGA Y GUARDADO
 * =========================================================
 */

@Composable
private fun SportsLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(70.dp),
                shape = RoundedCornerShape(23.dp),
                color = SportsNavy
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LC",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            CircularProgressIndicator(
                color = SportsNavy,
                strokeWidth = 3.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Cargando gestión deportiva...",
                color = SportsMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SavingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Black.copy(alpha = 0.34f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = SportsSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 30.dp,
                    vertical = 25.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = SportsNavy,
                    strokeWidth = 3.dp
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Guardando cambios",
                    color = SportsText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Espera un momento...",
                    color = SportsMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

/*
 * =========================================================
 * DIÁLOGO DE DEPORTE
 * =========================================================
 */

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
        shape = RoundedCornerShape(28.dp),
        containerColor = SportsSurface,
        titleContentColor = SportsText,
        textContentColor = SportsMuted,
        title = {
            Column {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Información general del deporte",
                    color = SportsMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        text = {
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                },
                label = {
                    Text("Nombre del deporte")
                },
                placeholder = {
                    Text("Ejemplo: Voleibol")
                },
                singleLine = true,
                shape = RoundedCornerShape(17.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = sportsTextFieldColors()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        nombre.trim()
                    )
                },
                enabled = nombre.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SportsNavy,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = confirmText,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = SportsMuted
                )
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

/*
 * =========================================================
 * DIÁLOGO DE CATEGORÍA
 * =========================================================
 */

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
        shape = RoundedCornerShape(28.dp),
        containerColor = SportsSurface,
        titleContentColor = SportsText,
        textContentColor = SportsMuted,
        title = {
            Column {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Configura el tipo y rango de edad",
                    color = SportsMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        text = {
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(13.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                    },
                    label = {
                        Text("Nombre de la categoría")
                    },
                    placeholder = {
                        Text("Ejemplo: Sala varonil A")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = sportsTextFieldColors()
                )

                Text(
                    text = "Tipo de categoría",
                    color = SportsText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(7.dp)
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
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = sportsTextFieldColors()
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
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = sportsTextFieldColors()
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
                enabled = nombre.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SportsNavy,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Guardar",
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = SportsMuted
                )
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold
                )
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
            modifier = modifier.height(44.dp),
            shape = RoundedCornerShape(14.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = SportsNavy,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(
                horizontal = 5.dp
            )
        ) {
            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(44.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(
                width = 1.dp,
                color = SportsBorder
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = SportsBackground,
                contentColor = SportsMuted
            ),
            contentPadding = PaddingValues(
                horizontal = 5.dp
            )
        ) {
            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

/*
 * =========================================================
 * DIÁLOGO DE POSICIÓN
 * =========================================================
 */

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
        shape = RoundedCornerShape(28.dp),
        containerColor = SportsSurface,
        titleContentColor = SportsText,
        textContentColor = SportsMuted,
        title = {
            Column {
                Text(
                    text = "Nueva posición",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Agrega una posición a $sportName",
                    color = SportsMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        text = {
            Column {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = SportsSoftPurple
                ) {
                    Text(
                        text = "Deporte seleccionado: $sportName",
                        modifier = Modifier.padding(
                            horizontal = 13.dp,
                            vertical = 11.dp
                        ),
                        color = SportsPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(13.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                    },
                    label = {
                        Text("Nombre de la posición")
                    },
                    placeholder = {
                        Text("Ejemplo: Portero")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(17.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = sportsTextFieldColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        nombre.trim()
                    )
                },
                enabled = nombre.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SportsNavy,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Crear posición",
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = SportsMuted
                )
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

/*
 * =========================================================
 * DIÁLOGO DE ELIMINACIÓN
 * =========================================================
 */

@Composable
private fun DeleteConfirmationDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = SportsSurface,
        titleContentColor = SportsText,
        textContentColor = SportsMuted,
        title = {
            Column {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(17.dp),
                    color = SportsSoftRed
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            color = SportsRed,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        text = {
            Text(
                text = message,
                color = SportsMuted,
                fontSize = 13.sp,
                lineHeight = 19.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SportsRed,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Eliminar",
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = SportsBorder
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SportsMuted
                )
            ) {
                Text(
                    text = "Cancelar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

/*
 * =========================================================
 * UTILIDADES
 * =========================================================
 */

@Composable
private fun sportsTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = SportsBackground,
        unfocusedContainerColor = SportsBackground,
        disabledContainerColor = SportsBackground,

        focusedBorderColor = SportsBlue,
        unfocusedBorderColor = SportsBorder,
        disabledBorderColor = SportsBorder,

        focusedTextColor = SportsText,
        unfocusedTextColor = SportsText,
        disabledTextColor = SportsMuted,

        focusedLabelColor = SportsNavy,
        unfocusedLabelColor = SportsMuted,

        cursorColor = SportsNavy
    )

private fun buildAgeText(
    minimumAge: Int?,
    maximumAge: Int?
): String {
    return when {
        minimumAge != null &&
                maximumAge != null -> {
            "$minimumAge a $maximumAge años"
        }

        minimumAge != null -> {
            "Desde $minimumAge años"
        }

        maximumAge != null -> {
            "Hasta $maximumAge años"
        }

        else -> {
            "Sin límite de edad"
        }
    }
}