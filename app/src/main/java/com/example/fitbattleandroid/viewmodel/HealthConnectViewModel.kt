package com.example.fitbattleandroid.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.ViewModel
import java.time.Instant

class HealthConnectViewModel : ViewModel() {
    private var _calorieUiState: MutableState<CalorieUiState> = mutableStateOf(CalorieUiState("0"))
    val calorieUiState: State<CalorieUiState> = _calorieUiState

    suspend fun readCalorie(
        healthConnectClient: HealthConnectClient,
        startTime: Instant,
        endTime: Instant,
    ) {
        try {
            val response =
                healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = TotalCaloriesBurnedRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
                        dataOriginFilter = setOf(DataOrigin("com.fitbit.FitbitMobile")),
                    ),
                )

            /*
            for (record in response.records) {
                Log.d("result", "${record.startTime}: ${record.energy.inCalories}")
            }
             */

            val totalCalorie = response.records.sumOf { (it.energy.inCalories / 1000).toInt() }
            _calorieUiState.value =
                _calorieUiState.value.copy(
                    calorie = (totalCalorie * 0.001).toString(),
                )
        } catch (e: Exception) {
            _calorieUiState.value = CalorieUiState("Error reading calorie data")
        }
    }
}

data class CalorieUiState(
    val calorie: String,
)
