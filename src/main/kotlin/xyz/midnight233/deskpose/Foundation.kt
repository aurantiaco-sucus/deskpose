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
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*

private fun deskposeStaticPath(builder: Path.() -> Unit) = Path().run { this.builder(); return@run this }

private fun deskposeGrayscale(gray: Float) = Color(red = gray, green = gray, blue = gray)

private val deskposeFocusedOutlineColor = deskposeGrayscale(0.75f)
private val deskposeOutlineColor = deskposeGrayscale(0.8f)
private val deskposeUnfocusedOutlineColor = deskposeGrayscale(0.85f)

private val deskposeFocusedTinySurfaceColor = deskposeGrayscale(0.8f)
private val deskposeTinySurfaceColor = deskposeGrayscale(0.85f)
private val deskposeUnfocusedTinySurfaceColor = deskposeGrayscale(0.9f)

private val deskposeSelectedSmallSurfaceColor = deskposeGrayscale(0.8f)
private val deskposeFocusedSmallSurfaceColor = deskposeGrayscale(0.85f)
private val deskposeSmallSurfaceColor = deskposeGrayscale(0.9f)
private val deskposeUnfocusedSmallSurfaceColor = deskposeGrayscale(0.95f)

private val deskposeSelectedSurfaceColor = deskposeGrayscale(0.875f)
private val deskposeFocusedSurfaceColor = deskposeGrayscale(0.925f)
private val deskposeSurfaceColor = deskposeGrayscale(0.95f)
private val deskposeUnfocusedSurfaceColor = deskposeGrayscale(0.975f)

private val deskposeDefaultShape = CircleShape

val tipTextStyle = TextStyle(
    color = Color.Black,
    fontSize = 12.sp,
    fontWeight = FontWeight.Medium
)
val labelTextStyle = TextStyle(
    color = Color.Black,
    fontSize = 14.sp
)
val headingTextStyle = TextStyle(
    color = Color(0xFF606060),
    fontSize = 24.sp
)

@Composable
private fun DeskposeClickable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    color: Color = Color.White,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
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

@Composable
private fun DeskposeUnclickable(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = Color.White,
    border: BorderStroke? = null,
    content: @Composable () -> Unit
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

@Composable
fun Button(
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DeskposeClickable(
        onClick = onClick,
        shape = deskposeDefaultShape,
        color = deskposeSurfaceColor,
        modifier = modifier
    ) {
        PaddedLabel(caption)
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
        shape = deskposeDefaultShape,
        color = deskposeSurfaceColor,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .buttonPadding(),
            content = content
        )
    }
}

@Composable
fun OutlineButton(
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DeskposeClickable(
        onClick = onClick,
        shape = deskposeDefaultShape,
        border = BorderStroke(width = 2.dp, color = deskposeOutlineColor),
        modifier = modifier
    ) {
        PaddedLabel(caption)
    }
}

@Composable
fun CustomOutlineButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    DeskposeClickable(
        onClick = onClick,
        shape = deskposeDefaultShape,
        border = BorderStroke(width = 2.dp, color = deskposeOutlineColor),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .buttonPadding(),
            content = content
        )
    }
}

@Composable
fun TextButton(caption: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    DeskposeClickable(
        onClick = onClick,
        shape = deskposeDefaultShape,
        color = Color.Transparent,
        modifier = modifier
    ) {
        PaddedLabel(caption)
    }
}

@Composable
fun CustomTextButton(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    DeskposeClickable(
        onClick = onClick,
        shape = deskposeDefaultShape,
        color = Color.Transparent,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .buttonPadding(),
            content = content
        )
    }
}

