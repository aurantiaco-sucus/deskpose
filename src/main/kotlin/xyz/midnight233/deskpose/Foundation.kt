package xyz.midnight233.deskpose

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*

@Composable
private fun DeskposeClickable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor
    ) {
        Box(
            modifier = modifier
                .clip(shape)
                .background(color)
                .border(border ?: BorderStroke(0.dp, Color.Transparent), shape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    enabled = enabled,
                    onClick = onClick
                )
        ) {
            content()
        }
    }
}

@Composable
private fun DeskposeUnclickable(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor
    ) {
        Box(
            modifier = modifier
                .clip(shape)
                .background(color)
                .border(border ?: BorderStroke(0.dp, Color.Transparent), shape)
                .semantics(mergeDescendants = false) {
                    isContainer = true
                }
                .pointerInput(Unit) {}
        ) {
            content()
        }
    }
}

@Composable
fun Button(caption: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DeskposeClickable(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Color(0xFFE8E8E8),
        contentColor = Color.Black,
        modifier = modifier
    ) {
        Text(
            text = caption,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun TextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(if (isFocused) Color(0xFF808080) else Color(0xFFE8E8E8))
    DeskposeUnclickable(
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 1.dp, color = borderColor),
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            interactionSource = interactionSource,
            textStyle = TextStyle(
                fontSize = 14.sp,
            ),
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun DeskposeBooleanButton(
    caption: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    graphic: DrawScope.(Float, Color) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val dotColor by animateColorAsState(if (selected) Color.Black else Color.Transparent)
    DeskposeClickable(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Canvas(Modifier.size(16.dp)) {
                this@Canvas.graphic(8f, dotColor)
            }
            Spacer(Modifier.width(4.dp))
            Text(
                text = caption,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun RadioButton(caption: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DeskposeBooleanButton(caption, selected, onClick, modifier) { radius, activeColor ->
        drawCircle(
            color = Color(0xFFD0D0D0),
            radius = radius,
            center = Offset(8f, 8f),
            style = Stroke(width = 2f)
        )
        drawCircle(
            color = activeColor,
            radius = radius / 2,
            center = Offset(8f, 8f),
            style = Fill
        )
    }
}

private fun deskposeCheckButtonTickPath(origin: Float, radius: Float) = Path().run {
    val arg1 = 1f
    val arg2 = 1f
    moveTo(origin, origin + arg1 * radius)
    lineTo(origin + arg2 * radius, origin + 2f * radius)
    lineTo(origin + 2f * radius, origin)
    return@run this
}

@Composable
fun CheckButton(caption: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DeskposeBooleanButton(caption, selected, onClick, modifier) { radius, activeColor ->
        drawRect(
            color = Color(0xFFD0D0D0),
            topLeft = Offset.Zero,
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = 2f)
        )
        drawPath(
            path = deskposeCheckButtonTickPath(4f, radius - 4f),
            color = activeColor,
            style = Stroke(width = 2f)
        )
    }
}

@Composable
fun TristateCheckButton(
    caption: String,
    state: ToggleableState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val dotColor by animateColorAsState(
        if (state == ToggleableState.On || state == ToggleableState.Indeterminate)
            Color.Black else Color.Transparent)
    DeskposeClickable(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Canvas(Modifier.size(16.dp)) {
                drawRect(
                    color = Color(0xFFD0D0D0),
                    topLeft = Offset.Zero,
                    size = Size(16f, 16f),
                    style = Stroke(width = 2f)
                )
                if (state == ToggleableState.On) {
                    drawPath(
                        path = deskposeCheckButtonTickPath(4f, 4f),
                        color = dotColor,
                        style = Stroke(width = 2f)
                    )
                }
                if (state == ToggleableState.Indeterminate) {
                    drawRect(
                        topLeft = Offset(4f, 4f),
                        size = Size(8f, 8f),
                        color = dotColor,
                        style = Fill
                    )
                }
            }
            Spacer(Modifier.width(4.dp))
            Text(
                text = caption,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ComboButton(items: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    var isPopping by remember { mutableStateOf(false) }
    val ratio by animateFloatAsState(if (isPopping) 1f else 0f)
    DeskposeUnclickable(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color(0xFFD0D0D0)),
        contentColor = Color.Black,
        modifier = modifier
    ) {
        Column {
            DeskposeClickable(
                onClick = {
                    isPopping = !isPopping
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = items[selectedIndex],
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "dropdown indicator",
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .rotate(ratio * 180f)
                    )
                }
            }
            AnimatedVisibility(visible = isPopping) {
                Column {
                    items.indices.forEach {
                        DeskposeClickable(
                            onClick = {
                                onSelect(it)
                                isPopping = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Transparent
                        ) {
                            Text(
                                text = items[it],
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HintDot(activated: Boolean, color: Color, caption: String) {
    val mainColor by animateColorAsState(if (activated) color else Color(0xFF808080))
    val ratio by animateFloatAsState(if (activated) 1f else 0f)
    val stroke by animateDpAsState(if (activated) 2.dp else 5.dp)
    DeskposeUnclickable(
        shape = RoundedCornerShape(50),
        border = BorderStroke(stroke, mainColor),
        modifier = Modifier
            .padding(start = ((1 - ratio) * 6f).dp)
            .defaultMinSize(10.dp, 10.dp)
    ) {
        AnimatedVisibility(visible = activated) {
            Text(
                text = caption,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}