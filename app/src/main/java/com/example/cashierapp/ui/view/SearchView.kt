package com.example.cashierapp.ui.view
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.cashierapp.ui.theme.BorderGray

@Composable
fun SearchView(){
    var text by remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        value = text,
        placeholder = {
            Text("Search")
        },
        onValueChange = { newText ->
            text = newText
        },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = BorderGray
        ),
        shape = RoundedCornerShape(18),
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        },
    )
}


@Preview(showBackground = true)
@Composable
fun SearchPreview(){
    SearchView()
}

