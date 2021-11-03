package com.android.ssm.presentation.issues

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.ssm.IssuesListQuery
import com.android.ssm.data.database.Rating
import com.android.ssm.domain.usecase.GetIssuesListUseCase
import com.android.ssm.domain.usecase.GetRatingDataUseCase
import com.android.ssm.util.Variables
import com.android.ssm.util.ViewState
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class IssuesViewModel @Inject constructor(private val getIssuesListUseCase: GetIssuesListUseCase,private val getRatingDataUseCase: GetRatingDataUseCase) : ViewModel() {

    private val _issuesList by lazy { MutableLiveData<ViewState<Response<IssuesListQuery.Data>>>() }
    val issuesList: LiveData<ViewState<Response<IssuesListQuery.Data>>>
        get() = _issuesList

    val isLoad = MutableLiveData<Boolean>()
    val retryClickedLiveData = MutableLiveData<Boolean>()
    val getDatabaseRatingLiveData = MutableLiveData<List<Rating>?>()

    init {
        isLoad.value = false
        getRatingsData()
    }

    fun queryCharactersList(page: Int, pageSize:Int,brandId: String) = viewModelScope.launch {
        _issuesList.postValue(ViewState.Loading())
        try {
            if (Variables.isNetworkConnected) {
                getIssuesListUseCase.getIssuesListData(page,pageSize,brandId).let { data ->
                    isLoad.value = true
                    _issuesList.postValue(ViewState.Success(data))
                }
            }
            else {
                isLoad.value = true
                _issuesList.postValue(ViewState.Error("No Internet Connection"))
            }

        } catch (e: ApolloException) {
            isLoad.value = true
            Log.d("ApolloException", "Failure", e)
            _issuesList.postValue(ViewState.Error("Error fetching characters"))
        }
    }

    fun insertRatingdata(rating :Rating) = viewModelScope.launch {

        try {
            withContext(Dispatchers.IO) {
                getRatingDataUseCase.insertRatingData(rating)
            }
        }
        catch (exception :Exception) {
            Log.d("RoomException", "insertRatingdata$exception")
        }


    }

    private fun getRatingsData() =viewModelScope.launch {

        try {
             withContext(Dispatchers.IO) {
                 getRatingDataUseCase.getRatingData().let { ratings->
                     getDatabaseRatingLiveData.postValue(ratings)
                 }
            }
        }
        catch (exception :Exception) {
            Log.d("RoomException", "getRatingsData$exception")
        }

    }

    fun onButtonRetryClicked() {
        isLoad.value = false
        retryClickedLiveData.value = true
    }


}