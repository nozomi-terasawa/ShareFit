package com.example.fitbattleandroid.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.metadata.DataOrigin
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.AndroidViewModel
import com.example.fitbattleandroid.MyApplication
import com.example.fitbattleandroid.data.remote.SaveFitnessReq
import com.example.fitbattleandroid.repositoryImpl.SaveFitnessRepositoryImpl
import java.time.Instant

class HealthConnectViewModel(
    application: MyApplication,
    private val saveFitnessRepository: SaveFitnessRepositoryImpl,
) : AndroidViewModel(application) {
    private var _calorieUiState: MutableState<CalorieUiState> = mutableStateOf(CalorieUiState("0"))
    val calorieUiState: State<CalorieUiState> = _calorieUiState

    private var _saveHealthDataState: MutableState<SaveHealthDataState> = mutableStateOf(SaveHealthDataState.Loading)
    val saveHealthDataState: State<SaveHealthDataState> = _saveHealthDataState

    private val userToken = (getApplication() as MyApplication).userToken

    // ローカルからカロリーを取得
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

            val totalCalorie = response.records.sumOf { (it.energy.inCalories / 1000).toInt() }
            _calorieUiState.value =
                _calorieUiState.value.copy(
                    calorie = (totalCalorie * 1).toString(),
                )
        } catch (e: Exception) {
            _calorieUiState.value = CalorieUiState("Error reading calorie data")
        }
    }

    suspend fun saveFitnessData(fitnessReq: SaveFitnessReq) {
        try {
            if (userToken == null) {
                _saveHealthDataState.value = SaveHealthDataState.Error
                return
            }
            saveFitnessRepository.saveFitnessData(fitnessReq, userToken)
            _saveHealthDataState.value = SaveHealthDataState.Success
        } catch (e: Exception) {
            e.printStackTrace()
            _saveHealthDataState.value = SaveHealthDataState.Error
        }
    }
}

data class CalorieUiState(
    val calorie: String,
)

sealed class SaveHealthDataState {
    data object Loading : SaveHealthDataState()

    data object Success : SaveHealthDataState()

    data object Error : SaveHealthDataState()
}
