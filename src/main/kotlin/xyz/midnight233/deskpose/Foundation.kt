package xyz.midnight233.deskpose

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
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
fun Button(
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    DeskposeClickable(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Color(0xFFE8E8E8),
        contentColor = Color.Black,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
            content = content
        )
    }
}

@Composable
fun TextButton(caption: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DeskposeClickable(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Color.Transparent,
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
fun CustomTextButton(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    DeskposeClickable(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Color.Transparent,
        contentColor = Color.Black,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
            content = content
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
    shape: Shape = RoundedCornerShape(50),
    modifier: Modifier = Modifier,
    graphic: DrawScope.(Float, Color) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val dotColor by animateColorAsState(if (selected) Color.Black else Color.Transparent)
    DeskposeClickable(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = shape,
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
    DeskposeBooleanButton(caption, selected, onClick, modifier = modifier) { radius, activeColor ->
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
    DeskposeBooleanButton(
        caption,
        selected,
        onClick,
        RoundedCornerShape(
            topStartPercent = 20,
            bottomStartPercent = 20,
            topEndPercent = 50,
            bottomEndPercent = 50
        ),
        modifier
    ) { radius, activeColor ->
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
        shape = RoundedCornerShape(
            topStartPercent = 20,
            bottomStartPercent = 20,
            topEndPercent = 50,
            bottomEndPercent = 50
        ),
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

@Composable
fun Slider(value: Float, width: Dp = 150.dp, height: Dp = 20.dp, knobWidth: Dp = 20.dp, onValueChange: (Float) -> Unit) {
    val gapAmount = 0.dp
    val availWidth = width.value - knobWidth.value
    require(value in 0f..1f)
    var currentDrag by remember { mutableStateOf(value) }
    val state = rememberDraggableState {
        currentDrag += it / availWidth
        val newValue = currentDrag.coerceIn(0f..1f)
        if (newValue != value) onValueChange(newValue)
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val knobColor by animateColorAsState(if (isFocused || isHovered) Color(0xFFD0D0D0) else Color(0xFFE8E8E8))
    Box(
        modifier = Modifier
            .size(width = width, height = height)
    ) {
        Canvas(
            modifier = Modifier
                .size(width = width, height = height),
        ) {
            drawLine(
                color = Color(0xFFA0A0A0),
                start = Offset(height.value / 2, height.value / 2),
                end = Offset(width.value - height.value / 2, height.value / 2),
                strokeWidth = 2f
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .size(width = width, height = height)
        ) {
            Spacer(modifier = Modifier.width(value * availWidth.dp + gapAmount))
            DeskposeUnclickable(
                shape = CircleShape,
                color = knobColor,
                modifier = Modifier
                    .size(height = height - gapAmount * 2f, width = knobWidth - gapAmount * 2f)
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = state,
                        onDragStarted = {
                            currentDrag = value
                        }
                    )
                    .focusable(interactionSource = interactionSource)
                    .hoverable(interactionSource)
            ) {}
        }
    }
}

@Composable
fun ProgressBar(value: Float, modifier: Modifier = Modifier.width(150.dp)) {
    DeskposeUnclickable(
        shape = RoundedCornerShape(50),
        color = Color(0xFFF0F0F0),
        modifier = Modifier
            .height(10.dp)
            .then(modifier)
    ) {
        DeskposeUnclickable(
            color = Color(0xFFA0A0A0),
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(value)
        ) {}
    }
}

@Composable
fun TabView(selected: Int, onSelect: (Int) -> Unit, captions: List<String>, modifier: Modifier = Modifier, content: @Composable (Int) -> Unit) {
    Column(
        modifier = modifier
    ) {
        DeskposeUnclickable(
            shape = RoundedCornerShape(50),
            border = BorderStroke(width = 2.dp, color = Color(0xFFE0E0E0)),
            modifier = Modifier.padding(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                captions.indices.forEach {
                    val contentColor by animateColorAsState(if (selected == it) Color.Black else Color(0xFF808080))
                    val color by animateColorAsState(if (selected == it) Color(0xFFE0E0E0) else Color.White)
                    DeskposeClickable(
                        onClick = { onSelect(it) },
                        color = color,
                        contentColor = contentColor
                    ) {
                        Text(
                            text = captions[it],
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
        Crossfade(selected) {
            content(it)
        }
    }
}

@Composable
private fun DeskposeBackArrow(color: Color = Color.Black) {
    Canvas(
        modifier = Modifier
            .size(15.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(7.5f, 0f),
            end = Offset(0f, 7.5f),
            strokeWidth = 2f
        )
        drawLine(
            color = color,
            start = Offset(0f, 7.5f),
            end = Offset(7.5f, 15f),
            strokeWidth = 2f
        )
        drawLine(
            color = color,
            start = Offset(1f, 7.5f),
            end = Offset(15f, 7.5f),
            strokeWidth = 2f
        )
    }
}

@Composable
private fun DeskposeRightArrow(color: Color = Color.Black) {
    Canvas(
        modifier = Modifier
            .size(width = 5.dp, height = 10.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(5f, 5f),
            strokeWidth = 2f
        )
        drawLine(
            color = color,
            start = Offset(5f, 5f),
            end = Offset(0f, 10f),
            strokeWidth = 2f
        )
    }
}

@Composable
fun BreadcrumbBar(items: List<String>, onClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .then(modifier)
    ) {
        Spacer(Modifier.width(5.dp))
        items.indices.forEach {
            TextButton(
                caption = items[it],
                onClick = { onClick(it) }
            )
            Spacer(Modifier.width(5.dp))
            if (it != items.size - 1) {
                DeskposeRightArrow(color = Color(0xFFD0D0D0))
                Spacer(Modifier.width(5.dp))
            }
        }
    }
}

class FilteredTreeItem(val caption: String, val itemIndex: Int? = null)

private fun filterTreeItems(cur: List<String>, paths: List<List<String>>): List<FilteredTreeItem> {
    var list = paths.withIndex()
    cur.forEach { seg ->
        list = list
            .filter { it.value[0] == seg }
            .map { IndexedValue(it.index, it.value.subList(1, it.value.size)) }
    }
    val leaves = list
        .filter { it.value.size == 1 }
        .map { FilteredTreeItem(caption = it.value[0], itemIndex = it.index) }
    val categories = list
        .filter { it.value.size > 1 }
        .map { it.value[0] }
        .distinct()
        .map { FilteredTreeItem(caption = it) }
    return leaves + categories
}

@Composable
fun TreeNavigationView(paths: List<List<String>>, modifier: Modifier = Modifier, content: @Composable (Int) -> Unit) {
    var currentPage by remember { mutableStateOf(0) }
    val currentPath = remember { mutableStateListOf<String>() }
    Row (
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedVisibility(visible = currentPath.size != 0) {
                DeskposeClickable(
                    onClick = { currentPath.removeLast() },
                    shape = CircleShape,
                    color = Color(0xFFF0F0F0),
                    modifier = Modifier
                        .padding(top = 5.dp, start = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                        ) {
                            DeskposeBackArrow()
                        }
                    }
                }
            }
            val items = filterTreeItems(currentPath, paths)
            items.forEach {
                if (it.itemIndex != null) {
                    DeskposeClickable(
                        shape = RoundedCornerShape(50),
                        color = if (currentPage == it.itemIndex) Color(0xFFF8F8F8) else Color.White,
                        onClick = { currentPage = it.itemIndex },
                        modifier = Modifier
                            .padding(start = 5.dp, top = 5.dp)
                    ) {
                        Text(
                            text = it.caption,
                            fontWeight = if (currentPage == it.itemIndex) FontWeight.SemiBold else FontWeight.Normal,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                } else {
                    DeskposeClickable(
                        shape = RoundedCornerShape(50),
                        onClick = { currentPath.add(it.caption) },
                        modifier = Modifier
                            .padding(start = 5.dp, top = 5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.caption,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                            DeskposeRightArrow()
                            Spacer(Modifier.width(10.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(5.dp))
        }
        Crossfade(
            targetState = currentPage,
            modifier = Modifier
                .fillMaxSize()
        ) { content(currentPage) }
    }
}