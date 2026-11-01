package com.example.sakhiai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import java.util.Locale

data class ChatMessage(val text: String, val fromUser: Boolean)

class MainActivity : ComponentActivity() {
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("hi", "IN")
            }
        }
        setContent { SakhiApp(onSpeak = { speak(it) }) }
    }

    private fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "sakhi-utterance")
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}

@Composable
fun SakhiApp(onSpeak: (String) -> Unit) {
    val repo = remember { AiRepository() }
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var listening by remember { mutableStateOf(false) }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("नमस्ते! मैं सखी हूँ 💖 तुम्हारी friendly learning साथी। आज क्या बात करें?", false)
        )
    }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        listening = false
        val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
        if (!spoken.isNullOrBlank()) input = spoken
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFB83280),
            secondary = Color(0xFF7C4DFF),
            background = Color(0xFFFFF7FC),
            surface = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFFFFE6F4), Color(0xFFF5F0FF))))
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Brush.linearGradient(listOf(Color(0xFFFF79B7), Color(0xFF8C72FF))), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("🌸", fontSize = 32.sp) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("सखी AI", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7A245B))
                    Text("तुम्हारी anime learning buddy", fontSize = 13.sp, color = Color(0xFF6E5570))
                }
                Text("●", color = Color(0xFF239B68), fontSize = 14.sp)
            }
            Spacer(Modifier.height(14.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.86f)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("आज क्या सीखें? ✨", fontWeight = FontWeight.Bold, color = Color(0xFF7A245B))
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Suggestion("📚 पढ़ाई") { input = "मुझे पढ़ाई में मदद करो" }
                        Suggestion("➗ गणित") { input = "मुझे गणित सिखाओ" }
                        Suggestion("🇬🇧 English") { input = "मुझे आसान English सिखाओ" }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(messages) { message ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (message.fromUser) Color(0xFFB83280) else Color.White,
                            shape = RoundedCornerShape(18.dp),
                            shadowElevation = 1.dp,
                            modifier = Modifier.widthIn(max = 310.dp)
                        ) {
                            Text(
                                message.text,
                                modifier = Modifier.padding(13.dp),
                                color = if (message.fromUser) Color.White else Color(0xFF352638),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
                if (busy) item { Text("सखी सोच रही है… ✨", color = Color(0xFF7A245B)) }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = {
                        listening = true
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "सखी से बात करें")
                        }
                        try { speechLauncher.launch(intent) } catch (_: Exception) { listening = false }
                    },
                    contentPadding = PaddingValues(horizontal = 13.dp, vertical = 15.dp)
                ) { Text(if (listening) "🎙️…" else "🎙️") }

                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("यहाँ लिखें…") },
                    shape = RoundedCornerShape(18.dp),
                    maxLines = 3
                )
                Button(
                    enabled = input.isNotBlank() && !busy,
                    onClick = {
                        val question = input.trim()
                        messages.add(ChatMessage(question, true))
                        input = ""
                        busy = true
                        scope.launch {
                            val answer = repo.reply(question)
                            messages.add(ChatMessage(answer, false))
                            busy = false
                            onSpeak(answer)
                        }
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 17.dp)
                ) { Text("➤") }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "सुरक्षा: निजी जानकारी साझा न करें। परेशानी या खतरे में भरोसेमंद बड़े से बात करें।",
                fontSize = 11.sp,
                color = Color(0xFF6E5570),
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun Suggestion(label: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
    ) { Text(label, fontSize = 11.sp) }
}
