import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import xyz.midnight233.deskpose.*

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        TreeNavigationView(
            paths = listOf(
                listOf("Home"),
                listOf("Foundation", "Stateless Buttons"),
                listOf("Foundation", "Stateful Buttons"),
            ),
            modifier = Modifier
                .fillMaxSize()
        ) { pageIndex ->
            when (pageIndex) {
                0 -> {
                    Column {
                        var sel by remember { mutableStateOf(false) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button("Button", {})
                            OutlineButton("Button", {})
                            HintDot(sel, Color.Red, "Hint")
                        }
                        var text by remember { mutableStateOf("") }
                        TextField(value = text, onValueChange = { text = it })
                        OutlineTextField(value = text, onValueChange = { text = it })
                        RadioButton("Radio", sel, { sel = !sel })
                        CheckButton("Check", sel, { sel = !sel })
                        TristateCheckButton("Check", if (sel) ToggleableState.Indeterminate else ToggleableState.Off, { sel = !sel })
                        var idx by remember { mutableStateOf(0) }
                        ComboButton((0..10).map { "Item $it" }, idx, { idx = it })
                        var rat by remember { mutableStateOf(0f) }
                        Slider(rat) { rat = it }
                        ProgressBar(rat)
                        BreadcrumbBar(listOf("1", "2", "3"), {})
                        var sel2 by remember { mutableStateOf(0) }
                        TabView(sel2, { sel2 = it }, listOf("one", "two", "three")) {}
                    }
                }
            }
        }
    }
}
