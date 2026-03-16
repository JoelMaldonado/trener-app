//
//  CorreoView.swift
//  ColegioTrenerSwift
//
//  Created by Jota on 10/11/25.
//

import SwiftUI
import QuickLook

struct CorreoView: View {
    
    @StateObject private var viewModel = CorreoViewModel()
    
    var body: some View {
        VStack(spacing: 12) {
            SelectHijo(
                hijoSelected: $viewModel.hijoSelected,
                listHijos: viewModel.listHijos,
                click: viewModel.seleccionarHijo(ctacli:)
            )
            .padding(.bottom, 4)
            
            FilterBar(
                meses: viewModel.meses,
                dias: viewModel.dias,
                selectedMes: viewModel.selectedMes,
                selectedDia: viewModel.selectedDia,
                searchText: viewModel.searchText,
                isSearchEnabled: viewModel.isSearchEnabled,
                onMesSelected: viewModel.onMesSelected,
                onDiaSelected: viewModel.onDiaSelected,
                onSearchChanged: viewModel.onSearchChanged
            )
            
            content
        }
        .padding(.top, 0)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .background(Color.white.ignoresSafeArea())
        .sheet(item: $viewModel.selectedCorreo) { correo in
            CorreoDetalleSheet(
                correo: correo,
                cerrar: viewModel.cerrarDetalle
            )
            .presentationDetents([.fraction(0.9)])
        }
    }
    
