package com.example.barapp.presentation.screens.techcard.type

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.barapp.R
import com.example.barapp.domain.models.CocktailType
import com.example.barapp.presentation.screens.dialogs.CreateRenameDialog
import com.example.barapp.presentation.screens.home.TopBar
import com.example.barapp.presentation.theme.BarAppTheme
import com.example.barapp.presentation.theme.GrayColor30
import com.example.barapp.presentation.theme.GrayColor40

@Composable
fun CocktailTypesScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<CocktailTypesScreenViewModel>()
    val state = viewModel.state.collectAsState()
    val loggedInUser = viewModel.getLoggedInUser()
    if (loggedInUser != null) {
        CocktailTypesScreenContent(
            types = state.value.types,
            viewModel = viewModel,
            onBackBtnClick={
                navController.popBackStack()
            }

        )
    }
}

@Composable
private fun CocktailTypesScreenContent(
    types: List<CocktailType>,
    viewModel: CocktailTypesScreenViewModel,
    onBackBtnClick: ()->Unit
) {
    var createRenameDialogType by remember {
        mutableStateOf<CocktailType?>(null)
    }
    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(onClick = {
                        onBackBtnClick()
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
                        text = stringResource(id = R.string.cocktail_types),
                        style = MaterialTheme.typography.h5.copy(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                menu = {

                },
                backgroundColor = MaterialTheme.colors.surface
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(60.dp)
                    .border(1.dp, MaterialTheme.colors.background, CircleShape),
                backgroundColor = MaterialTheme.colors.secondary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    createRenameDialogType = CocktailType()
                }) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colors.background
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colors.background)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                item {
                    Spacer(Modifier.height(0.dp))
                }
                items(types) { type ->
                    CocktailTypeItemPanel(type, {
                        createRenameDialogType = it
                    }) {
                        viewModel.onEvent(CocktailTypesScreenEvent.OnDelete(it))
                    }
                }
                item {
                    Spacer(Modifier.height(60.dp))
                }
            }
            if (types.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    text = stringResource(id = R.string.no_elements),
                    style = MaterialTheme.typography.h6.copy(
                        color = GrayColor40,
                        textAlign = TextAlign.Center
                    )
                )
            }
            createRenameDialogType?.let { type->
                CreateRenameDialog(
                    name = type.name,
                    onDismiss = {
                        createRenameDialogType = null
                    },
                    onConfirm ={
                        if(type.id==0){
                            viewModel.onEvent(CocktailTypesScreenEvent.OnCreate(type.copy(name = it)))
                        }
                        else{
                            viewModel.onEvent(CocktailTypesScreenEvent.OnChange(type.copy(name = it)))
                        }
                        createRenameDialogType = null
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CocktailTypeItemPanel(
    type: CocktailType,
    onItemClick: (type: CocktailType) -> Unit,
    onDelete: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 13.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, GrayColor40),
        backgroundColor = MaterialTheme.colors.surface,
        onClick = { onItemClick(type) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .weight(15f),
                painter = painterResource(id = R.drawable.cocktail_ic),
                contentDescription = null,
                tint = GrayColor30
            )
            Row(
                modifier = Modifier.weight(65f, false)
            ) {
                Text(
                    text = type.name,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            IconButton(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(15f),
                onClick = {
                    onDelete(type.id)
                }) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCocktailTypes() {
    BarAppTheme {
    }
}
