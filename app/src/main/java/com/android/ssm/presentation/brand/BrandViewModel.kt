package com.android.ssm.presentation.brand

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.ssm.BrandsListQuery
import com.android.ssm.domain.usecase.GetBrandListUseCase
import com.android.ssm.util.Variables
import com.android.ssm.util.ViewState
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class BrandViewModel  @Inject constructor(private val getUserDetailsUseCase: GetBrandListUseCase) : ViewModel() {

    private val _brandsList by lazy { MutableLiveData<ViewState<Response<BrandsListQuery.Data>>>() }
    val brandsList: LiveData<ViewState<Response<BrandsListQuery.Data>>>
        get() = _brandsList

    val isLoad = MutableLiveData<Boolean>()
    val retryClickedLiveData = MutableLiveData<Boolean>()

    init {
        isLoad.value = false
    }

    fun queryCharactersList(page:String,pageSize: String) = viewModelScope.launch {
        _brandsList.postValue(ViewState.Loading())
        try {
            if (Variables.isNetworkConnected) {
                getUserDetailsUseCase.getBrandListData(page,pageSize).let { data ->
                    isLoad.value = true
                    _brandsList.postValue(ViewState.Success(data))
                }
            }
            else {
                isLoad.value = true
                _brandsList.postValue(ViewState.Error("No Internet Connection"))
            }

        } catch (e: ApolloException) {
            isLoad.value = true
            Log.d("ApolloException", "Failure", e)
            _brandsList.postValue(ViewState.Error("Error fetching characters"))
        }
    }


    fun onButtonRetryClicked() {
        isLoad.value = false
        retryClickedLiveData.value = true
    }


}