package com.dasbikash.api_browser.ui.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.ImageResult
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.ui.WebViewActivity

fun ImageView.loadNetWorkImage(url: String,onSuccess:(()->Unit)?=null,onError:(()->Unit)?=null){
    context?.let {
        if (URLUtil.isNetworkUrl(url)) {
            ImageRequest
                .Builder(it)
                .data(url)
                .lifecycle(findViewTreeLifecycleOwner())
                .target(this)
                .listener(object : ImageRequest.Listener{
                    override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
                        onSuccess?.invoke()
                        super.onSuccess(request, metadata)
                    }
                    override fun onError(request: ImageRequest, throwable: Throwable) {
                        onError?.invoke()
                        super.onError(request, throwable)
                    }
                })
                .build().let { imageRequest ->
                    ImageLoader.invoke(context).enqueue(imageRequest)
                }
        }
    }
}

fun View.navigateToBrowserFragment(url: String?,jsonString:String?) {
    findViewTreeLifecycleOwner()?.lifecycleScope?.launchWhenResumed {
        findNavController().navigate(
            R.id.browseFragment,
            bundleOf(
                "arg_path" to (url ?: ""),
                "arg_json_obj" to jsonString
            )
        )
    }
}

private fun View.navigateToWebView(url: String) {
    context?.apply {
        WebViewActivity.getIntent(this, url)?.let { intent -> startActivity(intent) }
    }
}

fun isParameterizedLink(url:String) =
    URLUtil.isNetworkUrl(url) && url.contains("{")

fun isInternalUrl(url: String) = url.startsWith(PrefUtils.getBasePath(),true)

fun Fragment.navigateForwardWithUrl(url: String) = view?.navigateForwardWithUrl(url)

fun View.navigateForwardWithUrl(url: String){
    if (!isParameterizedLink(url) && !isInternalUrl(url)) navigateToWebView(url)
    else navigateToBrowserFragment(url,null)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(this, message, duration).show()
    }
}

fun TextView.setLinkStyle(){
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    setTextColor(Color.parseColor("#900000FF"))
}