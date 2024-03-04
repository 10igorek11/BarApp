package com.example.barapp.presentation.screens.techcard.techcard_edit_recipe

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.barapp.R
import com.example.barapp.domain.models.Recipe
import com.example.barapp.domain.models.TechCardRecipeDto
import com.example.barapp.presentation.screens.dialogs.CreateRenameDialog
import com.example.barapp.presentation.screens.dialogs.SaveQuitDialog
import com.example.barapp.presentation.screens.home.TopBar
import com.example.barapp.presentation.screens.techcard.ScreenEvent
import com.example.barapp.presentation.theme.GrayColor40

@OptIn(
    ExperimentalMaterialApi::class
)
@Composable
fun TechCardEditScreen(navController: NavController, techCardId: Int) {
    val viewModel = hiltViewModel<TechCardEditScreenViewModel>()
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    var isLoaded by remember {
        mutableStateOf(false)
    }
    if (!isLoaded) {
        viewModel.onEvent(TechCardEditScreenEvent.OnLoad(techCardId, context))
    }
    LaunchedEffect(context) {
        viewModel.screenEvents.collect {
            when (it) {
                ScreenEvent.SUCCESS -> {
                    isLoaded = true
                }

                ScreenEvent.DEFAULT -> {
                    navController.popBackStack()
                }

                ScreenEvent.ERROR -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.server_error),
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var productRecipeAddDialogItem by remember {
            mutableStateOf<TechCardRecipeDto?>(null)
        }
        var renameDialogShowed by remember {
            mutableStateOf(false)
        }
        var saveQuitDialogShowed by remember {
            mutableStateOf(false)
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                TopBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            if (state.value.isModified) {
                                saveQuitDialogShowed = true
                            } else {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = state.value.techCard.name,
                            style = MaterialTheme.typography.h5.copy(
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    menu = {
                        Box {
                            var menuExpanded by remember {
                                mutableStateOf(false)
                            }
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                onClick = { menuExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreHoriz,
                                    contentDescription = null
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .wrapContentWidth()
                            ) {
                                me.saket.cascade.CascadeDropdownMenu(
                                    modifier = Modifier
                                        .background(Color.Transparent)
                                        .border(1.dp, GrayColor40, RoundedCornerShape(5.dp))
                                        .clip(RoundedCornerShape(5.dp)),
                                    shadowElevation = 10.dp,
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }) {
                                    androidx.compose.material.DropdownMenuItem(
                                        onClick = {
                                            renameDialogShowed = true
                                            menuExpanded = false
                                        },
                                        modifier = Modifier
                                            .background(MaterialTheme.colors.surface)
                                            .align(Alignment.End)
                                    ) {
                                        Row {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null
                                            )
                                            Text(
                                                text = stringResource(id = R.string.rename),
                                                modifier = Modifier.padding(horizontal = 10.dp),
                                                style = MaterialTheme.typography.caption.copy(
                                                    color = MaterialTheme.colors.onBackground
                                                )
                                            )
                                        }
                                    }
                                    androidx.compose.material.DropdownMenuItem(
                                        onClick = {
                                            viewModel.onEvent(
                                                TechCardEditScreenEvent.OnConfirm(
                                                    context
                                                )
                                            )
                                            navController.popBackStack()
                                            menuExpanded = false
                                        },
                                        modifier = Modifier
                                            .background(MaterialTheme.colors.surface)
                                            .align(Alignment.End)
                                    ) {
                                        Row {
                                            Row {
                                                Icon(
                                                    imageVector = Icons.Default.Save,
                                                    contentDescription = null,
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.save),
                                                    modifier = Modifier.padding(horizontal = 10.dp),
                                                    style = MaterialTheme.typography.caption.copy(
                                                        color = MaterialTheme.colors.onBackground
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    androidx.compose.material.DropdownMenuItem(
                                        onClick = {
                                            viewModel.onEvent(TechCardEditScreenEvent.OnDelete)
                                            menuExpanded = false
                                        },
                                        modifier = Modifier
                                            .background(MaterialTheme.colors.surface)
                                            .align(Alignment.End)
                                    ) {
                                        Row {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = MaterialTheme.colors.secondary
                                            )
                                            Text(
                                                text = stringResource(id = R.string.delete),
                                                modifier = Modifier.padding(horizontal = 10.dp),
                                                style = MaterialTheme.typography.caption.copy(
                                                    color = MaterialTheme.colors.secondary
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    backgroundColor = MaterialTheme.colors.surface
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(15.dp)
                        .size(60.dp)
                        .border(1.dp, MaterialTheme.colors.background, CircleShape),
                    backgroundColor = MaterialTheme.colors.secondary,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    onClick = {
                        viewModel.onEvent(TechCardEditScreenEvent.OnConfirm(context))
                    }) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colors.background
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.End
            ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                if (isLoaded) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(horizontal = 13.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        item{

                        }
                        item{
                            Text(
                                modifier =Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.cocktail_type),
                                style = MaterialTheme.typography.h6.copy(
                                    color = GrayColor40
                                )
                            )
                        }
                        item{
                            var expanded by remember { mutableStateOf(false) }
                            ExposedDropdownMenuBox(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colors.surface),
                                    value = state.value.cocktailTypes.find { it.id == state.value.techCard.typeId }?.name ?: stringResource(id = R.string.no_chosen),
                                    onValueChange = {},
                                    readOnly = true,
                                    textStyle = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colors.onBackground
                                    ),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    shape = RoundedCornerShape(10.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    state.value.cocktailTypes.forEach{type ->
                                        DropdownMenuItem(
                                            content = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ){
                                                    Text(
                                                        modifier = Modifier.weight(1f),
                                                        text = type.name,
                                                        style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Medium),
                                                    )
                                                }
                                            },
                                            onClick = {
                                                viewModel.onEvent(TechCardEditScreenEvent.SelectedTypeChanged(type))
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        item{
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                label = {
                                    Text(text = stringResource(id = R.string.application_scope))
                                },
                                shape = RoundedCornerShape(10.dp),
                                value = state.value.techCard.applicationScope,
                                isError = state.value.applicationScopeError != null,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 1500) {
                                        viewModel.onEvent(TechCardEditScreenEvent.OnApplicationScopeChange(newValue))
                                    }
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.surface
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next,
                                    capitalization = KeyboardCapitalization.Sentences
                                ),
                            )
                            state.value.applicationScopeError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier
                                        .height(14.dp),
                                    fontSize = 12.sp
                                )
                            } ?: Spacer(Modifier.height(14.dp))
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                label = {
                                    Text(text = stringResource(id = R.string.raw_material_requirements))
                                },
                                shape = RoundedCornerShape(10.dp),
                                value = state.value.techCard.rawMaterialRequirements,
                                isError = state.value.rawMaterialRequirementsError != null,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 1500) {
                                        viewModel.onEvent(TechCardEditScreenEvent.OnRawMaterialRequirementsChange(newValue))
                                    }
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.surface
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next,
                                    capitalization = KeyboardCapitalization.Sentences
                                ),
                            )
                            state.value.rawMaterialRequirementsError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier
                                        .height(14.dp),
                                    fontSize = 12.sp
                                )
                            } ?: Spacer(Modifier.height(14.dp))

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.recipe_lbl),
                                style = MaterialTheme.typography.h6.copy(
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        items(state.value.recipes){ techCardRecipe ->
                            RecipeItem(
                                recipe = techCardRecipe.recipe,
                                onChange = {
                                    productRecipeAddDialogItem = techCardRecipe
                                },
                                onDelete = {
                                    viewModel.onEvent(TechCardEditScreenEvent.OnRecipeDelete(techCardRecipe))
                                }
                            )
                        }
                        item{
                            state.value.recipesError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier
                                        .height(16.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp
                                )
                            } ?: Spacer(Modifier.height(14.dp))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp, vertical = 5.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = MaterialTheme.colors.primary
                                ),
                                shape = RoundedCornerShape(25.dp),
                                onClick = {
                                    if (state.value.recipes.size < 50) {
                                        productRecipeAddDialogItem = TechCardRecipeDto(
                                            id = 0,
                                            techCardId = state.value.techCard.id,
                                            recipeId =  0,
                                            recipe = Recipe()
                                        )
                                    }
                                }) {
                                Text(
                                    text = stringResource(id = R.string.add_position)
                                )
                            }

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                label = {
                                    Text(text = stringResource(id = R.string.technological_process))
                                },
                                shape = RoundedCornerShape(10.dp),
                                value = state.value.techCard.technologicalProcess,
                                isError = state.value.technologicalProcessError != null,
                                onValueChange = { newValue ->
                                    if (newValue.length <= 1500) {
                                        viewModel.onEvent(TechCardEditScreenEvent.OnTechnologicalProcessChange(newValue))
                                    }
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.surface
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next,
                                    capitalization = KeyboardCapitalization.Sentences
                                ),
                            )
                            state.value.technologicalProcessError?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colors.error,
                                    modifier = Modifier
                                        .height(14.dp),
                                    fontSize = 12.sp
                                )
                            } ?: Spacer(Modifier.height(14.dp))
                        }
                        item{
                            Spacer(Modifier.height(30.dp))
                        }
                    }
                }
            }
        }
        productRecipeAddDialogItem?.let{
            ProductRecipeDialog(
                recipe = it.recipe,
                onDismiss = {
                    productRecipeAddDialogItem = null
                },
                onConfirm ={ recipe->
                    if(it.id>0){
                        viewModel.onEvent(
                            TechCardEditScreenEvent.OnRecipeUpdate(
                                it.copy(
                                    recipe = recipe
                                )
                            )
                        )
                    }
                    else {
                        viewModel.onEvent(
                            TechCardEditScreenEvent.OnRecipeAdd(
                                it.copy(
                                    recipe = recipe
                                )
                            )
                        )
                    }
            } )
        }

        if (renameDialogShowed) {
            CreateRenameDialog(
                name = state.value.techCard.name,
                onDismiss = {
                    renameDialogShowed = false
                },
                onConfirm = {
                    viewModel.onEvent(TechCardEditScreenEvent.OnNameChange(it))
                }
            )
        }
        if (saveQuitDialogShowed) {
            SaveQuitDialog(
                onDismiss = {
                    saveQuitDialogShowed = false
                    navController.popBackStack()
                },
                onConfirm = {
                    viewModel.onEvent(TechCardEditScreenEvent.OnConfirm(context))
                }
            )
        }
    }
}

