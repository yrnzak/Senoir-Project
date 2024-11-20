package com.org.jordanapp.adapaters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.org.jordanapp.R
import com.org.jordanapp.datamodel.Transaction
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(private val context: Context, private val transactions: List<Transaction>) :
    android.widget.BaseAdapter() {

    override fun getCount(): Int = transactions.size

    override fun getItem(position: Int): Any = transactions[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false)

        val transaction = transactions[position]

        val tvItemName = view.findViewById<TextView>(R.id.tvItemName)
        val tvQuantity = view.findViewById<TextView>(R.id.tvQuantity)
        val tvTransactionDate = view.findViewById<TextView>(R.id.tvTransactionDate)
        val tvTotalCost = view.findViewById<TextView>(R.id.tvTotalCost)


        val transactionDate = transaction?.transactionDate?.toDate()
        val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(transactionDate)
        tvTransactionDate.text = "Date: $formattedDate"

        tvItemName.text = transaction.itemName
        tvQuantity.text = "Quantity: ${transaction.quantity}"

        tvTotalCost.text = "Total Cost: $${transaction.totalCost}"

        return view
    }
}