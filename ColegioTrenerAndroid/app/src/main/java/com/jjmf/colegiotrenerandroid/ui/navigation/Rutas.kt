package com.jjmf.colegiotrenerandroid.ui.navigation

import androidx.navigation.NavHostController

sealed class Rutas(val route: String) {
    data object Login : Rutas("login")
    data object Menu : Rutas("menu")
    data object Inicio : Rutas("inicio")

    data object Notificaciones: Rutas("notificaciones")
    data object Correos: Rutas("correos")
    data object Perfil: Rutas("perfil")
    data object Administrativos {
        data object Datos : Rutas("administrativos/datos"){
            data object Padres: Rutas("${Datos.route}/padres")
            data object Apoderado: Rutas("${Datos.route}/apoderado")
            data object Hijos: Rutas("${Datos.route}/hijos")
            data object Clubes: Rutas("${Datos.route}/clubes")
        }
        data object Pagos : Rutas("administrativos/pagos")
        data object Inscripciones : Rutas("administrativos/inscripciones")
    }
    data object Asistencia {
        data object DiariaAcumulada : Rutas("asistencia/diaria_acumulada")
        data object Carnet : Rutas("asistencia/carnet")
        data object Justificacion : Rutas("asistencia/justificacion")
    }
    data object Tareas {
        data object Pendientes: Rutas("tareas/pendientes")
        data object Incumplimientos: Rutas("tareas/incumplimientos")
    }
    data object Resultados {
        data object CitaInforme: Rutas("resultados/cita_informe")
    }
    data object Autorizaciones : Rutas("Autorizaciones")
}


fun NavHostController.navigateTo(ruta: Rutas) {
    this.navigate(ruta.route)
}