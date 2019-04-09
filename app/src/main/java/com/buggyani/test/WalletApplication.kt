package com.buggyani.test

import android.app.Application
import android.content.Context
import android.os.Build
import com.buggyani.test.fcm.NotificationManager
import com.buggyani.test.network.WalletAPIInfo
import com.buggyani.test.util.BPreference
import com.buggyani.test.util.GlobalStatic.CREATED_NOTIFICATION_CHANNEL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by bslee on 2019-02-16.
 */
class WalletApplication : Application() {
    private val TAG = javaClass.simpleName
    private var mPref: BPreference? = null
    private var context: Context? = null


    override fun onCreate() {
        super.onCreate()
        instance = this
        this.context = applicationContext
        setRetrofit_Server(true)
        mPref = BPreference.getInstance(instance!!.applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !mPref!!.getBooleanValue(CREATED_NOTIFICATION_CHANNEL, false)) {
            NotificationManager.createChannel(this)
            mPref!!.setValue(CREATED_NOTIFICATION_CHANNEL, true)
        }
    }

    /**
     * retrofit setting
     */
    private fun setRetrofit_Server(debug: Boolean) {
        val httpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.BODY
        // add your other interceptors …
        // add logging as last interceptor
//        httpClient.addInterceptor(logging)  // <-- this is the important line!
//        logging.level = HttpLoggingInterceptor.Level.HEADERS
        httpClient.addInterceptor(logging)

        retrofit_Server = if (debug) {
            Retrofit.Builder().baseUrl(WalletAPIInfo.WalletAPI_URL)
                    .client(httpClient.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).build()
        } else {
            Retrofit.Builder().baseUrl(WalletAPIInfo.WalletAPI_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).build()
        }

        wallet_api_Server = retrofit_Server!!.create(WalletAPIInfo::class.java)
    }


    companion object {

        var instance: WalletApplication? = null
            private set
        var key = "" //지갑주소
        var secret = ""//지갑 key
        var FCM_Token = ""
        var retrofit_Server: Retrofit? = null
        lateinit var wallet_api_Server: WalletAPIInfo
        var mHeaders: MutableMap<String, String?> = mutableMapOf()
    }

}
