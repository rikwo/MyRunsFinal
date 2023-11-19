package com.example.ricky_kwong_myruns2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

//implementation taken from XD lecture
class RunRepository(private val runDao: RunDao) {
    val allRuns: Flow<List<Run>> = runDao.getRunData()

    fun insert(run: Run) {
        CoroutineScope(Dispatchers.IO).launch{
            runDao.insertRun(run)
        }
    }

    fun delete(id: Long) {
        CoroutineScope(Dispatchers.IO).launch{
            runDao.deleteRun(id)
        }
    }
    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            runDao.deleteAllEntries()
        }
    }
}