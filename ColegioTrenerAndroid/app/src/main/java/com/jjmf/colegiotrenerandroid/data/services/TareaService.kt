package com.jjmf.colegiotrenerandroid.data.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TareaService {
    @GET("PublicacionFox/TrenerWCFOX.svc/Trener/getTareasByMonth/{ctacli},{anoaca},{mes}")
    suspend fun getEstadosCalPendiente(
        @Path("ctacli") ctacli: String,
        @Path("anoaca") anio: String,
        @Path("mes") mes: String,
        @Header("Authorization") token: String
    ): Response<String>

    @GET("PublicacionFox/TrenerWCFOX.svc/Trener/getTareasByDia/{ctacli},{anoaca},{mes},{dia}")
    suspend fun getEstadoCalPendienteDia(
        @Path("ctacli") ctacli: String,
        @Path("anoaca") anio: String,
        @Path("mes") mes: String,
        @Path("dia") dia: String,
        @Header("Authorization") token: String
    ): Response<String>

    @GET("PublicacionFox/TrenerWCFOX.svc/Trener/getInfoIncumplimiento/{ctacli}")
    suspend fun getListaIncumplimientos(
        @Path("ctacli") ctacli: String,
        @Header("Authorization") token: String
    ): Response<String>

    @GET("PublicacionFox/TrenerWCFOX.svc/Trener/getCorreoMasivoPorAlumno/{ctacli},{dia},{mes},{anio}")
    suspend fun getCorreos(
        @Path("ctacli") ctacli: String,
        @Path("dia") dia: String,
        @Path("mes") mes: String,
        @Path("anio") anio: String,
        @Header("Authorization") token: String
    ): Response<String>

}