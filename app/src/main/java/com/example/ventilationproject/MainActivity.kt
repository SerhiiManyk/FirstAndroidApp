package com.example.ventilationproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ventilationproject.Calculator
import com.example.ventilationproject.ui.theme.VentilationProjectTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.ventilationproject.R

const val SCREEN_LANGUAGE = "language"
const val SCREEN_TYPE = "type"
const val SCREEN_PIPE = "pipe"
const val SCREEN_RECT = "rect"
const val SCREEN_RESULT = "result"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VentilationProjectTheme {

                var selectedLanguage by remember { mutableStateOf("uk") }
                var currentScreen by remember { mutableStateOf(SCREEN_LANGUAGE) }
                var resultValue by remember { mutableStateOf("") }

                when (currentScreen) {

                    SCREEN_LANGUAGE -> LanguageSelectionScreen { lang ->
                        selectedLanguage = lang
                        currentScreen = SCREEN_TYPE
                    }

                    SCREEN_TYPE -> TypeSelectionScreen(
                        language = selectedLanguage,
                        onBack = { currentScreen = SCREEN_LANGUAGE },
                        onSelect = { type ->
                            currentScreen = when (type) {
                                "pipe" -> SCREEN_PIPE
                                "rect" -> SCREEN_RECT
                                else -> SCREEN_TYPE
                            }
                        }
                    )

                    SCREEN_PIPE -> PipeScreen(
                        language = selectedLanguage,
                        onBack = { currentScreen = SCREEN_TYPE },
                        onResult = { value ->
                            resultValue = value
                            currentScreen = SCREEN_RESULT
                        }
                    )

                    SCREEN_RECT -> RectScreen(
                        language = selectedLanguage,
                        onBack = { currentScreen = SCREEN_TYPE },
                        onResult = { value ->
                            resultValue = value
                            currentScreen = SCREEN_RESULT
                        }
                    )

                    SCREEN_RESULT -> ResultScreen(
                        language = selectedLanguage,
                        result = resultValue,
                        onBack = { currentScreen = SCREEN_TYPE }
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionScreen(onLanguageSelected: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        FullScreenBackground()
        Scrim()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.top_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Оберіть мову / Wybierz język",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { onLanguageSelected("uk") }) {
                Text("Українська 🇺🇦")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onLanguageSelected("pl") }) {
                Text("Polski 🇵🇱")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun MainScreen(language: String) {
    val text = when (language) {
        "uk" -> "Головний екран (українська)"
        "pl" -> "Ekran główny (polski)"
        else -> "Main screen"
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 24.sp)
    }
}

@Composable
fun TypeSelectionScreen(
    language: String,
    onBack: () -> Unit,
    onSelect: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        FullScreenBackground()
        Scrim()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 🔥 ДОДАНА ЕМБЛЕМА
            Image(
                painter = painterResource(id = R.drawable.top_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (language == "uk") "Оберіть тип" else "Wybierz typ",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            TypeCard(
                imageRes = R.drawable.rura,
                text = if (language == "uk") "Труба" else "Rura"
            ) { onSelect("pipe") }

            TypeCard(
                imageRes = R.drawable.kanal,
                text = if (language == "uk") "Канал" else "Kanał"
            ) { onSelect("rect") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onBack) {
                Text(if (language == "uk") "Назад" else "Powrót")
            }
        }
    }
}

@Composable
fun PipeScreen(
    language: String,
    onBack: () -> Unit,
    onResult: (String) -> Unit
) {
    var diameter by remember { mutableStateOf("") }
    var insulation by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        FullScreenBackground()
        Scrim()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // 🔥 ЕМБЛЕМА ВГОРІ
            Image(
                painter = painterResource(id = R.drawable.top_image),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(72.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == "uk") "Розрахунок труби" else "Obliczanie rury",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔥 ЧИТАБЕЛЬНИЙ INPUT (білий фон)
            OutlinedTextField(
                value = diameter,
                onValueChange = { diameter = it },
                label = { Text(if (language == "uk") "Діаметр (мм)" else "Średnica (mm)") },
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    focusedTextColor = androidx.compose.ui.graphics.Color.Black,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.Black
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = insulation,
                onValueChange = { insulation = it },
                label = { Text(if (language == "uk") "Утеплення (мм)" else "Izolacja (mm)") },
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    focusedTextColor = androidx.compose.ui.graphics.Color.Black,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.Black
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                val d = diameter.toDoubleOrNull()
                val t = insulation.toDoubleOrNull()

                if (d != null && t != null) {
                    val lengthMm = Calculator.calculatePipe(d, t)
                    val lengthCm = kotlin.math.ceil(lengthMm / 10.0).toInt()

                    onResult(
                        if (language == "uk") "Довжина: $lengthCm см"
                        else "Długość: $lengthCm cm"
                    )
                } else {
                    onResult(
                        if (language == "uk") "Невірні дані"
                        else "Błędne dane"
                    )
                }
            }) {
                Text(if (language == "uk") "Розрахувати" else "Oblicz")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onBack) {
                Text(if (language == "uk") "Назад" else "Powrót")
            }
        }
    }
}

