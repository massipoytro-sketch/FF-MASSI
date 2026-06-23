package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme

data class AppStrings(
    val title: String,
    val inputHint: String,
    val generateButton: String,
    val emptyWarning: String,
    val generatedCount: String,
    val copySuccess: String,
    val copyDesc: String,
    val shareDesc: String,
    val clearDesc: String
)

val arStrings = AppStrings(
    title = "مولد أسماء فري فاير",
    inputHint = "أدخل اسمك هنا",
    generateButton = "زخرف الاسم",
    emptyWarning = "الرجاء إدخال اسم",
    generatedCount = "تم توليد %d اسم",
    copySuccess = "تم النسخ بنجاح",
    copyDesc = "نسخ",
    shareDesc = "مشاركة",
    clearDesc = "مسح"
)

val enStrings = AppStrings(
    title = "FF Name Generator",
    inputHint = "Enter your name here",
    generateButton = "Generate Names",
    emptyWarning = "Please enter a name",
    generatedCount = "Generated %d names",
    copySuccess = "Copied successfully",
    copyDesc = "Copy",
    shareDesc = "Share",
    clearDesc = "Clear"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }
    var generatedNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var isEnglish by remember { mutableStateOf(false) }
    val strings = if (isEnglish) enStrings else arStrings

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { isEnglish = !isEnglish }) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Switch Language",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = strings.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
            )
            IconButton(onClick = {
                inputText = ""
                generatedNames = emptyList()
            }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = strings.clearDesc,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text(strings.inputHint) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                if (inputText.isNotBlank()) {
                    generatedNames = NameGenerator.generateNames(inputText.trim(), 200)
                }
            }),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (inputText.isNotBlank()) {
                    generatedNames = NameGenerator.generateNames(inputText.trim(), 200)
                    focusManager.clearFocus()
                } else {
                    Toast.makeText(context, strings.emptyWarning, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = strings.generateButton,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = generatedNames.isNotEmpty(),
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            Text(
                text = strings.generatedCount.format(generatedNames.size),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(generatedNames) { name ->
                NameCard(name = name, strings = strings)
            }
        }
    }
}

@Composable
fun NameCard(name: String, strings: AppStrings) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                copyToClipboard(context, name, strings.copySuccess)
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                textAlign = if (name.any { it in 'a'..'z' || it in 'A'..'Z' }) TextAlign.Start else TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Row {
                IconButton(onClick = { copyToClipboard(context, name, strings.copySuccess) }) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = strings.copyDesc,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { shareText(context, name, strings.shareDesc) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = strings.shareDesc,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

fun copyToClipboard(context: Context, text: String, successMsg: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("زخرفة", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
}

fun shareText(context: Context, text: String, title: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, title))
}
