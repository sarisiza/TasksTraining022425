package com.clp.tasks.data.room

import com.clp.tasks.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val dao: TasksDao
) : TasksRepository{

    override fun getAllTasks(): Flow<UiState<List<Task>>> = flow{
        emit(UiState.LOADING)
        try {
            val result = dao.getTasks()
            emit(UiState.SUCCESS(result.toTask()))
        } catch (e: Exception){
            emit(UiState.ERROR(e))
        }
    }

    override fun insertTask(task: Task): Flow<UiState<Unit>> = flow{
        emit(UiState.LOADING)
        try {
            dao.insertTask(task.toTable())
            emit(UiState.SUCCESS(Unit))
        } catch (e: Exception){
            emit(UiState.ERROR(e))
        }
    }

    override fun deleteTask(task: Task): Flow<UiState<Unit>> = flow{
        emit(UiState.LOADING)
        try {
            dao.deleteTask(task.toTable())
            emit(UiState.SUCCESS(Unit))
        } catch (e: Exception){
            emit(UiState.ERROR(e))
        }
    }

}