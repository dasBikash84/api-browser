package com.dasbikash.api_browser.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.dasbikash.api_browser.R
import com.dasbikash.api_browser.ui.custom.BasePathChangeView
import com.dasbikash.api_browser.ui.custom.JsonElementView
import com.dasbikash.api_browser.ui.custom.UrlParamInputView
import com.dasbikash.api_browser.ui.fragments.view_models.BrowserViewModel
import com.dasbikash.api_browser.ui.utils.PrefUtils
import com.dasbikash.api_browser.ui.utils.isInternalUrl
import com.dasbikash.api_browser.ui.utils.isParameterizedLink
import com.dasbikash.api_browser.ui.utils.navigateForwardWithUrl
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_browser.*

class BrowserFragment : BaseFragment() {

    private val args: BrowserFragmentArgs by navArgs()

    val pageApiPath: String? by lazy { args.argPath ?: PrefUtils.getBasePath()}
    private var jsonElement: JsonElement? = null

    private fun getInitJsonObject(): JsonElement? =
        args.argJsonObj?.let { JsonParser.parseString(it)}

    private val browserViewModel by viewModels<BrowserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jsonElement = getInitJsonObject()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnBack.setOnLongClickListener {
            BasePathChangeView(requireContext()).show()
            true
        }
        btnForward.setOnClickListener {
            requestApiData(pageApiPath ?: PrefUtils.getBasePath())
        }
        pageApiPath?.let { bookMarkManager.initView(it) }

        initDataDisplay()
    }

    private fun initDataDisplay() {

        etAddressBar.isVisible = !pageApiPath.isNullOrBlank()
        btnForward.isVisible = !pageApiPath.isNullOrBlank()
        jsonElementView.isVisible = false

        pageApiPath?.let { etAddressBar.setText(it) }

        if (jsonElement != null){
            displayJsonElement()
        }else{
            (pageApiPath ?: PrefUtils.getBasePath()).let { url ->
                if (isParameterizedLink(url)){
                    vhRoot.addView(
                        UrlParamInputView(requireContext(),url){
                            etAddressBar.setText(it)
                            requestApiData(url)
                        }
                    )
                }else{
                    requestApiData(url)
                }
            }
        }
    }

    private fun displayJsonElement(){
        jsonElement?.let {
            jsonElementView.initView(it)
            jsonElementView.isVisible = true
        }
    }

    private fun requestApiData(url:String) {
        vhRoot.children.forEach {
            it.isVisible = (it !is UrlParamInputView)
        }
        if (isInternalUrl(url)) {
            requestNetworkData(
                { browserViewModel.getDataByUrl(url) },
                { repoSearchResponse ->
                    repoSearchResponse?.let {
                        jsonElement = it
                        displayJsonElement()
                    }
                }
            )
        }else {navigateForwardWithUrl(url)}
    }
}