@Composable
fun RectScreen(
    language: String,
    onBack: () -> Unit,
    onResult: (String) -> Unit
) {
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var insulation by remember { mutableStateOf("") }

    val title = if (language == "uk") "Розрахунок каналу" else "Obliczanie kanału"

    val widthLabel = if (language == "uk") "Ширина (мм)" else "Szerokość (mm)"
    val heightLabel = if (language == "uk") "Висота (мм)" else "Wysokość (mm)"
    val insulationLabel = if (language == "uk") "Товщина утеплення (мм)" else "Grubość izolacji (mm)"

    Box(modifier = Modifier.fillMaxSize()) {

        FullScreenBackground()
        Scrim()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // 🔥 ЕМБЛЕМА
            Image(
                painter = painterResource(id = R.drawable.top_image),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(72.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = width,
                onValueChange = { width = it },
                label = { Text(widthLabel) },
                singleLine = true,
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    focusedTextColor = androidx.compose.ui.graphics.Color.Black,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text(heightLabel) },
                singleLine = true,
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    focusedTextColor = androidx.compose.ui.graphics.Color.Black,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.Black
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = insulation,
                onValueChange = { insulation = it },
                label = { Text(insulationLabel) },
                singleLine = true,
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color.White,
                    focusedTextColor = androidx.compose.ui.graphics.Color.Black,
                    unfocusedTextColor = androidx.compose.ui.graphics.Color.Black
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                val w = width.toDoubleOrNull()
                val h = height.toDoubleOrNull()
                val t = insulation.toDoubleOrNull()

                if (w != null && h != null && t != null) {
                    val lengthMm = Calculator.calculateRect(w, h, t)
                    val lengthCm = kotlin.math.ceil(lengthMm / 10.0).toInt()

                    onResult(
                        if (language == "uk") "Довжина: $lengthCm см"
                        else "Długość: $lengthCm cm"
                    )
                } else {
                    onResult(
                        if (language == "uk") "Невірні дані"
                        else "Błędne dane"
                    )
                }
            }) {
                Text(if (language == "uk") "Розрахувати" else "Oblicz")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onBack) {
                Text(if (language == "uk") "Назад" else "Powrót")
            }
        }
    }
}

@Composable
fun ResultScreen(
    language: String,
    result: String,
    onBack: () -> Unit
) {
    val thankYouText = if (language == "uk") {
        "Дякуємо!\nФірма може на тебе розраховувати"
    } else {
        "Dziękujemy!\nFirma może na Ciebie liczyć"
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top   // 🔥 ТУТ ГОЛОВНА ЗМІНА
        ) {

            Spacer(modifier = Modifier.height(40.dp)) // 🔥 підняли ще вище

            Text(
                text = thankYouText,
                fontSize = 30.sp   // 🔥 БІЛЬШИЙ ТЕКСТ
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = result,
                fontSize = 24.sp   // 🔥 БІЛЬШИЙ РЕЗУЛЬТАТ
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onBack) {
                Text(if (language == "uk") "Назад" else "Powrót")
            }
        }
    }
}

@Composable
fun TypeCard(
    imageRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.7f) // 70% ширини
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = text,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun FullScreenBackground() {
    Image(
        painter = painterResource(id = R.drawable.ventilation_bg),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.35f // 🔥 прозорість фону
    )
}

@Composable
fun Scrim() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.45f)
            )
    )
}

@Composable
fun TopImage() {
    Image(
        painter = painterResource(id = R.drawable.top_image),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp), // 🔥 як кнопка/невеликий блок
        contentScale = ContentScale.Fit
    )
}