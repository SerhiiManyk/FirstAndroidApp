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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Оберіть мову / Wybierz język", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { onLanguageSelected("uk") }) {
                Text("Українська 🇺🇦")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onLanguageSelected("pl") }) {
                Text("Polski 🇵🇱")
            }
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = if (language == "uk") "Оберіть тип" else "Wybierz typ",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { onSelect("pipe") }) {
                Text(if (language == "uk") "Труба" else "Rura")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onSelect("rect") }) {
                Text(if (language == "uk") "Канал" else "Kanał")
            }

            Spacer(modifier = Modifier.height(32.dp))

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

    val title = if (language == "uk") "Розрахунок труби" else "Obliczanie rury"
    val diameterHint = if (language == "uk") "Діаметр (мм)" else "Średnica (mm)"
    val insulationHint = if (language == "uk") "Утеплення (мм)" else "Izolacja (mm)"
    val calcText = if (language == "uk") "Розрахувати" else "Oblicz"
    val backText = if (language == "uk") "Назад" else "Powrót"

    Box(modifier = Modifier.fillMaxSize()) {

        FullScreenBackground()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.25f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(title, fontSize = 24.sp)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = diameter,
                onValueChange = { diameter = it },
                label = { Text(diameterHint) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = insulation,
                onValueChange = { insulation = it },
                label = { Text(insulationHint) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                val d = diameter.toDoubleOrNull()
                val t = insulation.toDoubleOrNull()

                if (d != null && t != null) {
                    val lengthMm = Calculator.calculatePipe(d, t)
                    val lengthCm = kotlin.math.ceil(lengthMm / 10.0).toInt()

                    val resultText = if (language == "uk") {
                        "Довжина: $lengthCm см"
                    } else {
                        "Długość: $lengthCm cm"
                    }

                    onResult(resultText)
                } else {
                    onResult(
                        if (language == "uk") "Невірні дані"
                        else "Błędne dane"
                    )
                }
            }) {
                Text(calcText)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onBack) {
                Text(backText)
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
    val widthHint = if (language == "uk") "Ширина (мм)" else "Szerokość (mm)"
    val heightHint = if (language == "uk") "Висота (мм)" else "Wysokość (mm)"
    val insulationHint = if (language == "uk") "Утеплення (мм)" else "Izolacja (mm)"
    val calcText = if (language == "uk") "Розрахувати" else "Oblicz"
    val backText = if (language == "uk") "Назад" else "Powrót"

    Box(modifier = Modifier.fillMaxSize()) {

        FullScreenBackground()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.25f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(title, fontSize = 24.sp)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = width,
                onValueChange = { width = it },
                label = { Text(widthHint) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text(heightHint) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = insulation,
                onValueChange = { insulation = it },
                label = { Text(insulationHint) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                val w = width.toDoubleOrNull()
                val h = height.toDoubleOrNull()
                val t = insulation.toDoubleOrNull()

                if (w != null && h != null && t != null) {
                    val lengthMm = Calculator.calculateRect(w, h, t)
                    val lengthCm = kotlin.math.ceil(lengthMm / 10.0).toInt()

                    val resultText = if (language == "uk") {
                        "Довжина: $lengthCm см"
                    } else {
                        "Długość: $lengthCm cm"
                    }

                    onResult(resultText)
                } else {
                    onResult(
                        if (language == "uk") "Невірні дані"
                        else "Błędne dane"
                    )
                }
            }) {
                Text(calcText)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onBack) {
                Text(backText)
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
        contentScale = ContentScale.Crop
    )
}