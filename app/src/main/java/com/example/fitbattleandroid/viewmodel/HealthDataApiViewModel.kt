package com.example.fitbattleandroid.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import kotlinx.serialization.json.Json

class HealthDataApiViewModel: ViewModel() {
    private val _encounterMembers = mutableStateListOf<MemberInfo>()
    val encounterMembers: SnapshotStateList<MemberInfo> get() = _encounterMembers


    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    // ジオフェンス入室リクエスト関数
    suspend fun sendGeoFenceEntryRequest(entry: EntryGeoFenceReq): EntryGeoFenceRes {
        val res =  client.post("http://192.168.224.234:7070/api/v1/geofence/entry") {
            contentType(ContentType.Application.Json)
            setBody(entry)
        }
        // レスポンスからボディを取得して変数に追加
        val responseBody = res.body<EntryGeoFenceRes>()
        for (member in responseBody.passingMember) {
            _encounterMembers.add(member)
        }
        Log.d("result", _encounterMembers.toString())

        return responseBody
    }

    // フィットネスデータの保存リクエスト
    suspend fun sendFitnessSave(request: SaveFitnessReq): HttpResponse {
        return client.post("http://192.168.224.234:7070/api/v1/fitness/save") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}