    private var content: some View {
        Group {
            if viewModel.isLoading {
                VStack(spacing: 12) {
                    ProgressView()
                    Text("Cargando correos...")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(.colorTexto)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
            } else if let message = viewModel.errorMessage {
                VStack(spacing: 16) {
                    Text(message)
                        .multilineTextAlignment(.center)
                        .font(.system(size: 15, weight: .medium))
                        .foregroundStyle(.colorTexto)
                    Button(action: viewModel.retry) {
                        Text("Reintentar")
                            .font(.system(size: 15, weight: .semibold))
                            .padding(.horizontal, 20)
                            .padding(.vertical, 10)
                            .background(Color.colorS1)
                            .foregroundStyle(Color.white)
                            .clipShape(Capsule())
                    }
                }
                .padding(.horizontal, 24)
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
            } else if viewModel.filteredCorreos.isEmpty {
                VStack(spacing: 12) {
                    Image(systemName: "tray")
                        .font(.system(size: 34))
                        .foregroundStyle(Color(UIColor.systemGray3))
                    Text("No hay correos para mostrar")
                        .font(.system(size: 15, weight: .medium))
                        .foregroundStyle(Color(UIColor.systemGray3))
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, 32)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
            } else {
                ScrollView {
                    LazyVStack(spacing: 12) {
                        ForEach(viewModel.filteredCorreos) { correo in
                            CorreoCard(
                                correo: correo,
                                abrirDetalle: { viewModel.abrirDetalle(correo: correo) }
                            )
                        }
                    }
                    .padding(.horizontal, 12)
                    .padding(.bottom, 12)
                }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
    }
}

// MARK: - Filter Bar

private struct FilterBar: View {
    let meses: [String]
    let dias: [String]
    let selectedMes: String
    let selectedDia: String
    let searchText: String
    let isSearchEnabled: Bool
    let onMesSelected: (String) -> Void
    let onDiaSelected: (String) -> Void
    let onSearchChanged: (String) -> Void
    
    var body: some View {
        HStack(spacing: 4) {
            Menu {
                ForEach(meses, id: \.self) { mes in
                    Button(mes) {
                        onMesSelected(mes)
                    }
                }
            } label: {
                filterBox(text: selectedMes)
            }
            
            Menu {
                ForEach(dias, id: \.self) { dia in
                    Button(dia) {
                        onDiaSelected(dia)
                    }
                }
            } label: {
                filterBox(text: selectedDia, centered: true)
                    .frame(width: 100)
            }
            
            SearchField(
                text: searchText,
                isEnabled: isSearchEnabled,
                onChange: onSearchChanged
            )
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 4)
    }
    
    private func filterBox(text: String, centered: Bool = false) -> some View {
        HStack {
            Text(text)
                .font(.system(size: 14, weight: .medium))
                .foregroundStyle(.black)
                .frame(maxWidth: .infinity, alignment: centered ? .center : .leading)
            Image(systemName: "chevron.down")
                .foregroundStyle(.colorP1)
        }
        .padding(.horizontal, 12)
        .frame(height: 44)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
    }
}

private struct SearchField: View {
    let text: String
    let isEnabled: Bool
    let onChange: (String) -> Void
    
    var body: some View {
        HStack {
            TextField(
                "Buscar remitente",
                text: Binding(
                    get: { text },
                    set: { value in onChange(value) }
                )
            )
            .font(.system(size: 12, weight: .medium))
            .foregroundStyle(isEnabled ? Color.black : Color(UIColor.systemGray3))
            .disabled(!isEnabled)
            
            Image(systemName: "magnifyingglass")
                .foregroundStyle(.colorP1)
                .opacity(isEnabled ? 1 : 0.4)
        }
        .padding(.horizontal, 12)
        .frame(height: 44)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
        .opacity(isEnabled ? 1 : 0.6)
    }
}

// MARK: - Card

private struct CorreoCard: View {
    let correo: CorreoMasivo
    let abrirDetalle: () -> Void
    
    private static let monthFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "es_ES")
        formatter.dateFormat = "dd MMM"
        return formatter
    }()
    
    private let indicatorRead = Color.gray.opacity(0.4)
    private let indicatorUnread = Color(red: 0.65, green: 0.21, blue: 0.21)
    private let buttonColor: Color = .colorS1
    
    var body: some View {
        ZStack(alignment: .trailing) {
            RoundedRectangle(cornerRadius: 18)
                .fill(Color.white)
                .shadow(color: .black.opacity(0.08), radius: 6, x: 0, y: 4)
            
            VStack(alignment: .leading, spacing: 12) {
                HStack {
                    HStack(spacing: 8) {
                        Image(systemName: "person.fill")
                            .foregroundStyle(buttonColor)
                        Text(correo.remitente)
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundStyle(.colorTexto)
                            .lineLimit(1)
                    }
                    Spacer()
                    HStack(spacing: 12) {
                        HStack(spacing: 4) {
                            Image(systemName: "calendar")
                                .foregroundStyle(buttonColor)
                            Text(Self.monthFormatter.string(from: correo.fecha).uppercased())
                                .font(.system(size: 12, weight: .semibold))
                                .foregroundStyle(.colorTexto)
                        }
                        HStack(spacing: 4) {
                            Image(systemName: "clock")
                                .foregroundStyle(buttonColor)
                            Text(correo.hora)
                                .font(.system(size: 12, weight: .semibold))
                                .foregroundStyle(.colorTexto)
                        }
                    }
                }
                
                HStack(alignment: .center, spacing: 12) {
                    HStack(spacing: 8) {
                        Image(systemName: "envelope")
                            .foregroundStyle(buttonColor)
                        Text(correo.asunto)
                            .font(.system(size: 14))
                            .foregroundStyle(.colorTexto)
                            .lineLimit(2)
                    }
                    Spacer(minLength: 12)
                    Button(action: abrirDetalle) {
                        Text("Ver")
                            .font(.system(size: 13, weight: .semibold))
                            .padding(.horizontal, 18)
                            .padding(.vertical, 6)
                            .background(buttonColor)
                            .foregroundStyle(Color.white)
                            .clipShape(Capsule())
                    }
                }
            }
            .padding(16)
            
            RoundedRectangle(cornerRadius: 2)
                .fill(correo.leido ? indicatorRead : indicatorUnread)
                .frame(width: 4)
                .padding(.vertical, 10)
        }
    }
}

// MARK: - Detail Sheet

private struct CorreoDetalleSheet: View {
    let correo: CorreoMasivo
    let cerrar: () -> Void
    
    private let buttonColor: Color = .colorS1
    @State private var showAttachmentError = false
    @State private var previewURL: URL?
    
