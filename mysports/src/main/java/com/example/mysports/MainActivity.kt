package com.example.mysports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.navigation.compose.*
import com.example.mysports.ui.theme.BuildingComposeTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuildingComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavComposeApp()
                }
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object Account : Screen("account", R.string.account)
    object Profile : Screen("profile", R.string.profile)
    object MySports : Screen("mysports", R.string.mysports)
}

val items = listOf(
    Screen.Home,
    Screen.Account,
    Screen.Profile,
    Screen.MySports
)

fun titleFromCurrentRoute(route: String?): Int {
    return when (route) {
        Screen.Home.route -> Screen.Home.resourceId
        Screen.Account.route -> Screen.Account.resourceId
        Screen.Profile.route -> Screen.Profile.resourceId
        Screen.MySports.route -> Screen.MySports.resourceId
        else -> Screen.Home.resourceId
    }
}

@Composable
fun NavComposeApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
    Scaffold(topBar = {
        TopAppBar {
            Text(
                text = stringResource(id = titleFromCurrentRoute(currentRoute)),
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    },
        bottomBar = {
            BottomNavigation {
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            when (screen.route) {
                                Screen.Home.route -> Icon(
                                    imageVector = Icons.Filled.Home,
                                    contentDescription = null
                                )
                                Screen.Account.route -> Icon(
                                    imageVector = Icons.Filled.AccountBox,
                                    contentDescription = null
                                )
                                Screen.Profile.route -> Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = null
                                )
                                Screen.MySports.route -> Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null
                                )
                            }
                        },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo = navController.graph.startDestination
                                launchSingleTop = true
                            }
                        })
                }
            }
        }) {
        NavHost(navController, startDestination = Screen.Home.route) {
            items.forEach { screen ->
                composable(screen.route) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 64.dp)
                    ) {

                        Text(
                            text = stringResource(id = screen.resourceId),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

}
