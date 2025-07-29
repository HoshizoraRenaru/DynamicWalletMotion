package org.hoshizora.dynamicwalletmotion.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.hoshizora.dynamicwalletmotion.R


@Composable
fun SuicaAnimationOverlay(
    visible: Boolean,
    onDismiss: () -> Unit,
) {
    if (!visible) return

    var phase by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(500)
        phase = 1
        delay(2000)
        phase = 2
        delay(2000)
        phase = 3
        delay(800)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 2.8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedSuicaIsland(phase = phase)
    }
}

@Composable
fun AnimatedSuicaIsland(phase: Int) {
    val baseHeightSmall = 37.5.dp  // normal Dynamic Island
    val smallWidth = 120.dp
    val bigWidth = 220.dp
    val longWidth = 250.dp
    val bigHeight = 140.dp

    val cornerRadius by animateDpAsState(
        targetValue = if (phase == 1) 70.dp else 20.dp,
        animationSpec = tween(durationMillis = 800)
    )

    val animWidth by animateDpAsState(
        targetValue = when (phase) {
            0 -> smallWidth
            1 -> bigWidth
            2 -> longWidth
            3 -> smallWidth
            else -> smallWidth
        },
        animationSpec = tween(durationMillis = 800)
    )

    val animHeight by animateDpAsState(
        targetValue = when (phase) {
            0, 2, 3 -> baseHeightSmall
            1 -> bigHeight
            else -> baseHeightSmall
        },
        animationSpec = tween(durationMillis = 800)
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (phase == 3) 0f else 1f,
        animationSpec = tween(durationMillis = 800)
    )

    Box(
        Modifier
            .size(width = animWidth, height = animHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when (phase) {
            1 -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp, bottom = 10.dp)
                        .alpha(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_suica_logo),
                        contentDescription = "Suica Logo",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            2 -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .alpha(contentAlpha),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_suica_logo),
                        contentDescription = "Suica Logo",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        "完了",
                        color = Color.White.copy(alpha = contentAlpha),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alpha(contentAlpha)
                    )
                }
            }
            3 -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                        .alpha(contentAlpha),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_suica_logo),
                        contentDescription = "Suica Logo",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        "完了",
                        color = Color.White.copy(alpha = contentAlpha),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alpha(contentAlpha)
                    )
                }
            }
            else -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_suica_logo),
                        contentDescription = "Suica Logo",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
