package com.example.ricky_kwong_myruns2

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class RunViewModel(private val repository: RunRepository): ViewModel() {
    val allRunsLiveData: LiveData<List<Run>> = repository.allRuns.asLiveData()

    fun insert(run: Run) {
        repository.insert(run)
    }

    fun delete(id: Long) {
        val runList = allRunsLiveData.value
        if (runList != null && runList.size > 0) {
            repository.delete(id)
        }
    }

    fun deleteAll() {
        repository.deleteAll()
    }
}

class RunViewModelFactory(private val repository: RunRepository): ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunViewModel::class.java))
            return RunViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}