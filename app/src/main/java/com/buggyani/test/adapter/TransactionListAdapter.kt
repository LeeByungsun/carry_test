package com.buggyani.test.adapter

/**
 * Created by bslee on 2019-02-16.
 */

import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buggyani.test.R
import com.buggyani.test.network.vo.TransactionVo
import kotlinx.android.synthetic.main.item_transaction.view.*

class TransactionListAdapter(transactionData: ObservableArrayList<TransactionVo>) : RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder>() {

    private val TAG = javaClass.simpleName
    private var trList: MutableList<TransactionVo>? = transactionData

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trList!!.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder?, position: Int) {
        val transactionVo = trList!![position]
        Log.e(TAG, "from = " + transactionVo.from)
        holder!!.amount.text = "amount : ${transactionVo.amount}"
        holder.creatTime.text = "creat time : ${transactionVo.created_at}"
        holder.to.text = "to : ${transactionVo.to}"
        holder.from.text = "from : ${transactionVo.to}"
    }

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var amount = view.amount1
        var creatTime = view.created_at1
        var to = view.to1
        var from = view.from1
    }
}