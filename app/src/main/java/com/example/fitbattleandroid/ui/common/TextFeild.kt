package com.example.fitbattleandroid.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CommonOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    color = Color.Gray,
                )
            },
            shape = RoundedCornerShape(5.dp),
//            colors = TextFieldDefaults.Color(
//                containerColor = Color.Transparent,
//                focusedTextColor = Color.Black,
//                unfocusedTextColor = Color.White,
//            ),
            modifier =
                Modifier
                    .width(300.dp)
                    .padding(bottom = 20.dp),
        )
    }
}
