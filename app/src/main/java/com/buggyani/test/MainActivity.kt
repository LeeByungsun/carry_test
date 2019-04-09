package com.buggyani.test

import android.app.DatePickerDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.buggyani.test.WalletApplication.Companion.FCM_Token
import com.buggyani.test.WalletApplication.Companion.key
import com.buggyani.test.WalletApplication.Companion.mHeaders
import com.buggyani.test.WalletApplication.Companion.secret
import com.buggyani.test.WalletApplication.Companion.wallet_api_Server
import com.buggyani.test.adapter.TransactionListAdapter
import com.buggyani.test.databinding.ActivityMainBinding
import com.buggyani.test.network.vo.TransactionVo
import com.buggyani.test.network.vo.response.BalanceResponse
import com.buggyani.test.network.vo.response.TransactionListResponse
import com.buggyani.test.network.vo.response.WalletResponse
import com.buggyani.test.util.BPreference
import com.buggyani.test.util.GlobalStatic.HEADER_BALANCE
import com.buggyani.test.util.GlobalStatic.SHARED_IS_WALLET
import com.buggyani.test.util.GlobalStatic.SHARED_KEY
import com.buggyani.test.util.GlobalStatic.SHARED_SECRET
import com.buggyani.test.util.Utils.Companion.getData
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


/**
 * Created by bslee on 2019-02-16.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityMainBinding
    private var isWallet = false
    private var mPref: BPreference? = null
    private var mTransactionData = ObservableArrayList<TransactionVo>()
    private lateinit var mTransactionAdapter: TransactionListAdapter
    private var disposable: Disposable? = null
    private val mCtx: MainActivity
        get() = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        isWallet = mPref!!.getBooleanValue(SHARED_IS_WALLET, false)
        setFCMToken()
        intiUI()
    }

    override fun onResume() {
        super.onResume()
        if (!balance.text.equals("0")) {
            getBalance()
        }
        if (mTransactionData.size > 0) {
            getTransactionList(null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    init {
        mPref = BPreference.getInstance(this)
    }

    /**
     * ui init
     */
    private fun intiUI() {
        if (isWallet) {
            key = mPref!!.getStringValue(SHARED_KEY, "")!!
            secret = mPref!!.getStringValue(SHARED_SECRET, "")!!
            binding.creatWallet.isEnabled = false
            getBalance()
        }
        initRecyclerView(mTransactionData)
        creatWallet.setOnClickListener { sendCreateWallet() }
        getBalance.setOnClickListener { getBalance() }
        sendTransaction.setOnClickListener { sendActivity() }
        getTransactionLists.setOnClickListener {
            getDate()
        }
    }

    /**
     * recyclerview init
     */
    private fun initRecyclerView(data: ObservableArrayList<TransactionVo>) {
        mTransactionAdapter = TransactionListAdapter(data)
        transaction_recyclerview.adapter = mTransactionAdapter
        transaction_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
    }

    /**
     * send put wallet
     */
    private fun sendCreateWallet() {
        disposable = wallet_api_Server.putWallet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: WalletResponse? ->
                    isWallet = true
                    key = t!!.key
                    secret = t.secret
                    mPref!!.applay {
                        setValue(SHARED_KEY, key)
                        setValue(SHARED_SECRET, secret)
                        setValue(SHARED_IS_WALLET, isWallet)
                    }
                    /*    mPref!!.setValue(SHARED_KEY, key)
                        mPref!!.setValue(SHARED_SECRET, secret)
                        mPref!!.setValue(SHARED_IS_WALLET, isWallet)*/
                    creatWallet.isEnabled = false
                    address.text = key
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    /**
     * send get balance
     */
    private fun getBalance() {
        if (mHeaders[HEADER_BALANCE] == null) {
            mHeaders[HEADER_BALANCE] = mPref?.getStringValue(SHARED_SECRET, null)
        }
        disposable = wallet_api_Server.getBalance(mHeaders, key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: BalanceResponse -> balance.text = t.balance }, { t: Throwable? -> t!!.printStackTrace() })
    }

    /**
     * get transaction list (all/after)
     */
    private fun getTransactionList(after: String? = null) {
        if (mHeaders[HEADER_BALANCE] == null) {
            mHeaders[HEADER_BALANCE] = mPref?.getStringValue(SHARED_SECRET, null)
        }

        disposable = wallet_api_Server.getTransactionList(mHeaders, key, after)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: TransactionListResponse? ->
                    mTransactionData.clear()
                    t!!.data.forEach {
                        mTransactionData.add(it)

                        it.apply {
                            Log.e(TAG, "-----------------------------------")
                            Log.e(TAG, "amount = " + amount)
                            Log.e(TAG, "from = " + from)
                            Log.e(TAG, "to = " + to)
                            Log.e(TAG, "created_at = " + created_at)
                            Log.e(TAG, "-----------------------------------")
                        }

                    }
                    mTransactionAdapter.notifyDataSetChanged()
                }, { t: Throwable? -> t!!.printStackTrace() })
    }


    private fun getDate() {
        val c = Calendar.getInstance()
        val cyear = c.get(Calendar.YEAR)
        val cmonth = c.get(Calendar.MONTH)
        val cday = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(mCtx, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val date = getData(year, monthOfYear + 1, dayOfMonth)
            getTransactionList(date)
        }, cyear, cmonth, cday)

        dpd.show()
    }

    /**
     * send activity
     */
    private fun sendActivity() {
        val intent = Intent(this, TransactionActivity::class.java)
        startActivity(intent)
    }

    private fun setFCMToken() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            Log.e(TAG, "FCM_Token new : " + instanceIdResult.token)
            FCM_Token = instanceIdResult.token
        }
    }
}
