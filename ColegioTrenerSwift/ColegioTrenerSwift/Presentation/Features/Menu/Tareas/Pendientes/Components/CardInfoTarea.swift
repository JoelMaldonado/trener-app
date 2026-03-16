//
//  CardInfoTarea.swift
//  ColegioTrenerSwift
//
//  Created by Joel Maldonado on 1/05/24.
//

import SwiftUI

extension PendientesView {
    
    @ViewBuilder
    func CardInfoTarea(_ list: [InfoTareaPendiente]) -> some View {
        if let first = list.first {
            VStack(spacing: 0) {
                Text("\(first.fechaasignacion.format(pattern: "dd LLLL - EEEE"))")
                    .font(.callout)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 4)
                    .background(.colorP1)
                    .foregroundStyle(.white)
                ForEach(list, id: \.self) { tarea in
                    ItemInfoTarea(tarea)
                }
            }
            .font(.footnote)
            .background(.white)
            .clipShape(.rect(cornerRadius: 12))
            .shadow(radius: 8)
            .padding()
        }
    }
    
    @ViewBuilder
    func ItemInfoTarea(_ tarea: InfoTareaPendiente) -> some View {
        
        ZStack {
            Text("\(tarea.curso)")
                .font(.callout)
                .fontWeight(.bold)
            Text("\(tarea.estado)")
                .padding(.trailing)
                .frame(maxWidth: .infinity, alignment: .trailing)
        }
        .padding(.vertical, 2)
        .foregroundStyle(.white)
        .background(
            LeyendaPendientesTab.from(name: tarea.estado)?.color() ?? .colorT1
        )
        VStack(alignment: .leading, spacing: 0) {
            HStack {
                Text("Tarea:")
                Text("\(tarea.tarea)")
            }
            HStack {
                Text("Fecha asignación:")
                Text("\(tarea.fechaasignacion.format())")
                Spacer()
                Text("Fecha entrega:")
                Text("\(tarea.fechaentrega.format())")
            }
        }
        .padding(8)
    }
}

#Preview {
    PendientesView()
}
