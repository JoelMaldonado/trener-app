package com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.Tareas.Incumplimientos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjmf.colegiotrenerandroid.core.Result
import com.jjmf.colegiotrenerandroid.domain.model.Incumplimiento
import com.jjmf.colegiotrenerandroid.domain.repository.CitaInformeRepository
import com.jjmf.colegiotrenerandroid.domain.repository.TareaRepository
import com.jjmf.colegiotrenerandroid.ui.features.Menu.Features.CitaInforme.Trimestre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TareasIncumplimientosViewModel @Inject constructor(
    private val repository: CitaInformeRepository,
    private val repo: TareaRepository
) : ViewModel() {

    var trimestre by mutableStateOf<Trimestre?>(null)
    var list by mutableStateOf<List<Incumplimiento>>(emptyList())
    var totalAcumulado by mutableStateOf(0)
    var error by mutableStateOf<String?>(null)
    var isLoadingList by mutableStateOf(false)

    init {
        getTrimestreActual()
    }


    private fun getTrimestreActual() {
        viewModelScope.launch {
            try {
                when (val res = repository.getTrimestreActual()) {
                    is Result.Correcto -> trimestre = res.datos
                    is Result.Error -> error = res.mensaje
                }
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    fun listarIncumplimientos(ctactli: String) {
        viewModelScope.launch {
            try {
                isLoadingList = true
                val res = repo.listarIncumplimientos(
                    ctactli = ctactli
                )
                when (res) {
                    is Result.Correcto -> {
                        val data = res.datos ?: emptyList()
                        list = data
                        totalAcumulado = data.firstNotNullOfOrNull { it.total } ?: 0
                    }

                    is Result.Error -> {
                        error = res.mensaje
                        list = emptyList()
                        totalAcumulado = 0
                    }
                }
            } catch (e: Exception) {
                error = e.message
                list = emptyList()
                totalAcumulado = 0
            } finally {
                isLoadingList = false
            }
        }
    }
}