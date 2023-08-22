## Alerter - An Android Alerter Library, now in Jatpack Compose!


This library aims to overcome the limitations of Toasts and Snackbars, while reducing the
complexity of your layouts.

This library originated from [Tapado Alerter](https://github.com/Tapadoo/Alerter) and modified to make proper usage for **Jatpack Compose**.


![Header](https://github.com/akardas16/Alerter/assets/28716129/d7ed8e90-b455-42a8-82e1-bd964c859858)

## General

With simplicity in mind, the Alerter employs the builder pattern to facilitate easy integration into any app.
A customisable Alert View is dynamically added to the Decor View of the Window, overlaying all content. 

## Install

Include the JitPack.io Maven repo in your project's build.gradle file

```groovy
allprojects {
 repositories {
    maven { url "https://jitpack.io" }
 }
}
```

Then add this dependency to your app's build.gradle file

```groovy
dependencies {
    implementation 'com.github.akardas16:Alerter:1.0.5'
}
```

# Usage

 * Use `modifier = Modifier.iconPulse()` for icon pulse effect 
  <p align="center">
   Alerter with icon, title and message
  </p>
 <p align="center">
 <img align="center" src="https://github.com/akardas16/Alerter/assets/28716129/7e036b7f-b024-44af-b8ac-0d5c3a8cd240" width="400">
</p>

 

```kotlin
 var showAlert by remember { mutableStateOf(false) }

 Alerter(isShown = showAlert, onChanged = {showAlert = it},
                backgroundColor = Color(0xFFF69346)) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically) {

                    Icon(imageVector = Icons.Rounded.Notifications, contentDescription = "",
                        tint = Color.White, modifier = Modifier.padding(start = 12.dp).iconPulse())

                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text(text = "Alert Title", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text(text = "Alert text...", color = Color.White, fontSize = 14.sp)

                    }
                }
            }
```



<br />
<br />

 * `backgroundColor = Color.Transparent` will seperate UI from status bar.
 * By default value is  `backgroundColor = Color.Transparent` (see below example)
  <p align="center">
   Alerter with Coil image library
  </p>
 <p align="center">
 <img align="center" src="https://github.com/akardas16/Alerter/assets/28716129/124029d2-7f16-48d0-b6eb-2c0e271fd7d4" width="400">
</p>

 

```kotlin
 var showAlert by remember { mutableStateOf(false) }

 Alerter(isShown = showAlert, onChanged = { showAlert = it }, backgroundColor = Color.Transparent) {

                Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()
                        .background(Color(0xFFE2E1E1),shape = RoundedCornerShape(15.dp))
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {

                    //Coil
                    AsyncImage(model = "image url",
                        contentDescription = "person", contentScale = ContentScale.Crop,
                        modifier = Modifier.padding(start = 24.dp)
                            .size(48.dp).clip(CircleShape))

                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {

                        Text(text = "Jane Clark",
                            color = Color.Black.copy(0.7f), fontWeight = FontWeight.SemiBold)

                        Text(text = "You have new message",
                            color = Color.Black.copy(0.7f), fontSize = 14.sp)

                    }

                }
            }
```

<br />
<br />



 <p align="center">
 <img align="center" src="https://github.com/akardas16/Alerter/assets/28716129/1f49c596-ee0d-4df8-9181-2229c8472a4c" width="400">
</p>



```kotlin
 var showAlert by remember { mutableStateOf(false) }

 Alerter(isShown = showAlert, onChanged = {showAlert = it}, backgroundColor = Color.Transparent) {

                Row(modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .background(Color(0xFF9499FF), shape = RoundedCornerShape(18.dp))
                    .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {

                    Icon(painter = painterResource(id = R.drawable.gift_icon), contentDescription = "",
                        tint = Color.Unspecified, modifier = Modifier.padding(start = 24.dp).size(48.dp))

                    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text(text = "Gift", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text(text = "Claim your gift!", color = Color.White, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { showAlert4 = !showAlert4 },
                        shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4C52C7), contentColor = Color.White),
                        modifier = Modifier.padding(end = 24.dp)) {
                        Text(text = "Claim")
                    }
                }
            }
```



