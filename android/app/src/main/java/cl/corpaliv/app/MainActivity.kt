package cl.corpaliv.app

import cl.corpaliv.app.ui.screens.CorpalivApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CorpalivApp() }
    }
}
