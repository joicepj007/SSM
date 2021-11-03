package com.android.ssm.data

import android.os.Looper
import com.android.ssm.util.Constants.BASE_URL
import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

class SandSMediaApi {

    fun getApolloClient(): ApolloClient {
        check(Looper.myLooper() == Looper.getMainLooper()) {
            "Only the main thread can get the apolloClient instance"
        }

        val okHttpClient = OkHttpClient.Builder().build()
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build()
    }

}