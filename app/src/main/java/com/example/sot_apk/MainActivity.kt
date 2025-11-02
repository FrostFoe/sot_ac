package com.example.sot_apk

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url
                if (url != null) {
                    if (url.host == "study.mnr.world" || url.host == "github.com" || url.host == "accounts.google.com") {
                        return false // Open in WebView
                    }
                    if (url.scheme == "t.me") {
                        val intent = Intent(Intent.ACTION_VIEW, url)
                        intent.setPackage("org.telegram.messenger")
                        try {
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            // Telegram app is not installed, open in browser
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/" + url.path?.substring(1)))
                            startActivity(browserIntent)
                        }
                        return true // Don't load in WebView
                    }
                    val intent = Intent(Intent.ACTION_VIEW, url)
                    startActivity(intent)
                    return true // Don't load in WebView
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        webView.loadUrl("https://study.mnr.world/calendar")
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed()
                return
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}
