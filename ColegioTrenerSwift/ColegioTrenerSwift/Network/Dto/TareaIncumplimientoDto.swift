//
//  TareaIncumplimientoDto.swift
//  ColegioTrenerSwift
//
//  Created by Joel Maldonado on 1/05/24.
//

import SwiftUI

struct TareaIncumplimientoDto: Codable {
    let ctacli: String?
    let semana: String?
    let clatarid: Int?
    let destar: String?
    let fectar: String?
    let cumtar: String?
    let abrevactualmod: String?
    let leyenda1: String?
    let fechaini: String?
    let fechafin: String?
    let total: Int?
    
    func toDomain() -> TareaIncumplimiento {
        return TareaIncumplimiento(
            ctacli: ctacli?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
            semana: semana?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
            clatarid: clatarid ?? 0,
            destar: destar?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
            fectar: fectar?.toDate() ?? .now,
            cumtar: cumtar?.toDate() ?? .now,
            abrevactualmod: abrevactualmod?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
            leyenda1: leyenda1?.trimmingCharacters(in: .whitespacesAndNewlines) ?? "",
            fechaini: fechaini?.toDate() ?? .now,
            fechafin: fechafin?.toDate() ?? .now,
            total: total
        )
    }
}
