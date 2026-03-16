package com.jjmf.colegiotrenerandroid.data.dto

import com.jjmf.colegiotrenerandroid.domain.model.Incumplimiento
import com.jjmf.colegiotrenerandroid.util.toDate
import java.util.Date

data class IncumplimientoDto(
    val ctacli: String?,
    val semana: String?,
    val clatarid: Int?,
    val destar: String?,
    val fectar: String?,
    val cumtar: String?,
    val abrevactualmod: String?,
    val leyenda1: String?,
    val total:Int?
) {
    fun toDomain(): Incumplimiento {
        return Incumplimiento(
            ctacli = ctacli?.trim(),
            semana = semana?.trim(),
            clatarid = clatarid,
            destar = destar?.trim(),
            fectar = fectar?.toDate() ?: Date(),
            cumtar = cumtar?.toDate() ?: Date(),
            abrevactualmod = abrevactualmod?.trim(),
            leyenda1 = leyenda1?.trim(),
            total = total
        )
    }
}