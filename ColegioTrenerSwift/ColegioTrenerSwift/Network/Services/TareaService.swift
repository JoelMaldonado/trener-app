//
//  TareaService.swift
//  ColegioTrenerSwift
//
//  Created by Joel Maldonado on 24/04/24.
//

import SwiftUI
import Alamofire

class TareaService {
    
    static let shared = TareaService()
    
    func getTareasByMonth(
        ctacli: String,
        anio: String,
        mes: String,
        completion: @escaping (EResult<[FechaTarea]>) -> Void
    ) {
        
        TokenUsecase.shared.getToken { res in
            switch res {
            case .success(let token):
                
                let headers: HTTPHeaders = [
                    "Authorization": token
                ]
                AF.request(
                    "\(Constants.baseURL)/PublicacionFox/TrenerWCFOX.svc/Trener/getTareasByMonth/\(ctacli),\(anio),\(mes)",
                    method: .get,
                    headers: headers
                )
                .responseDecodable(of: String.self) { res in
                    switch res.result {
                    case .success(let success):
                        let res: EResult<[FechaTareaDto]> = success.toData()
                        switch res {
                        case .success(let data):
                            completion(.success(data.map { $0.toDomain() } ))
                        case .failure(let err):
                            completion(.failure(err))
                        }
                    case .failure(let failure):
                        completion(.failure(failure.localizedDescription))
                    }
                }
            case .failure(let err):
                completion(.failure(err))
            }
        }
    }
    
    func getTareasByDay(
        ctacli: String,
        anio: String,
        mes: String,
        dia: String,
        completion: @escaping (EResult<[InfoTareaPendiente]>) -> Void
    ) {
        TokenUsecase.shared.getToken { res in
            switch res {
            case .success(let token):
                let headers: HTTPHeaders = [
                    "Authorization": token
                ]
                AF.request(
                    "\(Constants.baseURL)/PublicacionFox/TrenerWCFOX.svc/Trener/getTareasByDia/\(ctacli),\(anio),\(mes),\(dia)",
                    method: .get,
                    headers: headers
                )
                .responseDecodable(of: String.self) { res in
                    switch res.result {
                    case .success(let success):
                        let res: EResult<[InfoTareaPendienteDto]> = success.toData()
                        switch res {
                        case .success(let data):
                            completion(.success(data.map { $0.toDomain() } ))
                        case .failure(let err):
                            completion(.failure(err))
                        }
                    case .failure(let err):
                        completion(.failure(err.localizedDescription))
                    }
                }
            case .failure(let err):
                completion(.failure(err))
            }
        }
    }
    
    func listarIncumplimientos(
        ctacli: String,
        completion: @escaping (EResult<[TareaIncumplimiento]>) -> Void
    ) {
        TokenUsecase.shared.getToken { res in
            switch res {
            case .success(let token):
                
                let headers: HTTPHeaders = [
                    "Authorization": token
                ]
                
                AF.request(
                    "\(Constants.baseURL)/PublicacionFox/TrenerWCFOX.svc/Trener/getInfoIncumplimiento/\(ctacli)",
                    method: .get,
                    headers: headers
                )
                .responseDecodable(of: String.self) { res in
                    switch res.result {
                    case .success(let success):
                        let res: EResult<[TareaIncumplimientoDto]> = success.toData()
                        switch res {
                        case .success(let data):
                            completion(.success(data.map{ $0.toDomain() }))
                        case .failure(let err):
                            completion(.failure(err))
                        }
                    case .failure(let failure):
                        completion(.failure(failure.localizedDescription))
                    }
                }
            case .failure(let err):
                completion(.failure(err))
            }
        }
    }
    
    func listarCorreosMasivos(
        ctacli: String,
        dia: String,
        mes: String,
        anio: String,
        completion: @escaping (EResult<[CorreoMasivo]>) -> Void
    ) {
        TokenUsecase.shared.getToken { res in
            switch res {
            case .success(let token):
                
                let headers: HTTPHeaders = [
                    "Authorization": token
                ]
                
                AF.request(
                    "\(Constants.baseURL)/PublicacionFox/TrenerWCFOX.svc/Trener/getCorreoMasivoPorAlumno/\(ctacli),\(dia),\(mes),\(anio)",
                    method: .get,
                    headers: headers
                )
                .responseDecodable(of: String.self) { res in
                    switch res.result {
                    case .success(let success):
                        let result: EResult<[CorreoMasivoDto]> = success.toData()
                        switch result {
                        case .success(let data):
                            completion(.success(data.map { $0.toDomain() }))
                        case .failure(let err):
                            completion(.failure(err))
                        }
                    case .failure(let failure):
                        completion(.failure(failure.localizedDescription))
                    }
                }
            case .failure(let err):
                completion(.failure(err))
            }
        }
    }
    
}

