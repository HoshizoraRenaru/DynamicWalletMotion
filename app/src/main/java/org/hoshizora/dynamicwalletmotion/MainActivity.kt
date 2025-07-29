package org.hoshizora.dynamicwalletmotion

import android.content.Intent
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hoshizora.dynamicwalletmotion.ui.SuicaAnimationOverlay

class MainActivity : ComponentActivity() {
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setContent {
            var nfcTriggered by remember { mutableStateOf(false) }

            SuicaAnimationOverlay(
                visible = nfcTriggered,
                onDismiss = { nfcTriggered = false }
            )

            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Suica를 폰에 가까이 대세요", fontSize = 20.sp)
            }

            LaunchedEffect(Unit) {
                enableNfcReader {
                    nfcTriggered = true
                }
            }
        }
    }

    private fun enableNfcReader(onTagRead: () -> Unit) {
        val options = Bundle().apply {
            putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
        }
        nfcAdapter.enableReaderMode(
            this,
            { tag ->
                onTagRead()
            },
            NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            options
        )
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter.disableReaderMode(this)
    }
}
