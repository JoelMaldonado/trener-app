//
//  IncumplimientosViewModel.swift
//  ColegioTrenerSwift
//
//  Created by Joel on 31/01/24.
//

import Foundation
import SVProgressHUD

class IncumplimientosViewModel : ObservableObject {
    
    @Published var hijoSelected: HijoTrener?
    @Published var listHijos: [HijoTrener] = []
    
    @Published var trimestre: TrimestreTab = .Uno
    @Published var listIncumplimientos: [TareaIncumplimiento] = []
    @Published var totalAcumulado: Int = 0
    
    @Published var isError = false
    @Published var error: String?
    
    init() {
        self.listarHijos()
        self.getTrimestreActual()
    }
    
    func listarHijos() {
        DatosService.shared.getHijosTrener { res in
            switch res {
            case .success(let data):
                self.listHijos = data
                self.hijoSelected = data.first
                self.getIncumplimientos()
            case .failure(let err):
                self.error = err
                self.isError = true
            }
        }
    }
    
    func getTrimestreActual() {
        InformeService.shared.getTrimestre { res in
            switch res {
            case .success(let data):
                self.trimestre = data
            case .failure(let err):
                self.error = err
                self.isError = true
            }
        }
    }
    
    func getIncumplimientos() {
        if let ctacli = hijoSelected?.ctacli {
            SVProgressHUD.show()
            TareaService.shared.listarIncumplimientos(ctacli: ctacli) { res in
                switch res {
                case .success(let data):
                    self.listIncumplimientos = data
                    self.totalAcumulado = data.first(where: { $0.total != nil })?.total ?? 0
                    SVProgressHUD.dismiss()
                case .failure(let err):
                    self.error = err
                    self.isError = true
                    self.listIncumplimientos = []
                    self.totalAcumulado = 0
                    SVProgressHUD.dismiss()
                }
            }
        }
    }
    
}
