package com.example.panedemo.panecomparison.legacy.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.panedemo.panecomparison.common.message.MessageUiState
import com.example.panedemo.panecomparison.common.message.MessageViewModel
import com.example.panedemo.panecomparison.common.product.ProductUiState
import com.example.panedemo.panecomparison.common.product.ProductViewModel
import com.example.panedemo.ui.theme.PaneDemoTheme

class MessageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaneDemoTheme {
                val viewModel: MessageViewModel = viewModel()
                val productViewModel: ProductViewModel = viewModel()
                val viewState: MessageUiState by viewModel.uiState.collectAsStateWithLifecycle()
                val productViewState: ProductUiState by productViewModel.uiState.collectAsStateWithLifecycle()

                MessageScreen(
                    viewState = viewState,
                    productViewState = productViewState,
                    viewModel = viewModel,
                    onBackPressed = ::finish,
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, MessageActivity::class.java)
    }
}