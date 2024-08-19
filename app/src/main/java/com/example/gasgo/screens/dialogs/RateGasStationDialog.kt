package com.example.gasgo.screens.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gasgo.ui.theme.buttonDisabledColor
import com.example.gasgo.ui.theme.greyTextColor
import com.example.gasgo.ui.theme.lightMailColor
import com.example.gasgo.ui.theme.mainColor

@Composable
fun RateGasStationDialog(

    showRateDialog: MutableState<Boolean>,
    rate: MutableState<Int>,
    rateGasStation: () -> Unit,
    isLoading: MutableState<Boolean>
) {
    val interactionSource = remember { MutableInteractionSource() }
    AlertDialog(
        modifier = Modifier
            .clip(
                RoundedCornerShape(20.dp)
            ),
        onDismissRequest = {},

        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .background(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = "Kako biste ocenili ovu benzinsku stanicu?",
                            style = TextStyle(
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround

                        ) {
                            for (i in 1..5){
                                Icon(
                                    imageVector =
                                    if(rate.value >= i) Icons.Filled.Star
                                    else Icons.Filled.StarBorder,
                                    contentDescription = "",
                                    tint =
                                    if(rate.value >= i) lightMailColor
                                    else greyTextColor,
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clickable(
                                            interactionSource = interactionSource,
                                            indication = null
                                        ) {
                                            rate.value = i
                                        }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        Button(
                            onClick = rateGasStation,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = mainColor,
                                contentColor = Color.Black,
                                disabledContainerColor = buttonDisabledColor,
                                disabledContentColor = Color.White,
                            ),
                        ) {
                            if (isLoading.value) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Potvrdi",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "Zatvori",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    showRateDialog.value = false
                                },
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = greyTextColor,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
        },
        dismissButton = {
        }
    )
}