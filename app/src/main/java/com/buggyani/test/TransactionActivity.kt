package com.buggyani.test

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.buggyani.test.WalletApplication.Companion.key
import com.buggyani.test.WalletApplication.Companion.mHeaders
import com.buggyani.test.WalletApplication.Companion.wallet_api_Server
import com.buggyani.test.databinding.ActivityTransactionBinding
import com.buggyani.test.network.vo.response.TransactionResponse
import com.buggyani.test.util.BPreference
import com.buggyani.test.util.GlobalStatic.HEADER_BALANCE
import com.buggyani.test.util.GlobalStatic.SHARED_IS_WALLET
import com.buggyani.test.util.GlobalStatic.SHARED_SECRET
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_transaction.*

/**
 * Created by bslee on 2019-02-16.
 */
class TransactionActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityTransactionBinding
    private var isWallet = false
    private var mPref: BPreference? = null
    private val mTestAddress: String = "tRTyzYPdKmO7kyfJg7QDvfAaqnON3rcl" // for test
    private val mCtx: TransactionActivity
        get() = this
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)
        isWallet = mPref!!.getBooleanValue(SHARED_IS_WALLET, false)
        intiUI()
    }

    init {
        mPref = BPreference.getInstance(this)
    }

    /**
     * init ui
     */
    private fun intiUI() {
        if (!isWallet) binding.sendTranscetion.isEnabled = false
        binding.address.setText(mTestAddress)
        binding.sendTranscetion.setOnClickListener { sendTransaction() }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    /**
     * send transaction
     */
    private fun sendTransaction() {
        if (mHeaders[HEADER_BALANCE] == null) {
            mHeaders[HEADER_BALANCE] = mPref?.getStringValue(SHARED_SECRET, null)
        }
        disposable = wallet_api_Server.sendTransaction(mHeaders, key, binding.address.text.toString(), binding.amount.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: TransactionResponse ->
                    val balance = t.balance
                    val amountTr = t.transaction.amount
                    Log.e(TAG, "balance = $balance")
                    Log.e(TAG, "amouont = $amountTr")
                    amount.setText("")
                    Toast.makeText(mCtx, "Send OK", Toast.LENGTH_SHORT).show()
                }, { t: Throwable? -> t!!.printStackTrace() })
    }
}
