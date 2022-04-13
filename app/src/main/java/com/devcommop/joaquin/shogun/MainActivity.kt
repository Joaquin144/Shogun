package com.devcommop.joaquin.shogun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Our job is to display a webpage using webview and run some android code when its elements are accessed
        Steps :--
        1.) INTERNET permission in Manifest
        2.) webview in xml
        3.) load your url keeping javascript as enabled
        4.) define a client and assign it to the webview
        5.)
         */
        val webView: WebView = findViewById(R.id.webView)
        webView.loadUrl("https://cheezycode.com")
        webView.settings.javaScriptEnabled = true //Android's WARNING :-- This can introduce XSS vulnerabilities in app
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                injectJS(view)
            }
        }
        webView.addJavascriptInterface(JsAndroidBridge,"Bridge")

    }

    private fun injectJS(view: WebView?) {
        view!!.loadUrl(
            """
                javascript: (function(){
                    let header = document.querySelector(".header-widget");
                    header.addEventListener("click",function(){
                        Bridge.calledFromJS();
                    })
                })()
            """
        )
    }

    object JsAndroidBridge{
        @JavascriptInterface
        fun calledFromJS(){
            Log.d("####","You called android function using JS")
            //Toast.makeText(this@MainActivity,"You called android function using JS",Toast.LENGTH_SHORT).show()
        }
    }
}