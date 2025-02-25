package com.clp.tasks.viewmodel

import android.content.Context
import androidx.credentials.Credential
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clp.tasks.data.login.LoginRepository
import com.clp.tasks.data.room.Task
import com.clp.tasks.data.room.TasksRepository
import com.clp.tasks.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tasksRepository: TasksRepository,
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

    fun insertTask(task: Task){
        viewModelScope.launch(coroutineDispatcher) {
            tasksRepository.insertTask(task).collect{
                _insertTask.postValue(it)
            }
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(coroutineDispatcher) {
            tasksRepository.deleteTask(task).collect{
                _deleteTask.postValue(it)
            }
        }
    }

}