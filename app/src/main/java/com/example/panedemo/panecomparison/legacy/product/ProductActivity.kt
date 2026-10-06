package com.example.panedemo.panecomparison.legacy.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.panedemo.panecomparison.common.product.ProductUiState
import com.example.panedemo.panecomparison.common.product.ProductViewModel
import com.example.panedemo.panecomparison.legacy.message.MessageActivity
import com.example.panedemo.ui.theme.PaneDemoTheme

class ProductActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaneDemoTheme {
                val viewModel: ProductViewModel = viewModel()
                val viewState: ProductUiState by viewModel.uiState.collectAsStateWithLifecycle()

                ProductScreen(
                    viewState = viewState,
                    onContactClicked = { startActivity(MessageActivity.intent(this)) },
                    onBackPressed = ::finish,
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, ProductActivity::class.java)
    }
}