package com.org.jordanapp.datamodel

import com.google.firebase.Timestamp

data class Transaction(
    val itemName: String = "",
    val quantity: Int = 0,
    val transactionDate: Timestamp? = null,
    val totalCost: Double = 0.0
)