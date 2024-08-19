package com.example.gasgo.data

import com.example.gasgo.model.Rate
import com.example.gasgo.model.GasStation

interface RateRepository {
        suspend fun getGasStationRates(
            gid: String
        ): Resource<List<Rate>>
        suspend fun getUserRates(): Resource<List<Rate>>
        suspend fun getUserAdForGasStation(): Resource<List<Rate>>
        suspend fun addRate(
            gid: String,
            rate: Int,
            gasStation: GasStation
        ): Resource<String>

        suspend fun updateRate(
            rid: String,
            rate: Int,
        ): Resource<String>
    }
