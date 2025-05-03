package com.location.livetracker.data.repository

import com.google.android.gms.maps.model.LatLng
import com.location.livetracker.core.utils.logIt
import com.location.livetracker.data.network.ktor.NetworkClient
import com.location.livetracker.domain.model.ResponseState
import com.location.livetracker.domain.model.RoutesModel
import com.location.livetracker.domain.repository.RoutesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoutesRepositoryImpl : RoutesRepository {

    private val key = "Your Direction API Here"

    override suspend fun getRoutes(
        fromLatLng: LatLng,
        toLatLng: LatLng
    ): ResponseState<List<LatLng>> {
        val origin = "${fromLatLng.latitude},${fromLatLng.longitude}"
        val destination = "${toLatLng.latitude},${toLatLng.longitude}"
        val mUrl =
            "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&key=$key"

        mUrl.logIt()

        return withContext(Dispatchers.IO) {
            when (val response = NetworkClient.makeNetworkRequest<RoutesModel>(mUrl)) {
                is ResponseState.Error -> {
                    ResponseState.Error(response.error)
                }

                is ResponseState.Idle -> {
                    ResponseState.Idle
                }

                is ResponseState.Loading -> {
                    ResponseState.Loading

                }

                is ResponseState.Success -> {
                    val mResponse = response.data
                    if (mResponse?.error?.isEmpty() == true) {
                        val points = mResponse.routes.firstOrNull()?.overviewPolyline?.points
                        points?.let {
                            val latLngList = decodePolyline(it)
                            "RoutesSize=${latLngList.size}".logIt()
                            ResponseState.Success(latLngList)
                        } ?: ResponseState.Error("No route found")
                    } else {
                        val error =
                            "Error: ${mResponse?.error}\n\n Status: ${mResponse?.status}"
                        ResponseState.Error(error)
                    }

                }
            }
        }
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0

            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)

            val dLat = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
            lat += dLat

            shift = 0
            result = 0

            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)

            val dLng = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
            lng += dLng

            poly.add(LatLng(lat / 1E5, lng / 1E5))
        }

        return poly
    }


}