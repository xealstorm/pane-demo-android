package com.example.panedemo.panecomparison.unoptimised.product

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
import com.example.panedemo.panecomparison.unoptimised.message.UnoptimisedMessageActivity
import com.example.panedemo.ui.theme.PaneDemoTheme

class UnoptimisedProductActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaneDemoTheme {
                val viewModel: ProductViewModel = viewModel()
                val viewState: ProductUiState by viewModel.uiState.collectAsStateWithLifecycle()

                UnoptimisedProductScreen(
                    viewState = viewState,
                    onContactClicked = { startActivity(UnoptimisedMessageActivity.intent(this)) },
                    onBackPressed = ::finish,
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, UnoptimisedProductActivity::class.java)
    }
}
