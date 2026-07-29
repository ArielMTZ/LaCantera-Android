package com.example.lacantera.data.model

import com.google.gson.annotations.SerializedName



data class UserPermissions(
    @SerializedName("ver_dashboard")
    val verDashboard: Boolean = false,

    @SerializedName("administrar_deportes")
    val administrarDeportes: Boolean = false,

    @SerializedName("ver_logs")
    val verLogs: Boolean = false,

    @SerializedName("ver_usuarios_bloqueados")
    val verUsuariosBloqueados: Boolean = false,

    @SerializedName("ver_solicitudes_recuperacion")
    val verSolicitudesRecuperacion: Boolean = false,

    @SerializedName("administrar_equipos")
    val administrarEquipos: Boolean = false,

    @SerializedName("administrar_usuarios")
    val administrarUsuarios: Boolean = false,

    @SerializedName("ver_temporada_actual")
    val verTemporadaActual: Boolean = false,

    @SerializedName("ver_historial_temporadas")
    val verHistorialTemporadas: Boolean = false,

    @SerializedName("ver_inscripciones")
    val verInscripciones: Boolean = false,

    @SerializedName("ver_arbitrajes")
    val verArbitrajes: Boolean = false,

    @SerializedName("ver_historial_inscripciones")
    val verHistorialInscripciones: Boolean = false,

    @SerializedName("ver_mis_partidos")
    val verMisPartidos: Boolean = false,

    @SerializedName("ver_historial_partidos")
    val verHistorialPartidos: Boolean = false,

    @SerializedName("ver_panel_arbitro")
    val verPanelArbitro: Boolean = false,

    @SerializedName("ver_mis_equipos")
    val verMisEquipos: Boolean = false,

    @SerializedName("ver_panel_capitan")
    val verPanelCapitan: Boolean = false,

    @SerializedName("ver_historial_juegos_capitan")
    val verHistorialJuegosCapitan: Boolean = false,

    @SerializedName("ver_historial_pagos_capitan")
    val verHistorialPagosCapitan: Boolean = false,

    @SerializedName("ver_estadisticas")
    val verEstadisticas: Boolean = false
)

