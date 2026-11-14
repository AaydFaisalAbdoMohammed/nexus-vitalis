package app.nexusvitalis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Background = Color(0xFF081116)
private val Panel = Color(0xFF111F26)
private val PanelLight = Color(0xFF1A2B32)
private val Accent = Color(0xFF55E0BD)
private val MainText = Color(0xFFF1F7F5)
private val MutedText = Color(0xFF91A7A5)
private val Warning = Color(0xFFF1C477)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.rgb(8, 17, 22)
        window.navigationBarColor = android.graphics.Color.rgb(8, 17, 22)

        setContent {
            VitalisTheme {
                VitalisDashboard()
            }
        }
    }
}

@Composable
private fun VitalisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Accent,
            onPrimary = Background,
            background = Background,
            surface = Panel,
            onSurface = MainText,
            secondary = MutedText
        ),
        content = content
    )
}

@Composable
private fun VitalisDashboard() {
    var selectedMinutes by rememberSaveable { mutableIntStateOf(25) }
    var remainingSeconds by rememberSaveable { mutableIntStateOf(25 * 60) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var completedSessions by rememberSaveable { mutableIntStateOf(0) }
    var focusMinutesToday by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(isRunning, remainingSeconds) {
        if (isRunning && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        } else if (isRunning && remainingSeconds == 0) {
            isRunning = false
            completedSessions++
            focusMinutesToday += selectedMinutes
        }
    }

    val totalSeconds = selectedMinutes * 60
    val progress = remainingSeconds.toFloat() / totalSeconds.toFloat()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(top = 24.dp, bottom = 28.dp)
        ) {
            Header()

            Spacer(Modifier.height(30.dp))

            Text(
                text = "YOUR SPACE. YOUR PACE.",
                color = Accent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Focus on what\\nmatters.",
                color = MainText,
                fontSize = 32.sp,
                lineHeight = 39.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(25.dp))

            FocusCard(
                selectedMinutes = selectedMinutes,
                remainingSeconds = remainingSeconds,
                progress = progress,
                isRunning = isRunning,
                onStartPause = {
                    if (remainingSeconds == 0) {
                        remainingSeconds = selectedMinutes * 60
                    }
                    isRunning = !isRunning
                },
                onReset = {
                    isRunning = false
                    remainingSeconds = selectedMinutes * 60
                },
                onSelectDuration = { minutes ->
                    isRunning = false
                    selectedMinutes = minutes
                    remainingSeconds = minutes * 60
                }
            )

            Spacer(Modifier.height(22.dp))

            Text(
                text = "YOUR PROGRESS",
                color = MutedText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = focusMinutesToday.toString(),
                    unit = "MIN",
                    label = "Focus time"
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    value = completedSessions.toString(),
                    unit = "SESSIONS",
                    label = "Completed"
                )
            }

            Spacer(Modifier.height(22.dp))

            PrivacyCard()

            Spacer(Modifier.height(20.dp))

            Text(
                text = "NEXUS VITALIS  •  FOUNDATION MVP 0.2.0",
                modifier = Modifier.fillMaxWidth(),
                color = MutedText,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(43.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(PanelLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "N",
                color = Accent,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = "NEXUS VITALIS",
                color = MainText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "ADAPTIVE FOCUS COMPANION",
                color = MutedText,
                fontSize = 9.sp,
                letterSpacing = 1.sp
            )
        }

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Panel)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Accent)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = "READY",
                    color = Accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FocusCard(
    selectedMinutes: Int,
    remainingSeconds: Int,
    progress: Float,
    isRunning: Boolean,
    onStartPause: () -> Unit,
    onReset: () -> Unit,
    onSelectDuration: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Panel)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "FOCUS SESSION",
            color = MutedText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )

        Spacer(Modifier.height(22.dp))

        Box(
            modifier = Modifier.size(218.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = PanelLight,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 11.dp.toPx())
                )

                drawArc(
                    color = Accent,
                    startAngle = -90f,
                    sweepAngle = 360f * progress.coerceIn(0f, 1f),
                    useCenter = false,
                    style = Stroke(
                        width = 11.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatTime(remainingSeconds),
                    color = MainText,
                    fontSize = 43.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = if (isRunning) "SESSION ACTIVE" else "TIME TO FOCUS",
                    color = Accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        Text(
            text = "SESSION DURATION",
            color = MutedText,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(15, 25, 45).forEach { minutes ->
                val selected = selectedMinutes == minutes

                Button(
                    onClick = { onSelectDuration(minutes) },
                    shape = RoundedCornerShape(13.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) Accent else PanelLight,
                        contentColor = if (selected) Background else MainText
                    )
                ) {
                    Text(
                        text = "${minutes}m",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onStartPause,
                modifier = Modifier.weight(1f).height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = Background
                )
            ) {
                Text(
                    text = if (isRunning) "PAUSE SESSION" else "START FOCUS",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = onReset,
                modifier = Modifier.height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PanelLight,
                    contentColor = MainText
                )
            ) {
                Text("RESET", fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    value: String,
    unit: String,
    label: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Panel)
            .padding(16.dp)
    ) {
        Text(
            text = label,
            color = MutedText,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                color = MainText,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = unit,
                color = Accent,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
    }
}

@Composable
private fun PrivacyCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(PanelLight)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(39.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Panel),
            contentAlignment = Alignment.Center
        ) {
            Text("◆", color = Accent, fontSize = 17.sp)
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Privacy comes first",
                color = MainText,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "No sensor collection is active in this build.",
                color = MutedText,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remaining = seconds % 60
    return "%02d:%02d".format(minutes, remaining)
}
