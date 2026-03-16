//
//  AutorizacionesView.swift
//  ColegioTrenerSwift
//
//  Created by Joel on 31/01/24.
//

import SwiftUI

enum EstadoAutorizacionTab : String, CaseIterable {
    case Activos
    case Vencidos
    
    func code() -> String {
        switch self {
        case .Activos:
            return "A"
        case .Vencidos:
            return "V"
        }
    }
}

struct AutorizacionesView: View {
    
    @StateObject var viewModel = AutorizacionesViewModel()
    
    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 12) {
                
                SelectEstado()
                
                SelectAutorizacion()
                
                ForEach(viewModel.listEstado, id: \.self) { estado in
                    CardAutorizacion(
                        estado: estado,
                        estadoTab: viewModel.estado,
                        autorizar: { bool in
                            viewModel.autorizar(estado: bool, ctacli: estado.ctacli)
                        }
                    )
                }
                
            }
            .padding(8)
        }
        .alert(isPresented: $viewModel.isError) {
            Alert(title: Text("Warning"), message: Text(viewModel.error ?? "Sin Definir"))
        }
        .background(.white)
    }
}

#Preview {
    AutorizacionesView()
}
