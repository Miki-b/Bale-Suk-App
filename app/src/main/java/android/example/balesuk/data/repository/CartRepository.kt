package android.example.balesuk.data.repository

import android.example.balesuk.data.CartItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CartRepository {
    private val cartItems = MutableLiveData<List<CartItem>>(emptyList())

    fun getCartItems(): LiveData<List<CartItem>> = cartItems

    fun addItem(item: CartItem) {
        val currentList = cartItems.value.orEmpty()
        val updatedList = if (currentList.any { it.productId == item.productId }) {
            currentList.map {
                if (it.productId == item.productId)
                    it.copy(quantity = it.quantity + item.quantity)
                else it
            }
        } else {
            currentList + item
        }
        cartItems.value = updatedList
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        val updatedList = cartItems.value.orEmpty().map {
            if (it.productId == productId) it.copy(quantity = newQuantity) else it
        }
        cartItems.value = updatedList
    }

    fun clearCart() {
        cartItems.value = emptyList()
    }

    fun getTotalPrice(): Double {
        return cartItems.value.orEmpty().sumOf { it.price * it.quantity }
    }
}
