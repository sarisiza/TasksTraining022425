package com.clp.tasks.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.clp.tasks.data.login.LoginRepository
import com.clp.tasks.data.notifications.MessagingRepository
import com.clp.tasks.data.room.Task
import com.clp.tasks.data.room.TasksRepository
import com.clp.tasks.notifications.NotificationsWorker
import com.clp.tasks.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TAG = "TasksViewModel"
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tasksRepository: TasksRepository,
    private val messagingRepository: MessagingRepository,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel() {

    //encapsulation
    private val _loginState: MutableLiveData<UiState<FirebaseUser>> = MutableLiveData(UiState.LOADING)
    val loginState: LiveData<UiState<FirebaseUser>> get() = _loginState

    private val _logoutState: MutableLiveData<UiState<Unit>> = MutableLiveData(UiState.LOADING)
    val logoutState: LiveData<UiState<Unit>> get() = _logoutState

    private val _tasksState: MutableLiveData<UiState<List<Task>>> = MutableLiveData(UiState.LOADING)
    val tasksState: LiveData<UiState<List<Task>>> get() = _tasksState

    private val _insertTask: MutableLiveData<UiState<Unit>> = MutableLiveData(UiState.LOADING)
    val insertTask: LiveData<UiState<Unit>> get() = _insertTask

    private val _deleteTask: MutableLiveData<UiState<Unit>> = MutableLiveData(UiState.LOADING)
    val deleteTask: LiveData<UiState<Unit>> get() = _deleteTask

    init {
        retrieveUserToken()
    }

    fun authenticate(context: Context){
        viewModelScope.launch(coroutineDispatcher) {
            loginRepository.handleLogin(context).collect{
                _loginState.postValue(it)
            }
        }
    }

    fun logOut(){
        viewModelScope.launch(coroutineDispatcher) {
            loginRepository.handleLogout().collect{
                _logoutState.postValue(it)
            }
        }
    }

    fun getTasks(){
        viewModelScope.launch(coroutineDispatcher) {
            tasksRepository.getAllTasks().collect{
                _tasksState.postValue(it)
            }
        }
    }

    fun insertTask(task: Task, context: Context){
        viewModelScope.launch(coroutineDispatcher) {
            tasksRepository.insertTask(task).collect{
                _insertTask.postValue(it)
            }
        }
        val inputData = Data
            .Builder()
            .putString(NotificationsWorker.TITLE,task.title)
            .putString(NotificationsWorker.DESCRIPTION,task.description)
            .putString(NotificationsWorker.DUE_DATE,task.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
            .build()
        val delay = task.dueDate.atTime(10,0).toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(
            ZoneOffset.UTC)
        val notificationsWorker = OneTimeWorkRequestBuilder<NotificationsWorker>()
            .setInputData(inputData)
            //.setInitialDelay(delay,TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(task.id,ExistingWorkPolicy.REPLACE,notificationsWorker)
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(coroutineDispatcher) {
            tasksRepository.deleteTask(task).collect{
                _deleteTask.postValue(it)
            }
        }
    }

    private fun retrieveUserToken(){
        viewModelScope.launch(coroutineDispatcher) {
            messagingRepository.getToken().collect{
                when (it){
                    is UiState.ERROR -> {
                        Log.e(TAG, "error retrieving token: ${it.error.localizedMessage}", it.error)
                    }
                    UiState.LOADING -> {}
                    is UiState.SUCCESS -> {
                        Log.d(TAG, "retrieveUserToken: ${it.message}")
                    }
                }
            }
        }
    }

}