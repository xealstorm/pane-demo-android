package com.example.panedemo.panecomparison.nav3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.panedemo.nav.Nav3Navigator
import com.example.panedemo.nav.TwoPaneSceneStrategy
import com.example.panedemo.nav.rememberTwoPaneMode
import com.example.panedemo.panecomparison.nav3.list.ListScreen
import com.example.panedemo.panecomparison.nav3.message.MessageScreen
import com.example.panedemo.panecomparison.nav3.product.ProductScreen
import com.example.panedemo.ui.theme.PaneDemoTheme

/**
 * Sole host for the Nav3-based pane-comparison demos. Runs the same List + Product + Message flow
 * under either a fixed [com.example.panedemo.nav.TwoPaneMode]-weighted divider or a user-draggable divider, selected by the
 * launcher via the [EXTRA_RESIZABLE] intent extra.
 */
class NavActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val resizable = intent.getBooleanExtra(EXTRA_RESIZABLE, false)

        setContent {
            PaneDemoTheme {
                ProductNavHost(resizable = resizable, onFinishActivity = ::finish)
            }
        }
    }

    companion object {
        private const val EXTRA_RESIZABLE = "extra_resizable"

        fun intent(context: Context, resizable: Boolean): Intent =
            Intent(context, NavActivity::class.java)
                .putExtra(EXTRA_RESIZABLE, resizable)
    }
}

@Composable
private fun ProductNavHost(
    resizable: Boolean,
    onFinishActivity: () -> Unit,
) {
    val backStack = rememberNavBackStack(NavRoute.List)
    val navigator = remember(backStack) { Nav3Navigator(backStack, onExit = onFinishActivity) }
    val twoPaneMode = rememberTwoPaneMode()

    val strategy = remember {
        TwoPaneSceneStrategy<NavKey>(
            modeProvider = { twoPaneMode },
            resizable = resizable,
        )
    }

    val provider = remember(navigator) {
        entryProvider<NavKey> {
            entry<NavRoute.List> { ListScreen(navigator) }
            entry<NavRoute.Product> { ProductScreen(navigator) }
            entry<NavRoute.Message>(metadata = TwoPaneSceneStrategy.twoPane("Write reply")) {
                MessageScreen(navigator)
            }
        }
    }

    val entries = rememberDecoratedNavEntries(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = provider,
    )

    NavDisplay(
        entries = entries,
        onBack = { navigator.navigateBack() },
        sceneStrategies = listOf(strategy),
    )
}
