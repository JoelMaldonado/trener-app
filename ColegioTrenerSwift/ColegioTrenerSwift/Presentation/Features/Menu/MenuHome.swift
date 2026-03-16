//
//  MenuHome.swift
//  ColegioTrenerSwift
//
//  Created by Joel Maldonado on 23/04/24.
//

import SwiftUI

struct MenuHome: View {
    @Binding var tab: MenuTab
    var body: some View {
        ScrollView {
            VStack(spacing: 8) {
                
                // Administrativos
                VStack(alignment: .leading, spacing: 8) {
                    ItemMenuView(label: "Administrativos")
                    
                    HStack(spacing: 8) {
                        MenuItemButton(
                            text: "Datos",
                            imageName: "ic_datos_actualizado",
                            action: { self.tab = .Datos }
                        )
                        
                        MenuItemButton(
                            text: "Pagos",
                            imageName: "ic_pagos_actualizado",
                            action: { self.tab = .Pagos }
                        )
                        
                        MenuItemButton(
                            text: "Registros",
                            imageName: "ic_inscripciones_actualizado",
                            action: { self.tab = .Inscripciones }
                        )
                    }
                    .frame(maxWidth: .infinity)
                    
                    HStack(spacing: 8) {
                        MenuItemButton(
                            text: "Correos",
                            imageName: "ic_correos_actualizado",
                            action: { self.tab = .Correos }
                        )
                        
                        Spacer()
                            .frame(maxWidth: .infinity)
                        Spacer()
                            .frame(maxWidth: .infinity)
                    }
                    .frame(maxWidth: .infinity)
                }
                
                Spacer()
                    .frame(height: 12)
                
                // Asistencia
                VStack(alignment: .leading, spacing: 8) {
                    ItemMenuView(label: "Asistencias")
                    
                    HStack(spacing: 8) {
                        MenuItemButton(
                            text: "Diarias",
                            imageName: "ic_diaria_acumulada_actualizado",
                            action: { self.tab = .DiariaAcumulada }
                        )
                        
                        MenuItemButton(
                            text: "Justificaciones",
                            imageName: "ic_justificacion_actualizado",
                            action: { self.tab = .Justificacion }
                        )
                        
                        MenuItemButton(
                            text: "Carnets",
                            imageName: "ic_carnet_actualizado",
                            action: { self.tab = .Carnet }
                        )
                    }
                    .frame(maxWidth: .infinity)
                }
                
                Spacer()
                    .frame(height: 12)
                
                // Tareas
                VStack(alignment: .leading, spacing: 8) {
                    ItemMenuView(label: "Tareas")
                    
                    HStack(spacing: 8) {
                        MenuItemButton(
                            text: "Pendientes",
                            imageName: "ic_pendientes_actualizado",
                            action: { self.tab = .Pendientes }
                        )
                        
                        MenuItemButton(
                            text: "Incumplimientos",
                            imageName: "ic_incumplimientos_actualizado",
                            action: { self.tab = .Incumplimientos }
                        )
                        
                        Spacer()
                            .frame(maxWidth: .infinity)
                    }
                    .frame(maxWidth: .infinity)
                }
                
                Spacer()
                    .frame(height: 12)
                
                // Resultados académicos
                VStack(alignment: .leading, spacing: 8) {
                    ItemMenuView(label: "Resultados académicos")
                    
                    HStack(spacing: 8) {
                        MenuItemButton(
                            text: "Citas/informes",
                            imageName: "ic_cita_informe_actualizado",
                            action: { self.tab = .CitaInforme }
                        )
                        
                        Spacer()
                            .frame(maxWidth: .infinity)
                        Spacer()
                            .frame(maxWidth: .infinity)
                    }
                    .frame(maxWidth: .infinity)
                }
                
                Spacer()
                    .frame(height: 12)
                
                // Autorizaciones
                VStack(alignment: .leading, spacing: 8) {
                    ItemMenuView(label: "Autorizaciones")
                    
                    HStack(spacing: 8) {
                        MenuItemButton(
                            text: "Autorizaciones",
                            imageName: "ic_autorizaciones_actualizado",
                            action: { self.tab = .Autorizaciones }
                        )
                        
                        Spacer()
                            .frame(maxWidth: .infinity)
                        Spacer()
                            .frame(maxWidth: .infinity)
                    }
                    .frame(maxWidth: .infinity)
                }
                
                Spacer()
                
                // Botón Intranet
                Button {
                    if let link = UserDefaults.standard.string(forKey: Keys.loginIntranet) {
                        if let url = URL(string: link) {
                            UIApplication.shared.open(url)
                        }
                    }
                } label: {
                    HStack(spacing: 12) {
                        Image(systemName: "globe")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 30, height: 30)
                        Text("Intranet Web")
                            .font(.system(size: 16, weight: .bold))
                    }
                    .foregroundStyle(.white)
                }
            }
            .padding(16)
        }
    }
}

// Componente para items verticales (3 columnas)
struct MenuItemButton: View {
    let text: String
    let imageName: String
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            VStack(spacing: 0) {
                Image(imageName)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 50, height: 50)
                
                Text(text)
                    .font(.system(size: 14, weight: .medium))
                    .foregroundStyle(.white)
                    .multilineTextAlignment(.center)
                    .lineLimit(1)
                    .minimumScaleFactor(0.8)
            }
            .frame(maxWidth: .infinity, minHeight: 90, alignment: .center)
        }
    }
}

// Componente para items horizontales (ancho completo)
struct MenuItemHorizontalButton: View {
    let text: String
    let imageName: String
    let action: () -> Void
    
    var body: some View {
        Button(action: action) {
            HStack(spacing: 5) {
                Image(imageName)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 50, height: 50)
                
                Text(text)
                    .font(.system(size: 14, weight: .medium))
                    .foregroundStyle(.white)
                    .multilineTextAlignment(.leading)
                    .lineLimit(1)
                
                Spacer()
            }
        }
    }
}
