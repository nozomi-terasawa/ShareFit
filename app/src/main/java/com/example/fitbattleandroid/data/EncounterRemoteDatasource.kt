package com.example.fitbattleandroid.data

import com.example.fitbattleandroid.data.remote.EntryGeoFenceReq
import com.example.fitbattleandroid.data.remote.EntryGeoFenceRes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class EncounterRemoteDatasource {
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
        return responseBody
    }
}
