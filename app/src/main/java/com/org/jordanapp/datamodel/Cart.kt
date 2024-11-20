package com.org.jordanapp.datamodel

object Cart {
    val cartItems = ArrayList<CartItem>()

    fun addItem(item: CartItem) {
        // Check if the item already exists in the cart
        val existingItem = cartItems.find { it.name == item.name }
        if (existingItem != null) {
            // If item exists, update the quantity
            existingItem.quantity += item.quantity
        } else {
            // If item does not exist, add to the cart
            cartItems.add(item)
        }
    }

    fun getTotalPrice(): Double {
        return cartItems.sumByDouble { it.price * it.quantity }
    }

    fun getItems(): List<CartItem> {
        return cartItems
    }

    fun getTotalCost(): Double {
        return cartItems.sumByDouble { it.price * it.quantity }
    }

    // Function to clear all items in the cart
    fun clearItems() {
        cartItems.clear()
    }
}
