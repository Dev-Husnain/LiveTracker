package com.location.livetracker.data.network.ktor

import com.location.livetracker.domain.model.ResponseState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NetworkClient {

    val mJson = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
    private const val TIME_OUT = 30_000L
    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(mJson)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = TIME_OUT
            connectTimeoutMillis = TIME_OUT
            socketTimeoutMillis = TIME_OUT
        }
    }

    suspend inline fun <reified T> makeNetworkRequest(url: String): ResponseState<T> {
        return try {
            val response: String = httpBuilder(url) {}.body()
            val newResponse: T = mJson.decodeFromString(response)
            (ResponseState.Success(newResponse))
        } catch (e: ClientRequestException) {
            (ResponseState.Error(e.message))
        } catch (e: ServerResponseException) {
            (ResponseState.Error(e.message))
        } catch (e: ServerResponseException) {
            (ResponseState.Error(e.message))
        } catch (e: Exception) {
            (ResponseState.Error(e.message ?: "Unknown error"))
        }
    }


    suspend fun httpBuilder(
        url: String,
        callback: (HttpRequestBuilder) -> Unit,
    ): HttpResponse {
        return httpClient.get(url) {
            callback.invoke(this)
        }

    }
}

