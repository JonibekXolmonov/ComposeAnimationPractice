package com.example.composesampleanimation

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

class VolumeCustomProgress : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xff101010))
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(1.dp, color = Color.Green, RoundedCornerShape(10.dp))
                        .padding(30.dp)
                ) {
                    var volume by remember {
                        mutableStateOf(0f)
                    }

                    val barCount = 20

                    VolumeCustomProgressUi(modifier = Modifier.size(100.dp), onValueChane = {
                        volume = it
                    })

                    Spacer(modifier = Modifier.width(20.dp))

                    VolumeBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp),
                        activeBars = (barCount * volume).roundToInt(),
                        barCount = barCount
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun VolumeCustomProgressUi(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 25f,
    onValueChane: (Float) -> Unit
) {
    var rotation by remember {
        mutableStateOf(limitingAngle)
    }

    var touchX by remember {
        mutableStateOf(0f)
    }
    var touchY by remember {
        mutableStateOf(0f)
    }

    var centerX by remember {
        mutableStateOf(0f)
    }

    var centerY by remember {
        mutableStateOf(0f)
    }


    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(50))
            .onGloballyPositioned {
                val windowBounds = it.boundsInWindow()
                centerX = windowBounds.size.width / 2f
                centerY = windowBounds.size.height / 2f
            }
            .pointerInteropFilter { motionEvent ->
                touchX = motionEvent.x
                touchY = motionEvent.y
                val angle = -atan2(centerX - touchX, centerY - touchY) * (180f / PI).toFloat()
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE -> {
                        if (angle !in -limitingAngle..limitingAngle) {
                            val fixedAngle = if (angle in -180f..-limitingAngle) {
                                360f + angle
                            } else {
                                angle
                            }
                            rotation = fixedAngle

                            val percent = (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
                            onValueChane(percent)
                            true
                        } else false
                    }
                    else -> false
                }
            }
            .rotate(rotation)
    )
}

@Composable
fun VolumeBar(
    modifier: Modifier = Modifier,
    activeBars: Int = 0,
    barCount: Int = 10
) {
    BoxWithConstraints(contentAlignment = Alignment.Center, modifier = modifier) {
        val barWidth = remember {
            constraints.maxWidth / (2f * barCount)
        }

        Canvas(modifier = modifier) {
            for (i in 0 until barCount) {
                drawRoundRect(
                    color = if (i in 0..activeBars) Color.Green else Color.DarkGray,
                    topLeft = Offset(i * barWidth * 2f + barWidth / 2f, 0f),
                    size = Size(barWidth, constraints.maxHeight.toFloat()),
                    cornerRadius = CornerRadius.Zero
                )
            }
        }
    }
}