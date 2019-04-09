package com.buggyani.test.network.vo.response

import com.google.gson.annotations.SerializedName

/**
 * Created by bslee on 2019-02-16.
 */

/**
 * 지갑생성 응답
 */
data class WalletResponse(@SerializedName("key") var key: String,
                          @SerializedName("secret") var secret: String)