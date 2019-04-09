package com.buggyani.test.network.vo.response

import com.buggyani.test.network.vo.TransactionVo
import com.google.gson.annotations.SerializedName

/**
 * Created by bslee on 2019-02-16.
 */
/**
 * 송금 응답
 */
data class TransactionResponse(@SerializedName("balance") var balance: String
                               , @SerializedName("transaction") var transaction: TransactionVo)