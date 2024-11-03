package com.example.fitbattleandroid.repository

import com.example.fitbattleandroid.data.remote.SaveFitnessReq

interface SaveFitnessRepository {
    suspend fun saveFitnessData(request: SaveFitnessReq)
}
