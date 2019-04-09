package com.buggyani.test.network

import com.buggyani.test.network.vo.response.BalanceResponse
import com.buggyani.test.network.vo.response.TransactionListResponse
import com.buggyani.test.network.vo.response.TransactionResponse
import com.buggyani.test.network.vo.response.WalletResponse
import io.reactivex.Observable

import retrofit2.http.*

/**
 * Created by bslee on 2019-02-16.
 */

interface WalletAPIInfo {

    @PUT("wallet")
    fun putWallet(): Observable<WalletResponse>

    @GET("wallets/{key}/balance")
    fun getBalance(@HeaderMap headers: MutableMap<String, String?>, @Path("key") key: String): Observable<BalanceResponse>

    @FormUrlEncoded
    @POST("wallets/{key}/transaction")
    fun sendTransaction(@HeaderMap headers: MutableMap<String, String?>, @Path("key") key: String, @Field("to") to: String, @Field("amount") amount: String): Observable<TransactionResponse>

    @GET("wallets/{key}/transactions")// after 의미가 모호 17일 데이터가 있는경우 17일 이전값을 입력시 null이고 17일 이후 값을 입력시 17일 값이 나옴
    fun getTransactionList(@HeaderMap headers: MutableMap<String, String?>, @Path("key") key: String, @Query(value = "after", encoded = true) after: String? = null): Observable<TransactionListResponse>

    companion object {
        //server Url
        val WalletAPI_URL = "http://192.168.29.252:5000/"
    }
}
