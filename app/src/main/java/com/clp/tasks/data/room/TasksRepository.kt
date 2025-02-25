package com.clp.tasks.data.room

import com.clp.tasks.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TasksRepository {

    fun getAllTasks(): Flow<UiState<List<Task>>>

    fun insertTask(task: Task): Flow<UiState<Unit>>

    fun deleteTask(task: Task): Flow<UiState<Unit>>

}