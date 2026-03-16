//
//  ColegioTrenerSwiftApp.swift
//  ColegioTrenerSwift
//
//  Created by Joel on 30/01/24.
//

import SwiftUI

@main
struct ColegioTrenerSwiftApp: App {
    @State private var isSplashActive: Bool = true
    @State private var isSessionActive = false
    
    var body: some Scene {
        WindowGroup {
            ZStack {
                NavigationStack {
                    if self.isSessionActive {
                        MenuView()
                    } else {
                        LoginView()
                    }
                }
                .toolbar(.hidden, for: .navigationBar)
                SplashView(isActive: $isSplashActive)
            }
            .preferredColorScheme(.light)
            .onAppear {
                print(UserDefaults.standard.bool(forKey: Keys.loginRecuerdame))
                self.isSessionActive = UserDefaults.standard.bool(forKey: Keys.loginRecuerdame)
                DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                    withAnimation {
                        self.isSplashActive = false
                    }
                }
            }
        }
    }
}
