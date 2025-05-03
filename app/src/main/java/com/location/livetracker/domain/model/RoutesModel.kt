package com.location.livetracker.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutesModel(
    @SerialName("error_message")
    val error: String = "",
    val status: String = "",
    val routes: List<Routes> = emptyList()
)

@Serializable
data class Routes(
    @SerialName("overview_polyline")
    val overviewPolyline: OverviewPolyline = OverviewPolyline()
)

@Serializable
data class OverviewPolyline(
    val points: String = ""
)

