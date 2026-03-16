package com.jjmf.colegiotrenerandroid.domain.repository

import com.jjmf.colegiotrenerandroid.core.Result
import com.jjmf.colegiotrenerandroid.domain.model.EstadoCalPendiente
import com.jjmf.colegiotrenerandroid.domain.model.EstadoCalPendienteDia
import com.jjmf.colegiotrenerandroid.domain.model.Incumplimiento
import com.jjmf.colegiotrenerandroid.domain.model.CorreoMasivo

interface TareaRepository {

    suspend fun getEstadoCalPendiente(
        ctacli: String,
        anio: String,
        mes: String
    ): Result<List<EstadoCalPendiente>>

    suspend fun getEstadoCalPendienteDia(
        ctacli: String,
        anio: String,
        mes: String,
        dia: String
    ): Result<List<EstadoCalPendienteDia>>

    suspend fun listarIncumplimientos(ctactli: String): Result<List<Incumplimiento>>

    suspend fun getCorreos(
        ctacli: String,
        dia: String,
        mes: String,
        anio: String
    ): Result<List<CorreoMasivo>>

}

