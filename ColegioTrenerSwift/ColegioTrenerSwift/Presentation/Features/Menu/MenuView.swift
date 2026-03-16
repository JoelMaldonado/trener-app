//
//  MenuView.swift
//  ColegioTrenerSwift
//
//  Created by Joel on 30/01/24.
//

import SwiftUI

struct MenuView: View {
    @State var tab : MenuTab = .Home
    var body: some View {
        VStack(spacing: 0){
            TopView(menuTab: $tab)
            ZStack {
                Image(.fondoMenu)
                    .resizable()
                    .ignoresSafeArea()
                ZStack {
                    switch tab {
                    case .Home:
                        MenuHome(tab: $tab)
                    case .Datos:
                        DatosView()
                    case .Pagos:
                        PagosView()
                    case .Inscripciones:
                        InscripcionesView(
                            back: {
                                self.tab = .Home
                            }
                        )
                    case .DiariaAcumulada:
                        DiariaAcumuladaView()
                    case .Justificacion:
                        JustificacionView()
                    case .Carnet:
                        CarnetView()
                    case .Pendientes:
                        PendientesView()
                    case .Incumplimientos:
                        IncumplimientosView()
                    case .CitaInforme:
                        CitaInformeView()
                    case .Autorizaciones:
                        AutorizacionesView()
                    case .Correos:
                        CorreoView()
                    case .Notificaciones:
                        NotificacionesView()
                    case .Perfil:
                        PerfilView()
                    }
                }
            }
        }
        
    }
    
}

enum MenuTab {
    case Home
    case Datos
    case Pagos
    case Inscripciones
    case DiariaAcumulada
    case Justificacion
    case Carnet
    case Pendientes
    case Incumplimientos
    case CitaInforme
    case Autorizaciones
    case Correos
    case Notificaciones
    case Perfil
    
    func name()-> String {
        switch self {
        case .Home:
            return "Inicio"
        case .Datos:
            return "Datos"
        case .Pagos:
            return "Pagos"
        case .Inscripciones:
            return "Registros"
        case .DiariaAcumulada:
            return "Diaria Acumulada"
        case .Justificacion:
            return "Justificación"
        case .Carnet:
            return "Carnet Recojo"
        case .Pendientes:
            return "Pendientes"
        case .Incumplimientos:
            return "Incumplimientos"
        case .CitaInforme:
            return "Cita/Informe"
        case .Autorizaciones:
            return "Autorizaciones"
        case .Correos:
            return "Correos"
        case .Notificaciones:
            return "Notificaciones"
        case .Perfil:
            return "Cambios datos generales"
        }
    }
}


struct ItemMenuView : View {
    var label : String
    var body: some View {
        VStack(alignment: .leading, spacing: 0){
            Text(label)
                .font(.system(size: 18, weight: .semibold))
            Rectangle()
                .frame(height: 2)
                .padding(.bottom, 15)
        }
        .foregroundStyle(.white)
    }
}
