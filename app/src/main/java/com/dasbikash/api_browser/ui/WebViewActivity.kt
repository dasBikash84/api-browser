package com.dasbikash.api_browser.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import com.dasbikash.api_browser.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        init()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init() {
        web_view.settings.javaScriptEnabled = true
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false // then it is not handled by default action
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                hideLoadingScreen()
            }
        }

        btnBack.setOnClickListener { finish() }
        btnForward.setOnClickListener { goToUrl(etAddressBar.text.toString()) }

        if (intent.hasExtra(EXTRA_URL)){
            intent.getStringExtra(EXTRA_URL)?.let { url ->
                etAddressBar.setText(url)
                goToUrl(url)
            }
        }else finish()
    }

    private fun goToUrl(url: String) {
        web_view.loadUrl(url)
        bookMarkManager.initView(url)
        showLoadingScreen()
    }

    override fun showLoadingScreen() {
        progressBar.isVisible = true
    }

    override fun hideLoadingScreen() {
        progressBar.isVisible = false
    }

    companion object{
        private const val EXTRA_URL = "com.dasbikash.api_browser.ui.WebViewActivity.EXTRA_URL"

        fun getIntent(context: Context,url:String):Intent? =
            if (URLUtil.isNetworkUrl(url)){
                Intent(context,WebViewActivity::class.java).apply {
                    putExtra(EXTRA_URL,url)
                }
            }else null
    }
}