    var body: some View {
        NavigationStack {
            VStack(alignment: .leading, spacing: 16) {
                // Fecha y Hora fijos arriba
                HStack {
                    Text("Fecha: \(correo.fecha.format(pattern: "dd MMMM yyyy"))")
                    Spacer()
                    Text("Hora: \(correo.hora)")
                }
                .font(.system(size: 15, weight: .semibold))
                .foregroundStyle(.colorTexto)
                
                // Asunto fijo arriba
                SectionBlock(title: "Asunto", value: correo.asunto)
                
                // De fijo arriba
                SectionBlock(title: "De", value: correo.remitente)
                
                // Mensaje scrolleable
                VStack(alignment: .leading, spacing: 6) {
                    Text("Mensaje:")
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundStyle(.colorTexto)
                    ScrollView {
                        let mensajeTexto = correo.mensaje.isEmpty ? "Sin mensaje" : correo.mensaje
                        Text(attributedStringWithLinks(from: mensajeTexto))
                            .font(.system(size: 14))
                            .foregroundStyle(.colorTexto)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .multilineTextAlignment(.leading)
                            .fixedSize(horizontal: false, vertical: true)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 12)
                            .background(
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(Color(uiColor: .systemGray6))
                            )
                    }
                    .frame(maxHeight: 300)
                }
                
                // Adjunto fijo abajo (si existe)
                if let adjuntoBase64 = correo.adjuntoBase64,
                   !adjuntoBase64.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty,
                   !adjuntoBase64.lowercased().hasPrefix("error:"),
                   isValidBase64(adjuntoBase64) {
                    let nombreAdjunto = (correo.adjuntoNombre?.isEmpty == false ? correo.adjuntoNombre : nil) ?? "Documento_adjunto.pdf"
                    VStack(alignment: .leading, spacing: 6) {
                        Text("Adjunto:")
                            .font(.system(size: 15, weight: .semibold))
                            .foregroundStyle(.colorTexto)
                        HStack {
                            Text(nombreAdjunto)
                                .font(.system(size: 14, weight: .medium))
                                .foregroundStyle(.colorTexto)
                                .multilineTextAlignment(.leading)
                                .lineLimit(nil)
                                .fixedSize(horizontal: false, vertical: true)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            Button {
                                abrirAdjunto(base64: adjuntoBase64, fileName: nombreAdjunto)
                            } label: {
                                Image(systemName: "eye")
                                    .foregroundStyle(buttonColor)
                            }
                        }
                        .padding(.horizontal, 12)
                        .padding(.vertical, 12)
                        .background(
                            RoundedRectangle(cornerRadius: 12)
                                .fill(Color(uiColor: .systemGray6))
                        )
                    }
                }
            }
            .padding(16)
            .navigationTitle("Detalle del correo")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("Cerrar", action: cerrar)
                        .font(.system(size: 15, weight: .semibold))
                        .tint(buttonColor)
                }
            }
        }
        .presentationDragIndicator(.visible)
        .presentationDetents([.fraction(1.0)])
        .quickLookPreview($previewURL)
        .alert("No se pudo abrir el adjunto", isPresented: $showAttachmentError) {
            Button("Aceptar", role: .cancel) {}
        }
    }
    
    private func abrirAdjunto(base64: String, fileName: String) {
        guard let data = Data(base64Encoded: base64, options: .ignoreUnknownCharacters) else {
            showAttachmentError = true
            return
        }
        let nombreDepurado = fileName.isEmpty ? "Documento_adjunto.pdf" : fileName
        let seguro = nombreDepurado.replacingOccurrences(of: "[^A-Za-z0-9._-]", with: "_", options: .regularExpression)
        let url = FileManager.default.temporaryDirectory.appendingPathComponent(seguro.isEmpty ? "adjunto_correo.pdf" : seguro)
        do {
            try data.write(to: url, options: .atomic)
            previewURL = url
        } catch {
            showAttachmentError = true
        }
    }
    
    @ViewBuilder
    private func SectionBlock(title: String, value: String) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("\(title):")
                .font(.system(size: 15, weight: .semibold))
                .foregroundStyle(.colorTexto)
            Text(value)
                .font(.system(size: 14))
                .foregroundStyle(.colorTexto)
                .frame(maxWidth: .infinity, alignment: .leading)
                .multilineTextAlignment(.leading)
                .fixedSize(horizontal: false, vertical: true)
                .padding(.horizontal, 12)
                .padding(.vertical, 12)
                .background(
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color(uiColor: .systemGray6))
                )
        }
    }
    
    private func attributedStringWithLinks(from text: String) -> AttributedString {
        var attributedString = AttributedString(text)
        
        // Patrón para detectar URLs
        let urlPattern = #"(?:(?:https?|ftp)://|www\.)[\w\-]+(?:\.[\w\-]+)+([\w\-.,@?^=%&:/~+#]*[\w\-@?^=%&/~+#])?"#
        
        do {
            let regex = try NSRegularExpression(pattern: urlPattern, options: .caseInsensitive)
            let nsString = text as NSString
            let matches = regex.matches(in: text, options: [], range: NSRange(location: 0, length: nsString.length))
            
            // Procesar matches en orden inverso para mantener los índices correctos
            for match in matches.reversed() {
                if let range = Range(match.range, in: text) {
                    let urlString = String(text[range])
                    let fullUrl: String
                    
                    // Agregar https:// si no tiene protocolo
                    if !urlString.hasPrefix("http://") && !urlString.hasPrefix("https://") && !urlString.hasPrefix("ftp://") {
                        fullUrl = "https://\(urlString)"
                    } else {
                        fullUrl = urlString
                    }
                    
                    if let url = URL(string: fullUrl) {
                        if let attributedRange = Range(range, in: attributedString) {
                            // Aplicar estilo de link
                            attributedString[attributedRange].link = url
                            attributedString[attributedRange].foregroundColor = .blue
                            attributedString[attributedRange].underlineStyle = .single
                        }
                    }
                }
            }
        } catch {
            // Si hay error en el regex, retornar el texto sin modificar
            return AttributedString(text)
        }
        
        return attributedString
    }
    
    private func isValidBase64(_ base64String: String) -> Bool {
        guard let data = Data(base64Encoded: base64String, options: .ignoreUnknownCharacters) else {
            return false
        }
        return !data.isEmpty
    }
}

