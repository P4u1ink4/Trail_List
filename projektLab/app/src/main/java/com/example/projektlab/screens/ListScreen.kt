package com.example.projektlab.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.projektlab.additional.DrawerContent
import com.example.projektlab.data.Trail
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ListScreen(navController: NavController, context: Context, trails: List<Trail>, gridcells: Int) {
    val titles = listOf("Główna", "Krótkie trasy", "Długie trasy")
    val pagerState = rememberPagerState(initialPage = 0)
    var searchText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController = navController, drawerState = drawerState)
        }
    ){
        Box{
            Column{
                when(pagerState.currentPage){
                    0 -> MainCardTopBar(onMenuClick = { coroutineScope.launch { drawerState.open() } })
                    1 -> TrailListTopBar(searchText = searchText, onSearchTextChanged = { searchText = it }, onMenuClick = { coroutineScope.launch { drawerState.open() } })
                    2 -> TrailListTopBar(searchText = searchText, onSearchTextChanged = { searchText = it }, onMenuClick = { coroutineScope.launch { drawerState.open() } })
                }

                TabRow(selectedTabIndex = pagerState.currentPage) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    }
                }

                HorizontalPager(count = titles.size, state = pagerState) { page ->
                    Column(Modifier.fillMaxHeight()) {
                        when (page) {
                            0 -> MainCard()
                            1 -> TrailListScreen(navController, context, searchText, isShortRoute = true, trails, gridcells)
                            2 -> TrailListScreen(navController, context, searchText, isShortRoute = false, trails, gridcells)
                        }
                    }
                }
            }

        }
    }

}