@Composable
fun RecipeItem(
    recipe:Recipe,
    onDelete: ()->Unit,
    onChange: ()->Unit
){
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 6.dp,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, GrayColor40)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 13.dp)
        ){
            Column(
                modifier = Modifier
                    .weight(60f)
            ) {
                Text(
                    modifier = Modifier.padding(end = 5.dp),
                    style = MaterialTheme.typography.h6,
                    text = recipe.product
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                ){
                    Text(
                        modifier = Modifier
                            .weight(70f)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.body2,
                        text = stringResource(id = R.string.consumption_brutto)
                    )
                    Text(
                        modifier = Modifier
                            .weight(30f)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.body2.copy(
                            textAlign = TextAlign.Center
                        ),
                        text = recipe.consumptionBrutto1g
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                ){
                    Text(
                        modifier = Modifier
                            .weight(70f)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.body2,
                        text = stringResource(id = R.string.consumption_netto)
                    )
                    Text(
                        modifier = Modifier
                            .weight(30f)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.body2.copy(
                            textAlign = TextAlign.Center
                        ),
                        text = recipe.consumptionNetto1g
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(40f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        onChange()
                    }) {
                    Text(
                        text = stringResource(id = R.string.edit)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        onDelete()
                    }) {
                    Text(
                        text = stringResource(id = R.string.delete)
                    )
                }
            }
        }
    }
}
