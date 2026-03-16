//
//  FechaTareaDto.swift
//  ColegioTrenerSwift
//
//  Created by Joel Maldonado on 1/05/24.
//

import SwiftUI


enum LeyendaPendientesTab: CaseIterable{
    case NoTarea
    case Pendiente
    case Revisado
    
    func color() -> Color {
        switch self {
        case .NoTarea:
                .colorT1
        case .Pendiente:
                .yellow
        case .Revisado:
                .green
        }
    }
    
    func name() -> String {
        switch self {
        case .NoTarea:
            "No hizo tarea"
        case .Pendiente:
            "Pendiente"
        case .Revisado:
            "Revisado"
        }
    }
    
    static func from(name: String?) -> LeyendaPendientesTab? {
        guard let name = name?.trimmingCharacters(in: .whitespacesAndNewlines).lowercased() else {
            return nil
        }
        
        switch name {
        case "no hizo tarea", "no hizo la tarea":
            return .NoTarea
        case "pendiente":
            return .Pendiente
        case "revisado":
            return .Revisado
        default:
            return nil
        }
    }
}

struct FechaTareaDto: Codable {
    let fechaasignacion: String?
    let estado: String?
    let cantidad: Double?
    
    
    
    func toDomain() -> FechaTarea {
        
        return FechaTarea(
            fechaAsignacion: fechaasignacion?.toDate() ?? .now,
            estado: LeyendaPendientesTab.from(name: self.estado),
            cantidad: Int(self.cantidad ?? 0)
        )
    }
}
