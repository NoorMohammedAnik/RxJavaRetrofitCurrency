package com.app.rxjavaretrofitcurrency

import io.reactivex.Observable
import retrofit2.http.GET


/*
Created by Noor Mohammed Anik
*/

interface GetData {
    //Describe the request type and the relative URL//

    @GET("prices?key=5c747d47266df7d2f5a22de3dd6f17c8")
    fun getData() : Observable<List<RetroCrypto>>
}