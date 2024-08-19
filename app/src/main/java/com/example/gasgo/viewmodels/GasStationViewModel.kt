package com.example.gasgo.viewmodels

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gasgo.data.GasStationRepositoryImpl
import com.example.gasgo.data.RateRepositoryImpl
import com.example.gasgo.model.GasStation
import com.example.gasgo.model.Rate
import com.example.gasgo.data.Resource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GasStationViewModel: ViewModel() {
    val repository = GasStationRepositoryImpl()
    val rateRepository = RateRepositoryImpl()

    private val _gasStationFlow = MutableStateFlow<Resource<String>?>(null)
    val gasStationFlow: StateFlow<Resource<String>?> = _gasStationFlow

    private val _newRate = MutableStateFlow<Resource<String>?>(null)
    val newRate: StateFlow<Resource<String>?> = _newRate

    private val _gasStations = MutableStateFlow<Resource<List<GasStation>>>(Resource.Success(emptyList()))
    val gasStations: StateFlow<Resource<List<GasStation>>> get() = _gasStations

    private val _rates = MutableStateFlow<Resource<List<Rate>>>(Resource.Success(emptyList()))
    val rates: StateFlow<Resource<List<Rate>>> get() = _rates


    private val _userGasStations = MutableStateFlow<Resource<List<GasStation>>>(Resource.Success(emptyList()))
    val userGasStations: StateFlow<Resource<List<GasStation>>> get() = _userGasStations

    init {
        getAllGasStations()
    }

    fun getAllGasStations() = viewModelScope.launch {
        _gasStations.value = repository.getAllGasStations()
    }

    fun saveGasStationData(
        description: String,
        crowd: Int,
        mainImage: Uri,
        galleryImages: List<Uri>,
        location: MutableState<LatLng?>
    ) = viewModelScope.launch{
        _gasStationFlow.value = Resource.loading
        repository.saveGasStationData(
            description = description,
            crowd = crowd,
            mainImage = mainImage,
            galleryImages = galleryImages,
            location = location.value!!
        )
        _gasStationFlow.value = Resource.Success("Uspešno dodata benzinska stanica")
    }


    fun getGasStationAllRates(
        gid: String
    ) = viewModelScope.launch {
        _rates.value = Resource.loading
        val result = rateRepository.getGasStationRates(gid)
        _rates.value = result
    }

    fun addRate(
        gid: String,
        rate: Int,
        gasStation: GasStation
    ) = viewModelScope.launch {
        _newRate.value = rateRepository.addRate(gid, rate, gasStation)
    }

    fun updateRate(
        rid: String,
        rate: Int
    ) = viewModelScope.launch{
        _newRate.value = rateRepository.updateRate(rid, rate)
    }

    fun getUserGasStations(
        uid: String
    ) = viewModelScope.launch {
        _userGasStations.value = repository.getUserGasStations(uid)
    }
}

class GasStationViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GasStationViewModel::class.java)){
            return GasStationViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}