package com.buggyani.test.network.vo.response

import com.google.gson.annotations.SerializedName

/**
 * Created by bslee on 2019-02-16.
 */
/**
 * 지갑 잔액 응답
 */
data class BalanceResponse(@SerializedName("balance") var balance: String,
                           @SerializedName("transaction") var transaction: String
)