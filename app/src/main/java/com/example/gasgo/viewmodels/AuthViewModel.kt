package com.example.gasgo.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gasgo.data.AuthRepositoryImp
import com.example.gasgo.data.Resource
import com.example.gasgo.model.CustomUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(): ViewModel(){
    val repository = AuthRepositoryImp()
    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow

    private val _currentUserFlow = MutableStateFlow<Resource<CustomUser>?>(null)
    val currentUserFlow: StateFlow<Resource<CustomUser>?> = _currentUserFlow

    private val _allUsers = MutableStateFlow<Resource<List<CustomUser>>?>(null)
    val allUsers: StateFlow<Resource<List<CustomUser>>?> = _allUsers

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    fun getUserData() = viewModelScope.launch {
        val result = repository.getUserData()
        _currentUserFlow.value = result
    }

    fun getAllUserData() = viewModelScope.launch {
        val result = repository.getAllUserData()
        _allUsers.value = result
    }

    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch{
        _loginFlow.value = Resource.loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }
    fun register(fullName: String, phoneNumber: String, profileImage: Uri, email: String, password: String) = viewModelScope.launch{
        _registerFlow.value = Resource.loading
        val result = repository.register(fullName, phoneNumber, profileImage, email, password)
        _registerFlow.value = result
    }

    fun logout(){
        repository.logout()
        _loginFlow.value = null
        _registerFlow.value = null
        _currentUserFlow.value = null
    }
}

class AuthViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}