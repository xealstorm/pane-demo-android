package com.example.panedemo.panecomparison.unoptimised.message

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
import com.example.panedemo.ui.theme.PaneDemoTheme

class UnoptimisedMessageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PaneDemoTheme {
                val viewModel: MessageViewModel = viewModel()
                val viewState: MessageUiState by viewModel.uiState.collectAsStateWithLifecycle()

                UnoptimisedMessageScreen(
                    viewState = viewState,
                    viewModel = viewModel,
                    onBackPressed = ::finish,
                )
            }
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, UnoptimisedMessageActivity::class.java)
    }
}