struct CorreoMasivoDto: Codable {
    let profesor: String?
    let codusu: String?
    let codgra: String?
    let codremite: String?
    let asunto: String?
    let cuerpo: String?
    let rutafile: String?
    let fecenvio: String?
    let horpro: String?
    let urlpdf: String?
    let rutapdf: String?
    
    func toDomain() -> CorreoMasivo {
        let locale = Locale(identifier: "es_ES")
        let dateFormatter = DateFormatter()
        dateFormatter.locale = locale
        dateFormatter.timeZone = TimeZone(identifier: "America/Lima")
        dateFormatter.dateFormat = "dd-MM-yyyy"
        
        let cleanedDate = fecenvio?.trim() ?? ""
        let fecha = dateFormatter.date(from: cleanedDate) ?? Date()
        
        let mensajeLimpio = cuerpo?
            .replacingOccurrences(of: "\r\n", with: "\n")
            .trim() ?? ""
        
        let adjuntoBase64 = rutapdf?
            .trimmingCharacters(in: .whitespacesAndNewlines)
            .components(separatedBy: CharacterSet.newlines)
            .compactMap { valor -> String? in
                let limpio = valor.trimmingCharacters(in: .whitespacesAndNewlines)
                if limpio.isEmpty { return nil }
                if limpio.compare("null", options: .caseInsensitive) == .orderedSame { return nil }
                if limpio.lowercased().hasPrefix("error:") { return nil }
                return limpio
            }
            .first
        
        let adjuntoNombre: String? = adjuntoBase64.flatMap { _ in
            let rutaNormalizada = rutafile?
                .trimmingCharacters(in: .whitespacesAndNewlines)
                .replacingOccurrences(of: "\\", with: "/")
            let nombreDesdeFakePath = rutaNormalizada?
                .components(separatedBy: "fakepath/")
                .last?
                .trimmingCharacters(in: .whitespacesAndNewlines)
            let nombreDesdeRuta = nombreDesdeFakePath?.isEmpty == false ? nombreDesdeFakePath : rutaNormalizada?
                .components(separatedBy: "/")
                .last?
                .trimmingCharacters(in: .whitespacesAndNewlines)
            return (nombreDesdeRuta?.isEmpty == false ? nombreDesdeRuta : nil) ?? "Documento_adjunto.pdf"
        }
        
        let remitenteLimpio = profesor?.trim()
            ?? codremite?.trim()
            ?? codusu?.trim()
            ?? "Desconocido"
        
        let asuntoLimpio = asunto?.trim() ?? "Sin asunto"
        let horaLimpia = horpro?.trim() ?? ""
        
        return CorreoMasivo(
            remitente: remitenteLimpio,
            asunto: asuntoLimpio.isEmpty ? "Sin asunto" : asuntoLimpio,
            mensaje: mensajeLimpio,
            fecha: fecha,
            hora: horaLimpia,
            adjuntoUrl: nil,
            adjuntoNombre: adjuntoNombre,
            adjuntoBase64: adjuntoBase64,
            leido: false
        )
    }
}

struct CorreoMasivo: Identifiable, Hashable {
    let id = UUID()
    let remitente: String
    let asunto: String
    let mensaje: String
    let fecha: Date
    let hora: String
    let adjuntoUrl: String?
    let adjuntoNombre: String?
    let adjuntoBase64: String?
    let leido: Bool
}

struct InfoTareaPendienteDto: Codable {
    let fecpro: String?
    let curso: String?
    let tarea: String?
    let estado: String?
    let fechaasignacion: String?
    let fechaentrega: String?

    func toDomain() -> InfoTareaPendiente {
        InfoTareaPendiente(
            fecpro: fecpro?.toDate() ?? .now,
            curso: curso?.trim() ?? "",
            tarea: tarea?.trim() ?? "",
            estado: estado?.trim() ?? "",
            fechaasignacion: fechaasignacion?.toDate() ?? .now,
            fechaentrega: fechaentrega?.toDate() ?? .now
        )
    }
}

struct InfoTareaPendiente: Hashable {
    let fecpro: Date
    let curso: String
    let tarea: String
    let estado: String
    let fechaasignacion: Date
    let fechaentrega: Date
}
