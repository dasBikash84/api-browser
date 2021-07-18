package com.dasbikash.api_browser.ui.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.dasbikash.api_browser.data.models.response.base.RemoteResponse
import com.dasbikash.api_browser.ui.BaseActivity
import com.dasbikash.api_browser.ui.LiveDataResource
import com.dasbikash.api_browser.ui.custom.ApiErrorHandlerView
import com.dasbikash.api_browser.ui.utils.showToast
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

abstract class BaseFragment : Fragment() {

    private val TAG by lazy { this.javaClass.simpleName }

    protected fun log(message: String) {
        Logger.d("$TAG : $message")
    }

    protected fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        context?.let { it.showToast(message, duration) }
    }

    protected fun showLoadingScreen(){
        try{ (activity as? BaseActivity)?.showLoadingScreen() }catch (ex:Throwable){}
    }
    protected fun hideLoadingScreen(){
        try{ (activity as? BaseActivity)?.hideLoadingScreen() }catch (ex:Throwable){}
    }

    protected fun <T> requestNetworkData(
        request:()->LiveData<LiveDataResource<RemoteResponse<T>>>,
        handleResponse:(T?)->Unit
    ){
        lifecycleScope.launch {
            request.invoke().observe(viewLifecycleOwner){
                when(it?.status){
                    LiveDataResource.Status.LOADING -> {showLoadingScreen()}
                    LiveDataResource.Status.SUCCESS -> {
                        hideLoadingScreen()
                        handleResponse(it.data?.payload)
                    }
                    LiveDataResource.Status.ERROR -> {
                        hideLoadingScreen()
                        handleResponseError(it.data)
                    }
                    null -> {}
                }
            }
        }
    }

    protected fun handleResponseError(errorResponse: RemoteResponse<*>?){
        ApiErrorHandlerView(requireContext(),errorResponse).show()
    }

}