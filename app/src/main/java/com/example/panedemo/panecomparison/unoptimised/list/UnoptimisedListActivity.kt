package com.example.panedemo.panecomparison.unoptimised.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.panedemo.panecomparison.unoptimised.product.UnoptimisedProductActivity
import com.example.panedemo.ui.theme.PaneDemoTheme

class UnoptimisedListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaneDemoTheme {
                UnoptimisedListScreen(
                    onItemClicked = { startActivity(UnoptimisedProductActivity.intent(this)) },
                    onBackPressed = ::finish,
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, UnoptimisedListActivity::class.java)
    }
}