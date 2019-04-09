package com.buggyani.test

import com.buggyani.test.network.WalletAPIInfo
import com.buggyani.test.network.vo.response.BalanceResponse
import com.buggyani.test.network.vo.response.TransactionListResponse
import com.buggyani.test.network.vo.response.TransactionResponse
import com.buggyani.test.network.vo.response.WalletResponse
import com.buggyani.test.util.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    companion object {
        var key: String = ""
        var secret: String? = null
        var mHeaders: MutableMap<String, String?> = mutableMapOf()
    }


    private fun initRetrofit() {
        setRetrofit_Server(true)
        WalletApplication.wallet_api_Server = WalletApplication.retrofit_Server!!.create(WalletAPIInfo::class.java)
    }

    private fun setRetrofit_Server(debug: Boolean) {
        val httpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        WalletApplication.retrofit_Server = if (debug) {
            Retrofit.Builder().baseUrl(WalletAPIInfo.WalletAPI_URL)
                    .client(httpClient.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).build()
        } else {
            Retrofit.Builder().baseUrl(WalletAPIInfo.WalletAPI_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).build()
        }

        WalletApplication.wallet_api_Server = WalletApplication.retrofit_Server!!.create(WalletAPIInfo::class.java)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Before
    fun init() {
        initRetrofit()
    }

    @Test
    fun getDateTest() {
        assertEquals(Utils.getData(2019, 2, 5), "2019-02-05T00:00:00%2b09:00")
    }

    @Test
    fun test1createWalletTest() {
        WalletApplication.wallet_api_Server.putWallet()
                .subscribe({ t: WalletResponse ->
                    key = t.key
                    secret = t.secret
                    mHeaders["X-Wallet-Secret"] = secret
                    assertTrue(key!!.length == 32)
                    assertTrue(secret!!.length == 128)
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    @Test
    fun test2getBalanceTest() {
        var balance: String? = null
        WalletApplication.wallet_api_Server.getBalance(mHeaders, key)
                .subscribe({ t: BalanceResponse -> assertTrue(balance!!.length != null) }, { t: Throwable? -> t!!.printStackTrace() })
    }

    @Test
    fun test3sendTransaction() {
        WalletApplication.wallet_api_Server.sendTransaction(mHeaders, key, "tRTyzYPdKmO7kyfJg7QDvfAaqnON3rcl", "50")
                .subscribe({ t: TransactionResponse ->
                    assertTrue(t!!.transaction.created_at.length > 20)
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    @Test
    fun test4getTransaction() {
        WalletApplication.wallet_api_Server.getTransactionList(mHeaders, key, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: TransactionListResponse? ->
                    assertTrue(t!!.data.isEmpty())
                }, { t: Throwable? -> t!!.printStackTrace() })
    }


}