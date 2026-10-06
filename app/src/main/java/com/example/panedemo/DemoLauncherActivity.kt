package com.example.panedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.panedemo.panecomparison.unoptimised.list.UnoptimisedListActivity
import com.example.panedemo.panecomparison.legacy.list.ListActivity
import com.example.panedemo.panecomparison.nav3.NavActivity
import com.example.panedemo.ui.theme.PaneDemoTheme

class DemoLauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaneDemoTheme {
                DemoLauncherScreen(
                    onUnoptimisedClicked = {
                        startActivity(UnoptimisedListActivity.intent(this))
                    },
                    onLegacyDemoClicked = {
                        startActivity(ListActivity.intent(this))
                    },
                    onNav3DemoClicked = {
                        startActivity(NavActivity.intent(this, resizable = false))
                    },
                    onResizableNav3DemoClicked = {
                        startActivity(NavActivity.intent(this, resizable = true))
                    },
                    onBackPressed = ::finish,
                )
            }
        }
    }
}
