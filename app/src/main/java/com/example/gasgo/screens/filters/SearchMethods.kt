package com.example.gasgo.screens.filters

import com.example.gasgo.model.GasStation

fun searchGasStationsByDescription(
    gasStations: MutableList<GasStation>,
    query: String
):List<GasStation>{
    val regex = query.split(" ").joinToString(".*"){
        Regex.escape(it)
    }.toRegex(RegexOption.IGNORE_CASE)
    return gasStations.filter { gasStation ->
        regex.containsMatchIn(gasStation.description)
    }
}