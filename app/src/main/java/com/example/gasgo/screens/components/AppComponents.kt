package com.example.gasgo.screens.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.FormatListNumbered
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TableRows
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gasgo.R
import com.example.gasgo.model.GasStation
import com.example.gasgo.router.Routes
import com.example.gasgo.screens.filters.searchGasStationsByDescription
import com.example.gasgo.ui.theme.buttonDisabledColor
import com.example.gasgo.ui.theme.greyTextColor
import com.example.gasgo.ui.theme.lightRedColor
import com.example.gasgo.ui.theme.mainColor
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun loginImage(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),

        contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.loginimage),
            contentDescription = "Login Image",
            modifier = Modifier
                .width(210.dp)
                .height(210.dp)
        )
    }
}

@Composable
fun registerImage(
    selectedImageUri: MutableState<Uri?>,
    isError: MutableState<Boolean>

) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri.value == Uri.EMPTY) {
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(140.dp)
                    .border(
                        if (isError.value) BorderStroke(2.dp, Color.Red) else BorderStroke(
                            0.dp,
                            Color.Transparent
                        )
                    )
                    .clip(RoundedCornerShape(70.dp)) // 50% border radius
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )
        } else {
            selectedImageUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(70.dp)) // 50% border radius
                        .background(Color.LightGray)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun headingText(textValue: String){
    Text(style = TextStyle(
        color = Color.Black,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
        text = textValue
    )
}

@Composable
fun greyText(textValue: String){
    Text(style = TextStyle(
        color = greyTextColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}
@Composable
fun greyTextBigger(textValue: String){
    Text(style = TextStyle(
        color = greyTextColor,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}
@Composable
fun inputTextIndicator(textValue: String){
    Text(style = TextStyle(
        color = Color.Black,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium
    ),
        text = textValue
    )
}

@Composable
fun customTextInput(
    isEmail: Boolean,
    isNumber: Boolean = false,
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = if(isEmail && !isNumber) KeyboardOptions(keyboardType = KeyboardType.Email) else if(!isEmail && isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}

@Composable
fun customRichTextInput(
    inputValue: MutableState<String>,
    inputText: String,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }
}


@Composable
fun customPasswordInput(
    inputValue: MutableState<String>,
    inputText: String,
    leadingIcon: ImageVector,
    isError: MutableState<Boolean>,
    errorText: MutableState<String>
){
    var showPassword = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .shadow(
                6.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isError.value) Color.Red else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                Color.White,
                shape = RoundedCornerShape(20.dp)
            )
    ){
        OutlinedTextField(
            value = inputValue.value,
            onValueChange = { newValue ->
                inputValue.value = newValue
                isError.value = false
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = inputText,
                    style = TextStyle(
                        color = greyTextColor,
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            leadingIcon = {
                Icon(imageVector = leadingIcon,
                    contentDescription = "",
                    tint = Color.Black)
            },
            trailingIcon = {
                IconButton(onClick = {
                    showPassword.value = !showPassword.value
                }) {
                    Icon(
                        imageVector = if(!showPassword.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = "",
                        tint = Color.Black
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            visualTransformation = if(!showPassword.value) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
    if(isError.value && errorText.value.isNotEmpty()) {
        Text(
            text = errorText.value,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        )
    }else{
        Text(text = " ")
    }
}

@Composable
fun loginRegisterCustomButton(
    buttonText: String,
    isEnabled: MutableState<Boolean>,
    isLoading: MutableState<Boolean>,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .height(50.dp)
            .shadow(6.dp, shape = RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
            contentColor = Color.Black,
            disabledContainerColor = buttonDisabledColor,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        enabled = isEnabled.value
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = buttonText,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}


@Composable
fun customClickableText(
    firstText: String,
    secondText: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = firstText,
            style = TextStyle(
                fontSize = 12.sp
            )
        )
        Text(
            text = secondText,
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(start = 4.dp),
            style = TextStyle(
                fontSize = 12.sp,
                color = mainColor
            )
        )
    }
}

@Composable
fun customAuthError(
    errorText: String
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(lightRedColor)
        .height(50.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = errorText,
            style = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun mapNavigationBar(
    searchValue: MutableState<String>,
    profileImage: String,
    onImageClick: () -> Unit,
    gasStations: MutableList<GasStation>,
    navController: NavController?,
    cameraPositionState: CameraPositionState
){
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchList = remember{
        mutableListOf<GasStation>()
    }

    searchList.clear()
    searchList.addAll(searchGasStationsByDescription(gasStations, searchValue.value).toMutableList())

    val focusRequester = remember{
        FocusRequester()
    }

    val isFocused = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .weight(1f)
                .shadow(
                    6.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(20.dp)
                )
        ){
            OutlinedTextField(
                modifier = Modifier
                    .height(50.dp)
                    .focusRequester(focusRequester = focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused.value = focusState.isFocused
                    },
                value = searchValue.value,
                onValueChange = { newValue ->
                    searchValue.value = newValue
                    isFocused.value = true
                },
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_text),
                        style = TextStyle(
                            color = greyTextColor
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "",
                        tint = mainColor
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default
            )
            if(isFocused.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 60.dp)
                        .background(Color.White),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        for (gasStation in searchList) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .clickable {
                                            val gasStationJson = Gson().toJson(gasStation)
                                            val encodedGasStationJson = URLEncoder.encode(gasStationJson, StandardCharsets.UTF_8.toString())
                                            navController?.navigate(Routes.gasStationScreen + "/$encodedGasStationJson")
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            model = gasStation.mainImage,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .width(40.dp)
                                                .height(40.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = if (gasStation.description.length > 26) {
                                                gasStation.description.substring(0, 26) + "..."
                                            } else {
                                                gasStation.description
                                            }
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            isFocused.value = false
                                            keyboardController?.hide()
                                            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                                                LatLng(gasStation.location.latitude, gasStation.location.longitude), 17f)
                                        },
                                        modifier = Modifier
                                            .wrapContentWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.MyLocation,
                                            contentDescription = "",
                                            tint = mainColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .shadow(
                    6.dp,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center

        ){
            AsyncImage(
                model = profileImage,
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .size(50.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable {
                        onImageClick()
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}


fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {


    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )


    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
@Composable
fun bitmapDescriptorFromUrlWithRoundedCorners(
    context: Context,
    imageUrl: String,
    cornerRadius: Float
): State<BitmapDescriptor?> {
    val bitmapDescriptorState = remember { mutableStateOf<BitmapDescriptor?>(null) }

    LaunchedEffect(imageUrl) {
        try {
            withContext(Dispatchers.IO) {
                val inputStream = URL(imageUrl).openStream()
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()


                val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()


                val targetWidth = 150
                val targetHeight = (targetWidth / aspectRatio).toInt()
                val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, targetWidth, targetHeight, false)


                val roundedBitmap = Bitmap.createBitmap(
                    resizedBitmap.width,
                    resizedBitmap.height,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(roundedBitmap)
                val paint = Paint().apply {
                    isAntiAlias = true
                    shader = BitmapShader(resizedBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                }
                val rect = RectF(0f, 0f, resizedBitmap.width.toFloat(), resizedBitmap.height.toFloat())
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)

                val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(roundedBitmap)
                bitmapDescriptorState.value = bitmapDescriptor
            }
            Log.d("Ucitana", "Slika je ucitana sa zaobljenim uglovima")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Ucitana", e.toString())
        }
    }

    return rememberUpdatedState(bitmapDescriptorState.value)
}

@Composable
fun mapFooter(
    openAddNewGasStation: () -> Unit,
    active: Int,
    onHomeClick: () -> Unit,
    onTableClick: () -> Unit,
    onRankingClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                    spotColor = Color.Transparent
                )
                .border(
                    1.dp,
                    Color.Transparent,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = onHomeClick) {
                    Icon(
                        imageVector = Icons.Outlined.Home,  // Replace with appropriate icon
                        contentDescription = "",
                        tint = if(active == 0) mainColor else greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                IconButton(onClick = onTableClick) {
                    Icon(
                        imageVector = Icons.Outlined.TableRows,
                        contentDescription = "",
                        tint = if(active == 1) mainColor else greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                Spacer(modifier = Modifier.size(70.dp))
                IconButton(onClick = onRankingClick) {
                    Icon(
                        imageVector = Icons.Outlined.FormatListNumbered,
                        contentDescription = "",
                        tint = if(active == 2) mainColor else greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "",
                        tint = if(active == 3) mainColor else greyTextColor,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-30).dp)
                .size(90.dp)
        ) {
            IconButton(onClick = openAddNewGasStation,
                modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.searchimage),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CustomCrowd(
    selectedOption: MutableState<Int>
){
    val customModifier = Modifier
        .fillMaxSize()
        .shadow(
            elevation = 20.dp,
            shape = RoundedCornerShape(10.dp),
            spotColor = Color.Transparent
        )
        .border(
            1.dp,
            Color.Transparent,
            shape = RoundedCornerShape(10.dp),
        )
        .background(
            Color.White,
            shape = RoundedCornerShape(10.dp),
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 0)
                    customModifier.background(Color.Green)
                else
                    customModifier.clickable {
                        selectedOption.value = 0
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Slabo")
            }
        }
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 1)
                    customModifier.background(Color.Yellow)
                else
                    customModifier.clickable {
                        selectedOption.value = 1
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Srednje")
            }
        }
        Row(modifier = Modifier
            .width(100.dp)
            .height(40.dp)
        ) {
            Box(
                modifier =
                if(selectedOption.value == 2)
                    customModifier.background(Color.Red)
                else
                    customModifier.clickable {
                        selectedOption.value = 2
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Mnogo")
            }
        }
    }
}

@Composable
fun CustomImageForNewGasStation(
    selectedImageUri: MutableState<Uri?>
){
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )

    val interactionSource = remember { MutableInteractionSource() }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(vertical = 2.dp)
        .shadow(
            6.dp,
            shape = RoundedCornerShape(20.dp)
        )
        .border(
            1.dp,
            Color.Transparent,
            shape = RoundedCornerShape(20.dp)
        )
        .background(
            greyTextColor,
            shape = RoundedCornerShape(20.dp)
        ),
        contentAlignment = Alignment.Center
    ){
        if (selectedImageUri.value == Uri.EMPTY) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.uploadimage),
                    contentDescription = ""
                )
                Text(text = "Dodaj naslovnu sliku")
            }
        }else{
            selectedImageUri.value?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun CustomGalleryForAddNewGasStation(
    selectedImages: MutableState<List<Uri>>
) {
    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris != null) {
            selectedImages.value += uris
        }
    }

    LazyRow {
        if (selectedImages.value.size < 5) {
            item {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(100.dp)
                        .height(100.dp)
                        .border(
                            1.dp,
                            greyTextColor,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .background(
                            greyTextColor,
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { pickImagesLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.AddAPhoto, contentDescription = "")
                }
            }
        }
        items(selectedImages.value.size) { index ->
            val uri = selectedImages.value[index]
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .width(100.dp)
                    .height(100.dp)
                    .border(
                        1.dp,
                        Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .clickable { selectedImages.value -= uri },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}