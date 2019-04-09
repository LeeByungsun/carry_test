package com.buggyani.test.network.vo.response

import com.buggyani.test.network.vo.TransactionVo
import com.google.gson.annotations.SerializedName

/**
 * Created by bslee on 2019-02-16.
 */

/**
 * 거래 내용 응답
 */
data class TransactionListResponse(@SerializedName("data") var data: List<TransactionVo>)