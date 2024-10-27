package com.example.fitbattleandroid.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitbattleandroid.ui.theme.onPrimaryDark

@Composable
fun NormalText(text:String){
    Column {
        Text(
            text = text,
            color = onPrimaryDark,
        )
    }
}

@Composable
@Preview
fun NormalTextPreview(){
    NormalText("Hello")
}