@Composable
fun TextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val color by animateColorAsState(if (isFocused) deskposeFocusedSurfaceColor else deskposeUnfocusedSurfaceColor)
    DeskposeUnclickable(
        shape = deskposeDefaultShape,
        color = color,
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
fun OutlineTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(if (isFocused) deskposeFocusedOutlineColor else deskposeUnfocusedOutlineColor)
    DeskposeUnclickable(
        shape = deskposeDefaultShape,
        border = BorderStroke(width = 2.dp, color = borderColor),
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

private val deskposeStatefulTextButtonIconWidth = 16f

@Composable
private fun DeskposeStatefulTextButton(
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    graphic: DrawScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val iconWidth = deskposeStatefulTextButtonIconWidth
    DeskposeClickable(
        onClick = onClick,
        interactionSource = interactionSource,
        shape = deskposeDefaultShape,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .buttonPadding()
        ) {
            Canvas(
                modifier = Modifier
                    .size(iconWidth.dp)
            ) {
                this@Canvas.graphic()
            }
            Spacer(Modifier.width(8.dp))
            Label(caption)
        }
    }
}

@Composable
fun RadioButton(caption: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val dotColor by animateColorAsState(if (selected) Color.Black else Color.Transparent)
    val iconWidth = deskposeStatefulTextButtonIconWidth
    DeskposeStatefulTextButton(
        caption = caption,
        onClick = onClick,
        modifier = modifier
    ) {
        drawCircle(
            color = deskposeSmallSurfaceColor,
            radius = iconWidth / 2,
            center = Offset(8f, 8f),
            style = Fill
        )
        drawCircle(
            color = dotColor,
            radius = iconWidth / 4,
            center = Offset(8f, 8f),
            style = Fill
        )
    }
}

private fun deskposeCheckButtonTickPath(origin: Float = 4f, radius: Float = 4f) = Path().run {
    val arg1 = 1f
    val arg2 = 1f
    moveTo(origin, origin + arg1 * radius)
    lineTo(origin + arg2 * radius, origin + 2f * radius)
    lineTo(origin + 2f * radius, origin)
    return@run this
}

@Composable
fun CheckButton(caption: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val dotColor by animateColorAsState(if (selected) Color.Black else Color.Transparent)
    val iconWidth = deskposeStatefulTextButtonIconWidth
    DeskposeStatefulTextButton(
        caption = caption,
        onClick = onClick,
        modifier = modifier
    ) {
        drawRect(
            color = deskposeSmallSurfaceColor,
            topLeft = Offset.Zero,
            size = Size(iconWidth, iconWidth),
            style = Fill
        )
        drawPath(
            path = deskposeCheckButtonTickPath(),
            color = dotColor,
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
    val dotColor by animateColorAsState(
        if (state == ToggleableState.On || state == ToggleableState.Indeterminate)
            Color.Black else Color.Transparent)
    DeskposeStatefulTextButton(
        caption = caption,
        onClick = onClick,
        modifier = modifier,
    ) {
        drawRect(
            color = deskposeSmallSurfaceColor,
            topLeft = Offset.Zero,
            size = Size(16f, 16f),
            style = Fill
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
}

private val deskposeComboButtonDropdownArrowPath = deskposeStaticPath {
    moveTo(0f, 0f)
    lineTo(5f, 5f)
    lineTo(10f, 0f)
}

@Composable
fun ComboButton(items: List<String>, selectedIndex: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    var isPopping by remember { mutableStateOf(false) }
    val ratio by animateFloatAsState(if (isPopping) 1f else 0f)
    val color by animateColorAsState(if (isPopping) deskposeFocusedSurfaceColor else deskposeSurfaceColor)
    DeskposeUnclickable(
        shape = RoundedCornerShape(12.dp),
        color = deskposeUnfocusedSurfaceColor,
        modifier = modifier
    ) {
        Column {
            DeskposeClickable(
                color = color,
                shape = deskposeDefaultShape,
                onClick = {
                    isPopping = !isPopping
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PaddedLabel(items[selectedIndex])
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 10.dp)
                    ) {
                        Canvas(
                            modifier = Modifier
                                .size(width = 10.dp, height = 5.dp)
                                .rotate(ratio * 180f)
                                .align(Alignment.Center)
                        ) {
                            drawPath(
                                path = deskposeComboButtonDropdownArrowPath,
                                color = Color(0xFF808080),
                                style = Stroke(2f)
                            )
                        }
                    }
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
                            PaddedLabel(items[it])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HintDot(activated: Boolean, color: Color, caption: String) {
    val mainColor by animateColorAsState(if (activated) color else deskposeTinySurfaceColor)
    val ratio by animateFloatAsState(if (activated) 1f else 0f)
    val stroke by animateDpAsState(if (activated) 2.dp else 5.dp)
    DeskposeUnclickable(
        shape = deskposeDefaultShape,
        border = BorderStroke(stroke, mainColor),
        modifier = Modifier
            .padding(start = ((1 - ratio) * 6f).dp)
            .defaultMinSize(10.dp, 10.dp)
    ) {
        AnimatedVisibility(visible = activated) {
            Label(
                caption = caption,
                style = tipTextStyle,
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 3.dp)
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
    val knobColor by animateColorAsState(if (isFocused || isHovered) deskposeFocusedSmallSurfaceColor else deskposeUnfocusedSmallSurfaceColor)
    Box(
        modifier = Modifier
            .size(width = width, height = height)
    ) {
        Canvas(
            modifier = Modifier
                .size(width = width, height = height),
        ) {
            drawLine(
                color = deskposeOutlineColor,
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
        shape = deskposeDefaultShape,
        color = deskposeUnfocusedSmallSurfaceColor,
        modifier = Modifier
            .height(10.dp)
            .then(modifier)
    ) {
        DeskposeUnclickable(
            color = deskposeSelectedSmallSurfaceColor,
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
            shape = deskposeDefaultShape,
            modifier = Modifier.padding(5.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                captions.indices.forEach {
                    val color by animateColorAsState(if (selected == it) deskposeSelectedSurfaceColor else deskposeUnfocusedSurfaceColor)
                    DeskposeClickable(
                        onClick = { onSelect(it) },
                        color = color
                    ) {
                        PaddedLabel(captions[it])
                    }
                }
            }
        }
        Crossfade(selected) {
            content(it)
        }
    }
}

private val deskposeBackArrowPath = deskposeStaticPath {
    moveTo(7.5f, 0f)
    lineTo(0f, 7.5f)
    lineTo(7.5f, 15f)
    moveTo(1f, 7.5f)
    lineTo(15f, 7.5f)
}

@Composable
private fun DeskposeBackArrow(color: Color = Color.Black) {
    Canvas(
        modifier = Modifier
            .size(15.dp)
    ) {
        drawPath(
            path = deskposeBackArrowPath,
            color = Color.Black,
            style = Stroke(2f)
        )
    }
}

private val deskposeRightArrowPath = deskposeStaticPath {
    moveTo(0f, 0f)
    lineTo(5f, 5f)
    lineTo(0f, 10f)
}

@Composable
private fun DeskposeRightArrow() {
    Canvas(
        modifier = Modifier
            .size(width = 5.dp, height = 10.dp)
    ) {
        drawPath(
            path = deskposeRightArrowPath,
            color = deskposeOutlineColor,
            style = Stroke(2f)
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
                DeskposeRightArrow()
                Spacer(Modifier.width(5.dp))
            }
        }
    }
}

private class FilteredTreeItem(val caption: String, val itemIndex: Int? = null)

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
                    shape = deskposeDefaultShape,
                    color = deskposeSurfaceColor,
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
                        shape = deskposeDefaultShape,
                        color = if (currentPage == it.itemIndex) deskposeSelectedSurfaceColor else Color.White,
                        onClick = { currentPage = it.itemIndex },
                        modifier = Modifier
                            .padding(start = 5.dp, top = 5.dp)
                    ) {
                        PaddedLabel(it.caption)
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
                            PaddedLabel(it.caption)
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

@Composable
fun Label(
    caption: String,
    style: TextStyle = labelTextStyle,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    overflow: TextOverflow = TextOverflow.Ellipsis,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = caption,
        style = style,
        onTextLayout = onTextLayout,
        overflow = overflow,
        minLines = minLines,
        maxLines = maxLines,
        modifier = modifier
    )
}

private fun Modifier.buttonPadding(extra: Dp = 0.dp) = this
    .padding(horizontal = 10.dp + extra, vertical = 5.dp + extra)

@Composable
fun PaddedLabel(
    caption: String,
    style: TextStyle = labelTextStyle,
    modifier: Modifier = Modifier
) {
    Label(
        caption = caption,
        style = style,
        maxLines = 1,
        modifier = Modifier
            .buttonPadding()
            .then(modifier)
    )
}