package com.example.composesampleanimation

import android.os.Bundle
import android.view.animation.Animation
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composesampleanimation.ui.theme.ComposeSampleAnimationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleAnimationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    com.example.composesampleanimation.Animation()
                }
            }
        }
    }
}

@Composable
fun Animation(
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.fillMaxSize()) {

        var isVisible by remember { mutableStateOf(false) }
        var isRound by remember { mutableStateOf(false) }

        Button(
            onClick = {
                isVisible = !isVisible
                isRound = !isRound
            }) {
            Text(text = "Click to animate")
        }

        val transition = updateTransition(
            targetState = isRound,
            label = null
        )

        val borderRadius by transition.animateInt(
            transitionSpec = { tween(2000) },
            label = "Border radius",
            targetValueByState = { isRound ->
                if (isRound) 100 else 0

            }
        )

//        val borderRadius by animateIntAsState(
//            targetValue = if (isRound) 100 else 1,
//            animationSpec = tween(
//                durationMillis = 2000
//            )
//        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(borderRadius))
                .background(Color.Blue)
        )


        //visibility animation
//        AnimatedVisibility(
//            visible = isVisible,
//            enter = slideInHorizontally() + fadeIn(),
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(1f)
//        ) {
//            Box(
//                modifier = Modifier.background(Color.Blue)
//            )
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeSampleAnimationTheme {
        com.example.composesampleanimation.Animation()
    }
}