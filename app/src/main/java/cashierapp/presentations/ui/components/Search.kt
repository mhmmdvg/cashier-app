package cashierapp.presentations.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cashierapp.presentations.ui.theme.BorderGray
import cashierapp.presentations.ui.theme.PrimaryColor

@Composable
fun Search(
    onChange: (TextFieldValue) -> Unit,
    value: TextFieldValue,
    placeholder: String = "Search your product"
) {

    OutlinedTextField(
        value = value,
        placeholder = {
            Text(placeholder)
        },
        onValueChange = onChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 55.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = BorderGray
        ),
        shape = RoundedCornerShape(18),
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
            autoCorrectEnabled = false
        )
    )
}


@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Search(
        value = text,
        onChange = { text = it },
    )
}

