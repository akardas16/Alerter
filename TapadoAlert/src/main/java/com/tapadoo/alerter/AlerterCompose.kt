package com.tapadoo.alerter

import android.app.Activity
import android.util.Log
import android.view.Gravity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy


@Composable
fun Alerter(
    isShown: Boolean, onChanged: (isShown: Boolean) -> Unit,
    backgroundColor: Color = Color.Transparent,
    duration:Long = 3000,
    enableVibration:Boolean = true,
    enableSwipeToDismiss:Boolean = false,
    disableOutsideTouch:Boolean = false,
    enableInfiniteDuration:Boolean = false,
    gravity: Int = Gravity.TOP,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = isShown) {

        if (isShown) {
            Alerter.create(context as Activity)
                .enableVibration(enableVibration)
                .enableSwipeToDismiss(enableSwipeToDismiss)
                .enableInfiniteDuration(enableInfiniteDuration)
                .setLayoutGravity(gravity)
                .disableOutsideTouch(disableOutsideTouch)
                .setBackgroundColorInt(backgroundColor.toArgb())
                .also { alerter ->
                    alerter.layoutComposeView()?.apply {
                        setViewCompositionStrategy(
                            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                        )
                        setContent {
                            MaterialTheme {
                                content()
                            }
                        }
                    }
                }
                .listenAlertStatus {
                    if (it.not()) onChanged(false)
                }
                .setDuration(duration)
                .show()
        } else {
            if (Alerter.isShowing) Alerter.hide()
        }
    }
    

}

//Pulse Modifier
 fun Modifier.iconPulse() = composed {

    val transition = rememberInfiniteTransition(label = "")

    val animateScale by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 820, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Modifier
        .scale(animateScale)

}