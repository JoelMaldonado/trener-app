package com.jjmf.colegiotrenerandroid.data.repository

import com.jjmf.colegiotrenerandroid.app.Preferencias
import com.jjmf.colegiotrenerandroid.core.Result
import com.jjmf.colegiotrenerandroid.data.dto.EstadoCalPendienteDiaDto
import com.jjmf.colegiotrenerandroid.data.dto.EstadoCalPendienteDto
import com.jjmf.colegiotrenerandroid.data.dto.IncumplimientoDto
import com.jjmf.colegiotrenerandroid.data.dto.CorreoDto
import com.jjmf.colegiotrenerandroid.data.services.TareaService
import com.jjmf.colegiotrenerandroid.domain.model.EstadoCalPendiente
import com.jjmf.colegiotrenerandroid.domain.model.EstadoCalPendienteDia
import com.jjmf.colegiotrenerandroid.domain.model.Incumplimiento
import com.jjmf.colegiotrenerandroid.domain.model.CorreoMasivo
import com.jjmf.colegiotrenerandroid.domain.repository.TareaRepository
import com.jjmf.colegiotrenerandroid.domain.usecase.TokenUseCase
import com.jjmf.colegiotrenerandroid.util.convertJson
import javax.inject.Inject

class TareaRepositoryImpl @Inject constructor(
    private val api: TareaService,
    private val token: TokenUseCase
) : TareaRepository {
    override suspend fun getEstadoCalPendiente(
        ctacli: String,
        anio: String,
        mes: String
    ): Result<List<EstadoCalPendiente>> {
        return try {
            val call = api.getEstadosCalPendiente(
                ctacli = ctacli,
                anio = anio,
                mes = mes,
                token = token()
            )
            if (call.isSuccessful) {
                val data = convertJson<Array<EstadoCalPendienteDto>>(call.body())
                Result.Correcto(data.map { it.toDomain() })
            } else Result.Error(call.message())
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }

    override suspend fun getEstadoCalPendienteDia(
        ctacli: String,
        anio: String,
        mes: String,
        dia: String
    ): Result<List<EstadoCalPendienteDia>> {
        return try {
            val call = api.getEstadoCalPendienteDia(
                ctacli = ctacli,
                anio = anio,
                mes = mes,
                dia = dia,
                token = token()
            )
            if (call.isSuccessful) {
                val data = convertJson<Array<EstadoCalPendienteDiaDto>>(call.body())
                Result.Correcto(data.map { it.toDomain() })
            } else Result.Error(call.message())
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }


    override suspend fun listarIncumplimientos(ctacli: String): Result<List<Incumplimiento>> {
        return try {
            val call = api.getListaIncumplimientos(
                ctacli = ctacli,
                token = token()
            )
            if (call.isSuccessful) {
                val data = convertJson<Array<IncumplimientoDto>>(call.body()).map { it.toDomain() }
                Result.Correcto(data)
            } else Result.Error(call.message())
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }

    override suspend fun getCorreos(
        ctacli: String,
        dia: String,
        mes: String,
        anio: String
    ): Result<List<CorreoMasivo>> {
        return try {
            val call = api.getCorreos(
                ctacli = ctacli,
                dia = dia,
                mes = mes,
                anio = anio,
                token = token()
            )
            if (call.isSuccessful) {
                val data = convertJson<Array<CorreoDto>>(call.body())
                Result.Correcto(data.map { it.toDomain() })
            } else Result.Error(call.message())
        } catch (e: Exception) {
            Result.Error(e.message)
        }
    }
}