// MARK: - ViewModel

private final class CorreoViewModel: ObservableObject {
    
    @Published var listHijos: [HijoTrener] = []
    @Published var hijoSelected: HijoTrener?
    
    @Published private(set) var meses: [String]
    @Published private(set) var dias: [String] = []
    
    @Published var selectedMes: String
    @Published var selectedDia: String
    @Published var searchText: String = ""
    
    @Published private(set) var filteredCorreos: [CorreoMasivo] = []
    @Published var isLoading = false
    @Published var errorMessage: String?
    @Published var isSearchEnabled = false
    
    @Published var selectedCorreo: CorreoMasivo?
    
    private let todosLabel = "Todos"
    private let calendar = Calendar(identifier: .gregorian)
    private let locale = Locale(identifier: "es_ES")
    private let currentYear: Int
    private let monthNames: [String]
    private let monthMap: [String: Int]
    
    private var allCorreos: [CorreoMasivo] = []
    
    init() {
        let now = Date()
        self.currentYear = calendar.component(.year, from: now)
        
        let formatter = DateFormatter()
        formatter.locale = locale
        self.monthNames = formatter.monthSymbols.map { $0.capitalized }
        self.monthMap = Dictionary(uniqueKeysWithValues: monthNames.enumerated().map { ($1, $0 + 1) })
        self.meses = [todosLabel] + monthNames
        
        let monthIndex = calendar.component(.month, from: now)
        let day = calendar.component(.day, from: now)
        self.selectedMes = monthNames[monthIndex - 1]
        self.selectedDia = String(format: "%02d", day)
        
        updateDiasFor(month: selectedMes, resetSelection: false, referenceDate: now)
        listarHijos()
    }
    
    func seleccionarHijo(ctacli: String) {
        guard let hijo = listHijos.first(where: { $0.ctacli == ctacli }) else { return }
        hijoSelected = hijo
        resetFiltersToToday()
        searchText = ""
        filteredCorreos = []
        isSearchEnabled = false
        fetchCorreos()
    }
    
    func onMesSelected(_ mes: String) {
        guard selectedMes != mes else { return }
        selectedMes = mes
        updateDiasFor(month: mes, resetSelection: true)
        fetchCorreos()
    }
    
    func onDiaSelected(_ dia: String) {
        guard selectedDia != dia else { return }
        selectedDia = dia
        fetchCorreos()
    }
    
