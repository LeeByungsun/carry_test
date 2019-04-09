package com.buggyani.test.network.vo

import com.google.gson.annotations.SerializedName

/**
 * Created by bslee on 2019-02-16.
 */

/**
 * 거래 내역 Data
 */
data class TransactionVo(@SerializedName("amount") var amount: String,
                         @SerializedName("created_at") var created_at: String,
                         @SerializedName("from") var from: String,
                         @SerializedName("to") var to: String)