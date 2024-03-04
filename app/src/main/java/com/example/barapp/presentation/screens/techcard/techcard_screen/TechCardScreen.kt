package com.example.barapp.presentation.screens.techcard.techcard_screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.example.barapp.R
import com.example.barapp.presentation.screens.ImageViewer
import com.example.barapp.presentation.screens.home.TopBar
import com.example.barapp.presentation.screens.techcard.ScreenEvent
import com.example.barapp.presentation.theme.GrayColor40
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun TechCardScreen(navController: NavController, techCardId: Int) {
    val viewModel = hiltViewModel<TechCardScreenViewModel>()
    val state = viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isLoaded by remember {
        mutableStateOf(false)
    }
    if (!isLoaded) {
        viewModel.onEvent(TechCardScreenEvent.OnLoad(techCardId))
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
        var showImage by remember {
            mutableStateOf<ImageRequest?>(null)
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            topBar = {
                TopBar(
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
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

                    },
                    backgroundColor = MaterialTheme.colors.surface
                )
            },

            ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                if (isLoaded) {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 13.dp, vertical = 10.dp)
                            .fillMaxWidth(),
                        border = BorderStroke(1.dp, GrayColor40),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {

                            }
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.application_scope),
                                    style = MaterialTheme.typography.h6.copy(
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = state.value.techCard.applicationScope,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.raw_material_requirements),
                                    style = MaterialTheme.typography.h6.copy(
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = state.value.techCard.rawMaterialRequirements,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.recipe_lbl),
                                    style = MaterialTheme.typography.h6.copy(
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            item {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize()
                                    ){
                                        RecipeItem(
                                            number = stringResource(R.string.number_symbol),
                                            name = stringResource(R.string.name),
                                            consumptionBrutto = stringResource(R.string.consumption_brutto),
                                            consumptionNetto = stringResource(R.string.consumption_netto)
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top=4.dp)
                                                .height(1.dp)
                                                .background(MaterialTheme.colors.onBackground)
                                        )
                                    }

                                    state.value.recipes.forEachIndexed{index, recipe->
                                        RecipeItem(
                                            number = (index + 1).toString(),
                                            name = recipe.recipe.product,
                                            consumptionBrutto = recipe.recipe.consumptionBrutto1g,
                                            consumptionNetto = recipe.recipe.consumptionNetto1g
                                        )
                                    }
                                }
                            }
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.technological_process),
                                    style = MaterialTheme.typography.h6.copy(
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = state.value.techCard.technologicalProcess,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                            item {

                            }
                        }
                    }
                }
            }
            showImage?.let {
                ImageViewer(
                    showImage = showImage,
                    onDismiss = {
                        showImage = null
                    }
                )
            }
        }
    }
}

@Composable
fun RecipeItem(
    textStyle:TextStyle = MaterialTheme.typography.body2,
    number:String,
    name:String,
    consumptionBrutto:String,
    consumptionNetto:String
){
    val localDensity = LocalDensity.current
    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }
    Row(
        modifier = Modifier.fillMaxWidth().onGloballyPositioned { coordinates ->
            columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
        }
    ){
        Row(
            modifier = Modifier.weight(10f)){
            Text(
                modifier = Modifier.padding(horizontal = 2.dp),
                style = textStyle,
                text = number
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .height(columnHeightDp)
                    .padding(horizontal = 2.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colors.onBackground)
            )
        }
        Row(
            modifier = Modifier.weight(40f)){
            Text(
                modifier = Modifier.padding(horizontal = 2.dp),
                style = textStyle,
                text = name
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .height(columnHeightDp)
                    .padding(horizontal = 2.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colors.onBackground)
            )
        }
        Row(
            modifier = Modifier.weight(25f)){
            Text(
                modifier = Modifier.padding(horizontal = 2.dp),
                style = textStyle,
                text = consumptionBrutto
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .height(columnHeightDp)
                    .padding(horizontal = 2.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colors.onBackground)
            )
        }
        Row(
            modifier = Modifier.weight(25f)){
            Text(
                modifier = Modifier.padding(horizontal = 2.dp),
                style = textStyle,
                text = consumptionNetto
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
        }
//        Text(
//            modifier = Modifier.weight(9f),
//            style = textStyle,
//            text = number
//        )
//        Box(modifier = Modifier
//            .weight(1f)
//            .height(columnHeightDp)
//            .padding(horizontal = 2.dp)
//            .width(1.dp)
//            .background(MaterialTheme.colors.onBackground))
//        Text(
//            modifier = Modifier.weight(40f),
//            style = textStyle,
//            text = name
//        )
//        Box(modifier = Modifier
//            .weight(1f)
//            .height(columnHeightDp)
//            .padding(horizontal = 2.dp)
//            .width(1.dp)
//            .background(MaterialTheme.colors.onBackground))
//        Text(
//            modifier = Modifier.weight(24f),
//            style = textStyle,
//            text = consumptionBrutto
//        )
//        Box(modifier = Modifier
//            .weight(1f)
//            .height(columnHeightDp)
//            .padding(horizontal = 2.dp)
//            .width(1.dp)
//            .background(MaterialTheme.colors.onBackground))
//        Text(
//            modifier = Modifier.weight(24f),
//            style = textStyle,
//            text = consumptionNetto
//        )
    }
}
