package com.org.jordanapp.datamodel

import java.io.Serializable

data class Shoe(
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val imageUrl: String = ""
): Serializable
