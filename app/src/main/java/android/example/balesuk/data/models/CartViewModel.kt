package android.example.balesuk.ui.viewmodel

import android.example.balesuk.data.CartItem
import android.example.balesuk.data.repository.CartRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    val cartItems: LiveData<List<CartItem>> = CartRepository.getCartItems()

    fun addItem(item: CartItem) = CartRepository.addItem(item)

    fun updateQuantity(productId: String, newQuantity: Int) =
        CartRepository.updateQuantity(productId, newQuantity)

    fun clearCart() = CartRepository.clearCart()

    fun getTotalPrice(): Double = CartRepository.getTotalPrice()
}
