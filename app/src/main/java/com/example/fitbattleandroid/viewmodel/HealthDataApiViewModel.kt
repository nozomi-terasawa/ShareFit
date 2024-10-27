package com.example.fitbattleandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fitbattleandroid.data.EntryGeoFenceRes
import com.example.fitbattleandroid.data.MemberInfo
import com.example.fitbattleandroid.data.SaveFitnessReq
import com.example.fitbattleandroid.model.EntryGeoFenceReq
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class HealthDataApiViewModel : ViewModel() {
    private val _encounterMembers = MutableStateFlow<List<MemberInfo>>(emptyList())
    val encounterMembers: StateFlow<List<MemberInfo>> = _encounterMembers.asStateFlow()

    private val client =
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }
        }

    // ジオフェンス入室リクエスト関数
    suspend fun sendGeoFenceEntryRequest(entry: EntryGeoFenceReq): EntryGeoFenceRes {
        val res =
            client.post("http://192.168.224.234:7070/api/v1/geofence/entry") {
                contentType(ContentType.Application.Json)
                setBody(entry)
            }
        // レスポンスからボディを取得して変数に追加
        val responseBody = res.body<EntryGeoFenceRes>()
        for (member in responseBody.passingMember) {
            _encounterMembers.value += member
        }
        return responseBody
    }

    // フィットネスデータの保存リクエスト
    suspend fun sendFitnessSave(request: SaveFitnessReq): HttpResponse =
        client.post("http://192.168.224.234:7070/api/v1/fitness/save") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
}
