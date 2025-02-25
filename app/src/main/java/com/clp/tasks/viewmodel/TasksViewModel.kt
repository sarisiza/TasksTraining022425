package com.clp.tasks.viewmodel

import androidx.credentials.Credential
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clp.tasks.data.login.LoginRepository
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
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel() {

    //encapsulation
    private val _loginState: MutableLiveData<UiState<FirebaseUser>> = MutableLiveData(UiState.LOADING)
    val loginState: LiveData<UiState<FirebaseUser>> get() = _loginState

    fun authenticate(credential: Credential){
        viewModelScope.launch(coroutineDispatcher) {
            loginRepository.handleLogin(credential).collect{
                _loginState.postValue(it)
            }
        }
    }

}