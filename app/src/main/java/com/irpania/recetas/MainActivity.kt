package com.irpania.recetas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.irpania.recetas.ui.home.HomeScreen
import com.irpania.recetas.ui.onboarding.OnboardingScreen
import com.irpania.recetas.ui.recipedetail.RecipeDetailScreen
import com.irpania.recetas.ui.theme.RecetasTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String, @StringRes val resourceId: Int? = null, val icon: ImageVector? = null) {
    object Home : Screen("home", R.string.home_tab, Icons.Default.Home)
    object Favorites : Screen("favorites", R.string.favorites_tab, Icons.Default.Favorite)
    object RecipeDetail : Screen("recipeDetail/{mealId}")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value
        }
        enableEdgeToEdge()
        setContent {
            val isLoading by viewModel.isLoading.collectAsState()
            if (!isLoading) {
                RecetasTheme {
                    val showOnboarding by viewModel.showOnboarding.collectAsState()
                    if (showOnboarding) {
                        OnboardingScreen(onOnboardingFinished = { viewModel.setOnboardingCompleted() })
                    } else {
                        MainScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isTopLevelDestination = currentDestination?.hierarchy?.any {
        it.route == Screen.Home.route || it.route == Screen.Favorites.route
    } == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                navigationIcon = {
                    if (!isTopLevelDestination) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.back_action)
                            )
                        }
                    }
                },
            )
        },
        bottomBar = {
            if (isTopLevelDestination) {
                AppBottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        val homeState by viewModel.homeScreenState.collectAsState()

        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                HomeScreen(
                    uiState = homeState,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onMealClick = { mealId -> navController.navigate("recipeDetail/$mealId") },
                    showFavoritesOnly = false
                )
            }
            composable(Screen.Favorites.route) {
                HomeScreen(
                    uiState = homeState,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onMealClick = { mealId -> navController.navigate("recipeDetail/$mealId") },
                    showFavoritesOnly = true
                )
            }
            composable(
                route = Screen.RecipeDetail.route,
                arguments = listOf(navArgument("mealId") { type = NavType.StringType })
            ) {
                RecipeDetailScreen()
            }
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Favorites,
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            screen.resourceId?.let { stringResource(it) }?.let {
                NavigationBarItem(
                    icon = { screen.icon?.let { Icon(it, contentDescription = null) } },
                    label = { Text(it) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