    func onSearchChanged(_ value: String) {
        if !isSearchEnabled && !value.isEmpty { return }
        if searchText == value { return }
        searchText = value
        if value.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
            fetchCorreos()
        } else {
            filteredCorreos = applySearch(value)
        }
    }
    
    func abrirDetalle(correo: CorreoMasivo) {
        selectedCorreo = correo
    }
    
    func cerrarDetalle() {
        selectedCorreo = nil
    }
    
    func retry() {
        if hijoSelected == nil {
            listarHijos()
        } else {
            fetchCorreos()
        }
    }
    
    // MARK: - Private helpers
    
    private func listarHijos() {
        isLoading = true
        DatosService.shared.getHijosTrener { [weak self] res in
            DispatchQueue.main.async {
                guard let self else { return }
                switch res {
                case .success(let data):
                    self.listHijos = data
                    if let first = data.first {
                        self.hijoSelected = first
                        self.resetFiltersToToday()
                        self.fetchCorreos()
                    } else {
                        self.isLoading = false
                        self.errorMessage = "No se encontró información del alumno."
                    }
                case .failure(let error):
                    self.isLoading = false
                    self.errorMessage = error
                    self.filteredCorreos = []
                    self.allCorreos = []
                    self.isSearchEnabled = false
                }
            }
        }
    }
    
    private func fetchCorreos() {
        guard let ctacli = hijoSelected?.ctacli else {
            isLoading = false
            return
        }
        
        isLoading = true
        errorMessage = nil
        TareaService.shared.listarCorreosMasivos(
            ctacli: ctacli,
            dia: dayParam(),
            mes: monthParam(),
            anio: "\(currentYear)"
        ) { [weak self] res in
            DispatchQueue.main.async {
                guard let self else { return }
                self.isLoading = false
                switch res {
                case .success(let correos):
                    self.allCorreos = correos
                    self.isSearchEnabled = !correos.isEmpty
                    self.filteredCorreos = self.applySearch(self.searchText)
                    if correos.isEmpty {
                        self.searchText = ""
                    }
                case .failure(let error):
                    self.errorMessage = error
                    self.filteredCorreos = []
                    self.allCorreos = []
                    self.isSearchEnabled = false
                }
            }
        }
    }
    
    private func updateDiasFor(month: String, resetSelection: Bool, referenceDate: Date = Date()) {
        if month == todosLabel {
            dias = [todosLabel]
            selectedDia = todosLabel
            return
        }
        
        guard let monthNumber = monthMap[month] else { return }
        
        var components = DateComponents()
        components.year = currentYear
        components.month = monthNumber
        components.day = 1
        
        let date = calendar.date(from: components) ?? referenceDate
        let range = calendar.range(of: .day, in: .month, for: date) ?? (1..<31)
        let monthDays = range.map { String(format: "%02d", $0) }
        let newDias = [todosLabel] + monthDays
        dias = newDias
        
        let todayDay = String(format: "%02d", calendar.component(.day, from: referenceDate))
        let firstDay = monthDays.first ?? todosLabel
        
        if resetSelection {
            let referenceMonth = calendar.component(.month, from: referenceDate)
            if monthNumber == referenceMonth, newDias.contains(todayDay) {
                selectedDia = todayDay
            } else if selectedDia == todosLabel {
                selectedDia = todosLabel
            } else {
                selectedDia = firstDay
            }
        } else if selectedDia == todosLabel {
            selectedDia = todosLabel
        } else if newDias.contains(selectedDia) {
            // keep current selection
        } else {
            selectedDia = firstDay
        }
    }
    
    private func resetFiltersToToday() {
        let now = Date()
        let monthIndex = calendar.component(.month, from: now)
        selectedMes = monthNames[monthIndex - 1]
        updateDiasFor(month: selectedMes, resetSelection: true, referenceDate: now)
    }
    
    private func monthParam() -> String {
        if selectedMes == todosLabel { return "00" }
        guard let monthNumber = monthMap[selectedMes] else { return "00" }
        return String(format: "%02d", monthNumber)
    }
    
    private func dayParam() -> String {
        if selectedMes == todosLabel { return "00" }
        if selectedDia == todosLabel { return "00" }
        return selectedDia
    }
    
    private func applySearch(_ query: String) -> [CorreoMasivo] {
        let trimmed = query.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty else { return allCorreos }
        return allCorreos.filter { correo in
            correo.remitente.range(of: trimmed, options: .caseInsensitive) != nil
        }
    }
}

#Preview {
    CorreoView()
}
