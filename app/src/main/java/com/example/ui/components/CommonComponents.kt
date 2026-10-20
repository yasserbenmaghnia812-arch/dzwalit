package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.strings.Strings
import com.example.ui.theme.DzBlack
import com.example.ui.theme.DzCardBackground
import com.example.ui.theme.DzCardBorder
import com.example.ui.theme.DzCardElevated
import com.example.ui.theme.DzDarkBackground
import com.example.ui.theme.DzGreenBadge
import com.example.ui.theme.DzGreenIncome
import com.example.ui.theme.DzRedExpense
import com.example.ui.theme.DzTextMuted
import com.example.ui.theme.DzTextPrimary
import com.example.ui.theme.DzTextSecondary
import com.example.ui.theme.DzWhite

@Composable
fun DzPillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    backgroundColor: Color = DzWhite,
    contentColor: Color = DzBlack
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f))
            .clickable(enabled = enabled && !isLoading, onClick = onClick)
            .testTag("pill_button_${text.take(10)}"),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = contentColor,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = text,
                color = contentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DzTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (onBackClick != null) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DzCardBackground)
                    .border(1.dp, DzCardBorder, CircleShape)
                    .testTag("top_bar_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = DzTextPrimary
                )
            }
        } else {
            Spacer(modifier = Modifier.size(40.dp))
        }

        Text(
            text = title,
            color = DzTextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
            actions?.invoke()
        }
    }
}

@Composable
fun DzBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    strings: Strings
) {
    NavigationBar(
        containerColor = DzCardBackground,
        tonalElevation = 8.dp,
        modifier = Modifier
            .border(1.dp, DzCardBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        val items = listOf(
            Triple(0, strings.home, Icons.Default.Home),
            Triple(1, strings.globalShopping, Icons.Default.ShoppingBag),
            Triple(2, strings.cards, Icons.Default.CreditCard),
            Triple(3, strings.more, Icons.Default.MoreHoriz)
        )

        items.forEach { (index, title, icon) ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DzWhite,
                    selectedTextColor = DzWhite,
                    indicatorColor = DzCardElevated,
                    unselectedIconColor = DzTextSecondary,
                    unselectedTextColor = DzTextSecondary
                ),
                modifier = Modifier.testTag("nav_tab_$index")
            )
        }
    }
}

@Composable
fun DzRoundActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).testTag("action_$label")
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(DzCardBackground)
                .border(1.dp, DzCardBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = DzTextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = DzTextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun DzVirtualCard(
    cardHolder: String,
    last4: String,
    expiry: String = "12/28",
    status: String = "active",
    availableBalance: Double = 1245.32,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1F1F26),
                        Color(0xFF121215),
                        Color(0xFF0D0D10)
                    )
                )
            )
            .border(1.dp, Color(0xFF33333D), RoundedCornerShape(20.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(20.dp)
    ) {
        // Metallic card chip and branding
        Column(
            modifier = Modifier.matchParentSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(DzWhite),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("D", color = DzBlack, fontWeight = FontWeight.Black, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DZ Wallet",
                        color = DzWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                // Status chip
                val isActive = status == "active"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isActive) DzGreenBadge else Color(0xFF4A1515))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isActive) "صالحة" else "مجمّدة",
                        color = if (isActive) DzGreenIncome else DzRedExpense,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Card Chip Graphic
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(36.dp, 28.dp)) {
                    drawRoundRect(
                        color = Color(0xFFD4AF37),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = Color(0xFFB8860B),
                        topLeft = Offset(4f, 4f),
                        size = Size(size.width - 8f, size.height - 8f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                // Contactless Icon Waves
                Text(
                    text = "))))",
                    color = DzTextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Card Number and VISA Logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "••••  ••••  ••••  $last4",
                        color = DzWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = cardHolder.uppercase(),
                            color = DzTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = expiry,
                            color = DzTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Bold VISA text styling
                Text(
                    text = "VISA",
                    color = DzWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun DzQrCodeView(
    data: String,
    modifier: Modifier = Modifier,
    size: Dp = 220.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(16.dp))
            .background(DzWhite)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size - 32.dp)) {
            val grid = 21
            val cellSize = this.size.width / grid
            // Draw deterministic pseudo-random QR pattern matching the data hash
            val hash = data.hashCode()
            for (r in 0 until grid) {
                for (c in 0 until grid) {
                    val isCornerFinder = (r < 7 && c < 7) || (r < 7 && c >= grid - 7) || (r >= grid - 7 && c < 7)
                    val drawCell: Boolean
                    if (isCornerFinder) {
                        val inOuter = (r == 0 || r == 6 || c == 0 || c == 6) ||
                                (r == 0 || r == 6 || c == grid - 7 || c == grid - 1) ||
                                (r == grid - 7 || r == grid - 1 || c == 0 || c == 6)
                        val inCenter = (r in 2..4 && c in 2..4) ||
                                (r in 2..4 && c in (grid - 5)..(grid - 3)) ||
                                (r in (grid - 5)..(grid - 3) && c in 2..4)
                        drawCell = inOuter || inCenter
                    } else {
                        val bit = ((r * 31 + c * 17) xor hash) % 3 == 0
                        drawCell = bit
                    }
                    if (drawCell) {
                        drawRect(
                            color = DzBlack,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DzDottedGlobe(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(160.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 2 * 0.9f

        // Draw dotted latitude/longitude sphere lines
        drawCircle(
            color = Color(0xFF1E2028),
            radius = radius,
            center = center
        )

        for (angle in 0 until 360 step 15) {
            val rad = Math.toRadians(angle.toDouble())
            val x = center.x + radius * Math.cos(rad).toFloat()
            val y = center.y + radius * Math.sin(rad).toFloat()
            drawCircle(
                color = DzWhite.copy(alpha = 0.4f),
                radius = 2f,
                center = Offset(x, y)
            )
        }

        // Inner contour rings
        drawOval(
            color = DzWhite.copy(alpha = 0.25f),
            topLeft = Offset(center.x - radius * 0.7f, center.y - radius),
            size = Size(radius * 1.4f, radius * 2),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
        )
        drawOval(
            color = DzWhite.copy(alpha = 0.25f),
            topLeft = Offset(center.x - radius, center.y - radius * 0.5f),
            size = Size(radius * 2, radius),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
        )
    }
}
