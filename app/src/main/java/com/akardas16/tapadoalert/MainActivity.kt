package com.akardas16.tapadoalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.akardas16.alerter.R
import com.akardas16.tapadoalert.ui.theme.AlerterTheme
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.iconPulse

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlerterTheme {
                // A surface container using the 'background' color from the theme
                MyContent()
            }
        }
    }
}

@Composable
fun MyContent(){
    var showAlert1 by remember { mutableStateOf(false) }
    var showAlert2 by remember { mutableStateOf(false) }
    var showAlert3 by remember { mutableStateOf(false) }
    var showAlert4 by remember { mutableStateOf(false) }
    var showAlert5 by remember { mutableStateOf(false) }
    var showAlert6 by remember { mutableStateOf(false) }
    var showAlert7 by remember { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {

            Alerter(isShown = showAlert1, onChanged = {showAlert1 = it},
                backgroundColor = Color(0xFF46899E)
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Friend Request", color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(text = "You have new friend request", color = Color.White, fontSize = 14.sp)
                }
            }

            Alerter(isShown = showAlert2, onChanged = {showAlert2 = it},
                backgroundColor = Color.Transparent
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        Color(0xFFE2E1E1),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {

                    Spacer(modifier = Modifier.padding(start = 24.dp))
                    AsyncImage(model = "https://images.saymedia-content.com/.image/ar_1:1%2Cc_fill%2Ccs_srgb%2Cq_auto:eco%2Cw_1200/MTk4MDQzMTI5NzY3NTM1ODA2/short-captions-for-profile-pictures.png",
                        contentDescription = "person", contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape))


                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text(text = "Jane Clark", color = Color.Black.copy(0.7f), fontWeight = FontWeight.SemiBold)
                        Text(text = "You have new message", color = Color.Black.copy(0.7f), fontSize = 14.sp)

                    }


                }
            }


            Alerter(isShown = showAlert3, onChanged = {showAlert3 = it},
                backgroundColor = Color.Transparent, enableInfiniteDuration = true
            ) {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .background(Color.Transparent), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Row(modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            Color(0xFFE90505),
                            shape = CircleShape
                        )
                        .padding(vertical = 8.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {

                        Icon(painter = painterResource(id = R.drawable.globe_world_icon), contentDescription = "",
                            tint = Color.White, modifier = Modifier.size(20.dp))
                        Text(text = "No connection!", color = Color.White, fontSize = 14.sp,
                            modifier = Modifier.padding(start = 12.dp))

                    }
                }

            }


            Alerter(isShown = showAlert4, onChanged = {showAlert4 = it},
                backgroundColor = Color.Transparent) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        Color(0xFF9499FF),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {

                    Spacer(modifier = Modifier.padding(start = 24.dp))
                    Icon(painter = painterResource(id = R.drawable.gift_icon), contentDescription = "",
                        tint = Color.Unspecified, modifier = Modifier.size(48.dp))

                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text(text = "Gift", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text(text = "Claim your gift!", color = Color.White, fontSize = 14.sp)

                    }


                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { showAlert4 = !showAlert4 },
                        shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4C52C7), contentColor = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp)) {
                        Text(text = "Claim")
                    }

                    Spacer(modifier = Modifier.padding(start = 24.dp))

                }
            }

            Alerter(isShown = showAlert5, onChanged = {showAlert5 = it},
                backgroundColor =  Color(0xFFE2E1E1), duration = 6000
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(imageVector = Icons.Rounded.NotificationsActive, contentDescription = "",
                        tint = Color.Black.copy(0.7f), modifier = Modifier.iconPulse())
                    Text(text = "You have new suggestions", color = Color.Black.copy(0.7f), fontSize = 14.sp)
                }
            }


            Alerter(isShown = showAlert6, onChanged = {showAlert6 = it},
                backgroundColor = Color.Transparent
            ) {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .background(Color.Transparent), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Row(modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            Color(0xFF2FE04D),
                            shape = CircleShape
                        )
                        .padding(vertical = 8.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {

                        Icon(painter = painterResource(id = R.drawable.globe_world_icon), contentDescription = "",
                            tint = Color.White, modifier = Modifier.size(20.dp))
                        Text(text = "Connection restored", color = Color.White, fontSize = 14.sp,
                            modifier = Modifier.padding(start = 12.dp))

                    }
                }

            }

            Alerter(isShown = showAlert7, onChanged = {showAlert7 = it},
                backgroundColor = Color(0xFFF69346)
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {

                    Spacer(modifier = Modifier.padding(start = 12.dp))

                    Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "",
                        tint = Color.White, modifier = Modifier.iconPulse())


                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text(text = "Alert Title", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text(text = "Alert text...", color = Color.White, fontSize = 14.sp)

                    }


                }
            }

            Button {
                showAlert1 = showAlert1.not()
            }

            Button {
                showAlert2 = showAlert2.not()
            }

            Button {
                showAlert3 = showAlert3.not()
            }

            Button {
                showAlert4 = showAlert4.not()
            }

            Button {
                showAlert5 = showAlert5.not()
            }

            Button {
                showAlert6 = showAlert6.not()
            }

            Button {
                showAlert7 = showAlert7.not()
            }


        }

    }
}

@Composable
fun Button(clicked:() -> Unit){

    Button(onClick = {
        clicked()
    },colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF0DA5EB), contentColor = Color.White
    ), shape = CircleShape
    ) {
        Text(text = "Alert", Modifier.padding(horizontal = 45.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlerterTheme {
        MyContent()
    }
}