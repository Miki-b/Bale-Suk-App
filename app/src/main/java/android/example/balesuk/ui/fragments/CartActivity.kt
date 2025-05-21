package android.example.balesuk.ui

import CartAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.example.balesuk.databinding.ActivityCartBinding
import android.example.balesuk.ui.viewmodel.CartViewModel
//import android.example.balesuk.ui.adapter.CartAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartAdapter = CartAdapter(emptyList()) { item, newQuantity ->
            cartViewModel.updateQuantity(item.productId, newQuantity)
        }

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = cartAdapter

        binding.addButton.setOnClickListener {
            // Example item to add (you can customize this logic)
            cartViewModel.addItem(
                android.example.balesuk.data.CartItem(
                    productId = "123",
                    name = "Sample Product",
                    price = 9.99,
                    imageUrl = "https://via.placeholder.com/150",
                    quantity = 1
                )
            )
        }

        cartViewModel.cartItems.observe(this, Observer { items ->
            cartAdapter.updateItems(items)
            val total = cartViewModel.getTotalPrice()
            binding.totalPrice.text = "Total: $${String.format("%.2f", total)}"
        })
    }
}
