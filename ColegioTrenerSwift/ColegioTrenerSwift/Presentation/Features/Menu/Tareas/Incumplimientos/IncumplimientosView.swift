//
//  IncumplimientosView.swift
//  ColegioTrenerSwift
//
//  Created by Joel on 31/01/24.
//

import SwiftUI

struct IncumplimientosView: View {
    
    @StateObject private var viewModel = IncumplimientosViewModel()
    
    var body: some View {
        VStack(spacing: 0){
            
            SelectHijo (
                hijoSelected: $viewModel.hijoSelected,
                listHijos: viewModel.listHijos,
                click: { ctacli in
                    viewModel.getIncumplimientos()
                }
            )
            
            VStack {
                HStack {
                    Text("Total Acumulado")
                        .foregroundStyle(.white)
                        .padding(.horizontal)
                        .padding(.vertical, 3)
                        .background(.colorS1)
                    Spacer()
                    Text("\(viewModel.totalAcumulado)")
                        .fontWeight(.bold)
                    Spacer()
                    Text("Trimestre")
                    Spacer()
                    Text(viewModel.trimestre.num())
                        .padding(.trailing)
                }
                .bold()
                .background(.colorS1.opacity(0.2))
                .clipShape(.rect(cornerRadius: 16))
                
                ScrollView {
                    VStack {
                        let list = Dictionary(grouping: viewModel.listIncumplimientos, by: { $0.semana }).values.map{ $0 }
                        ForEach(list, id: \.self) { incumplimientos in
                            CardIncumplimiento(incumplimientos: incumplimientos)
                        }
                    }
                    .frame(maxHeight: .infinity)
                }
                
            }
            .frame(maxHeight: .infinity)
            .padding(5)
        }
        .background(.white)
    }
}

#Preview {
    IncumplimientosView()
}
