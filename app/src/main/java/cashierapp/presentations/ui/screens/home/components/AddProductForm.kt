package cashierapp.presentations.ui.screens.home.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cashierapp.data.resources.Resource
import cashierapp.domain.model.SelectItemModel
import cashierapp.presentations.ui.components.SelectItem
import cashierapp.presentations.ui.theme.BorderGray
import cashierapp.presentations.ui.theme.PrimaryColor
import cashierapp.presentations.viewmodel.home.ProductViewModel
import cashierapp.utils.JwtUtils
import cashierapp.utils.RupiahVisualTransformation
import cashierapp.utils.ScreenSize
import cashierapp.utils.SelectSizeList
import cashierapp.utils.SelectTypeList
import cashierapp.utils.rememberScreenSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductForm(
    viewModel: ProductViewModel = hiltViewModel(),
    onSuccessfulAdd: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var productNameVal by remember { mutableStateOf(TextFieldValue("")) }
    var sizeVal by remember { mutableStateOf("") }
    var typeVal by remember { mutableStateOf("") }
    var priceVal by remember { mutableStateOf(TextFieldValue("")) }
    var descriptionVal by remember { mutableStateOf(TextFieldValue("")) }

    var selectItem by remember { mutableStateOf<List<SelectItemModel>>(emptyList()) }
    var titleSheet by remember { mutableStateOf("") }


    var selectShow by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    val addProductState by viewModel.addProductState.collectAsState()

    val token = viewModel.getToken()

    val userId = JwtUtils.getUserId(token ?: "")

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenSize = rememberScreenSize()

    val sheetSize = when (screenSize) {
        ScreenSize.COMPACT -> screenHeight * 0.3f
        ScreenSize.MEDIUM -> screenHeight * 0.1f
        ScreenSize.EXPANDED -> screenHeight * 0.4f
    }

    LaunchedEffect(addProductState) {
        if (addProductState is Resource.Success && addProductState.data != null) {
            delay(100)
            onSuccessfulAdd()
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Add New Product",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = productNameVal,
            label = { Text("Product Name") },
            onValueChange = { productNameVal = it },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = BorderGray
            ),
            shape = RoundedCornerShape(18),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = BorderGray,
                        shape = RoundedCornerShape(18)
                    )
                    .weight(1f)
                    .padding(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            selectShow = true
                            selectItem = SelectTypeList
                            titleSheet = "Type"
                        }
                    )
            ) {
                Text(
                    text = if (typeVal.isEmpty()) "Type" else typeVal,
                    color = if (typeVal.isEmpty()) Color.Gray else Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = BorderGray,
                        shape = RoundedCornerShape(18)
                    )
                    .weight(1f)
                    .padding(18.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            selectShow = true
                            selectItem = SelectSizeList
                            titleSheet = "Size"
                        }
                    )
            ) {
                Text(
                    text = if (sizeVal.isEmpty()) "Size" else sizeVal,
                    color = if (sizeVal.isEmpty()) Color.Gray else Color.Black
                )
            }
        }


        OutlinedTextField(
            label = { Text("Price") },
            value = priceVal,
            onValueChange = {
                if (it.text.all { char -> char.isDigit() || char == '.' } || it.text.isEmpty()) {
                    priceVal = it
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = BorderGray
            ),
            shape = RoundedCornerShape(18),
            visualTransformation = RupiahVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        OutlinedTextField(
            label = { Text("Description") },
            value = descriptionVal,
            onValueChange = { descriptionVal = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = BorderGray
            ),
            shape = RoundedCornerShape(18),
            minLines = 3,
            maxLines = 5,
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()

                    if (productNameVal.text.isNotEmpty()) {
                        userId?.let {
                            viewModel.addProduct(
                                it,
                                productNameVal.text,
                                priceVal.text.toInt(),
                                descriptionVal.text,
                                typeVal,
                                sizeVal
                            )
                        }
                    }
                }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                userId?.let {
                    viewModel.addProduct(
                        it,
                        productNameVal.text,
                        priceVal.text.toInt(),
                        descriptionVal.text,
                        typeVal,
                        sizeVal
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor
            )
        ) {
            Text(
                text = "Save",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (selectShow) {
        ModalBottomSheet(
            onDismissRequest = {
                selectShow = false
            },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetSize),
            containerColor = Color.White
        ) {
            SelectItem(
                title = titleSheet,
                data = selectItem
            ) { it ->
                if (titleSheet == "Type") typeVal = it
                else sizeVal = it

                scope.launch {
                    sheetState.hide()
                    selectShow = false
                }
            }
        }
    }
}