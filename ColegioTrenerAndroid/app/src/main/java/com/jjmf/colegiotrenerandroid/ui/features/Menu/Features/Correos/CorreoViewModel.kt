package com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Correos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjmf.colegiotrenerandroid.core.Result
import com.jjmf.colegiotrenerandroid.domain.model.CorreoMasivo
import com.jjmf.colegiotrenerandroid.domain.repository.TareaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val TODOS = "Todos"

@HiltViewModel
class CorreoViewModel @Inject constructor(
    private val repository: TareaRepository
) : ViewModel() {

    private val localeEs = Locale("es", "ES")
    private val currentDate = LocalDate.now()
    private val dayFormatter = DateTimeFormatter.ofPattern("dd")

    private val monthNames = Month.values().map {
        it.getDisplayName(TextStyle.FULL, localeEs).replaceFirstChar { ch ->
            if (ch.isLowerCase()) ch.titlecase(localeEs) else ch.toString()
        }
    }
    private val monthMap = monthNames.withIndex().associate { it.value to it.index + 1 }

    val meses: List<String> = listOf(TODOS) + monthNames
    var dias by mutableStateOf<List<String>>(emptyList())
        private set

    var selectedMes by mutableStateOf(monthName(currentDate.monthValue))
        private set
    var selectedDia by mutableStateOf(dayFormatter.format(currentDate))
        private set
    var searchQuery by mutableStateOf("")
        private set

    var correos by mutableStateOf<List<CorreoMasivo>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var hasData by mutableStateOf(false)
        private set

    var selectedCorreo by mutableStateOf<CorreoMasivo?>(null)
        private set
    var isDialogOpen by mutableStateOf(false)
        private set

    private var currentCtacli: String? = null
    private val selectedYear: Int = currentDate.year
    private var allCorreos: List<CorreoMasivo> = emptyList()

    init {
        updateDiasFor(selectedMes, resetSelection = false)
    }

    fun seleccionarHijo(ctacli: String) {
        currentCtacli = ctacli
        selectedMes = monthName(currentDate.monthValue)
        updateDiasFor(selectedMes, resetSelection = true)
        searchQuery = ""
        fetchCorreos()
    }

    fun onMesSelected(value: String) {
        if (selectedMes == value) return
        selectedMes = value
        updateDiasFor(value, resetSelection = true)
        fetchCorreos()
    }

    fun onDiaSelected(value: String) {
        if (selectedDia == value) return
        selectedDia = value
        fetchCorreos()
    }

    fun onSearchChanged(value: String) {
        if (!hasData && value.isNotEmpty()) return
        if (searchQuery == value) return
        searchQuery = value
        if (value.isBlank()) {
            fetchCorreos()
        } else {
            correos = applySearch(value)
        }
    }

    fun filteredCorreos(): List<CorreoMasivo> = correos

    fun openDetalle(correo: CorreoMasivo) {
        selectedCorreo = correo
        isDialogOpen = true
    }

    fun closeDetalle() {
        isDialogOpen = false
    }

    fun retry() {
        fetchCorreos()
    }

    private fun fetchCorreos() {
        val ctacli = currentCtacli ?: return
        val mesParametro = monthParam()
        val diaParametro = dayParam()

        viewModelScope.launch {
            isLoading = true
            error = null
            hasData = false
            allCorreos = emptyList()
            correos = emptyList()

            when (val res = repository.getCorreos(
                ctacli = ctacli,
                dia = diaParametro,
                mes = mesParametro,
                anio = selectedYear.toString()
            )) {
                is Result.Correcto -> {
                    val data = res.datos.orEmpty()
                    allCorreos = data
                    hasData = data.isNotEmpty()
                    correos = applySearch(searchQuery)
                }
                is Result.Error -> {
                    error = res.mensaje ?: "Ocurrió un error al obtener los correos."
                }
            }

            isLoading = false
        }
    }

    private fun updateDiasFor(mes: String, resetSelection: Boolean) {
        val actualDays = if (mes == TODOS) {
            emptyList()
        } else {
            val monthNumber = monthMap[mes] ?: return
            val length = YearMonth.of(selectedYear, monthNumber).lengthOfMonth()
            (1..length).map { String.format("%02d", it) }
        }

        val newDias = if (mes == TODOS) {
            listOf(TODOS)
        } else {
            listOf(TODOS) + actualDays
        }
        dias = newDias

        val firstAvailableDay = actualDays.firstOrNull() ?: TODOS
        selectedDia = when {
            mes == TODOS -> TODOS
            resetSelection -> {
                val monthNumber = monthMap[mes]
                val currentDay = dayFormatter.format(currentDate)
                when {
                    monthNumber == currentDate.monthValue && currentDay in newDias -> currentDay
                    selectedDia == TODOS -> TODOS
                    else -> firstAvailableDay
                }
            }
            selectedDia in newDias -> selectedDia
            else -> firstAvailableDay
        }
    }

    private fun monthParam(): String {
        return if (selectedMes == TODOS) "00" else {
            val monthNumber = monthMap[selectedMes] ?: return "00"
            String.format("%02d", monthNumber)
        }
    }

    private fun dayParam(): String {
        return if (selectedDia == TODOS) "00" else selectedDia
    }

    private fun monthName(index: Int): String = monthNames[index - 1]

    private fun applySearch(query: String): List<CorreoMasivo> {
        if (query.isBlank()) return allCorreos
        val trimmed = query.trim()
        return allCorreos.filter { correo ->
            correo.remitente.contains(trimmed, ignoreCase = true)
        }
    }
}
