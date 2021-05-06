package com.example.recipebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.recipebook.model.Flavor
import com.example.recipebook.model.RecipeUiModel
import com.example.recipebook.ui.theme.BuildingComposeTheme
import java.util.*

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuildingComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(topBar = {
                        TopAppBar {
                            Text(
                                text = "Recipe Book",
                                style = MaterialTheme.typography.h5,
                                modifier = Modifier.padding(
                                    start = 8.dp
                                )
                            )
                        }
                    }) {
                        Content()
                    }
                }
            }
        }
    }
}
/*
var desc: String = ""
val recipesSet = (1..100).map {
    desc += "$it ___"
    RecipeUiModel(
        title = it.toString(),
        description = desc,
        flavor = Flavor.values()[it % 2]
    )
}
*/

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun Content() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val keyboard = LocalSoftwareKeyboardController.current
    var recipes: Set<RecipeUiModel> by remember {
        mutableStateOf(
            emptySet()
        )
    }

    val (dialog, setDialog) = remember { mutableStateOf(DialogState.Initial as DialogState) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
    ) {
        val (lazy, column) = createRefs()
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
                .constrainAs(lazy) {
                    top.linkTo(parent.top, margin = 0.dp)
                    bottom.linkTo(column.top)
                    height = Dimension.fillToConstraints
                },

            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Flavor.values().forEach { flavor ->
                stickyHeader {
                    Box(
                        Modifier
                            .background(MaterialTheme.colors.surface)
                            .padding(top = 4.dp, bottom = 4.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = flavor.name,
                            style = MaterialTheme.typography.subtitle1,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                items(items = recipes.filter { it.flavor == flavor },
                    key = { it.title + it.description + it.flavor }) { item ->
                    SwipeableRecipe(item, setDialog,
                        {
                            recipes -= it
                        })
                }
            }
        }
        Column(
            Modifier
                .constrainAs(column) {
                    top.linkTo(lazy.bottom)
                    bottom.linkTo(parent.bottom, margin = 0.dp)

                }
                .padding(4.dp)) {
            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                placeholder = { Text(text = "Recipe Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = description,
                onValueChange = {
                    description = it
                },
                placeholder = { Text(text = "Recipe Description") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.background)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Flavor.values().forEachIndexed { index, flavor ->
                    Button(
                        onClick = {
                            recipes += (RecipeUiModel(title, description, flavor))
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                        modifier = Modifier
                            .weight(1f),
                        enabled = title.isNotEmpty()
                    ) {
                        Text(text = "ADD ${flavor.name.capitalize(Locale.ROOT)}")
                    }
                    if (index < Flavor.values().size - 1)
                        Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
    DialogDemo(dialog, setDialog)
}

sealed class DialogState {
    object Initial : DialogState()
    data class OnSelect(val text: String) : DialogState()
    object OnDismiss : DialogState()
}

@ExperimentalMaterialApi
@Composable
private fun SwipeableRecipe(
    item: RecipeUiModel,
    setDialog: (DialogState) -> Unit,
    remove: (RecipeUiModel) -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) remove(
                item
            )
            it != DismissValue.DismissedToEnd
        }
    )
    Column {
        SwipeToDismiss(state = dismissState, background = {}) {
            Card(
                elevation = animateDpAsState(
                    if (dismissState.dismissDirection != null) 4.dp else 0.dp
                ).value
            ) {
                Text(
                    text = item.title,
                    modifier = Modifier
                        .clickable(onClick = {
                            setDialog(DialogState.OnSelect(item.description))
                        })
                        .padding(start = 26.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun DialogDemo(dialog: DialogState, setDialog: (DialogState) -> Unit) {
    if (dialog is DialogState.OnSelect) {
        Dialog(
            onDismissRequest = {
                setDialog(DialogState.OnDismiss)
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .fillMaxHeight(0.6f)
                    .clip(RoundedCornerShape(5))
                    .background(MaterialTheme.colors.background)
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {

                Text(text = dialog.text, modifier = Modifier.padding(16.dp))

            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Preview
@Composable
fun ContentPreview() {
    BuildingComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Content()
        }
    }
}
