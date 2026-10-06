package com.example.panedemo.panecomparison.legacy.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.panedemo.panecomparison.common.list.ListScreen
import com.example.panedemo.panecomparison.legacy.product.ProductActivity
import com.example.panedemo.ui.theme.PaneDemoTheme

class ListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaneDemoTheme {
                ListScreen(
                    onItemClicked = { startActivity(ProductActivity.intent(this)) },
                    onBackPressed = ::finish,
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, ListActivity::class.java)
    }
}