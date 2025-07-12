package android.example.balesuk.ui.activities

import CartAdapter
import android.content.Intent
import android.example.balesuk.databinding.ActivityCartBinding
import android.example.balesuk.model.CartItem
import android.example.balesuk.viewmodel.CartViewModel
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter

    private var currentCartItems: List<CartItem> = emptyList() // ✅ Store items here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // RecyclerView setup
        cartAdapter = CartAdapter(emptyList()) { item, newQuantity ->
            cartViewModel.updateQuantity(item.productId, newQuantity)
        }
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.cartRecyclerView.adapter = cartAdapter

        // Observe cart items
        cartViewModel.cartItems.observe(this, Observer { items ->
            cartAdapter.updateItems(items)
            currentCartItems = items   // <---- ADD THIS LINE
            val total = cartViewModel.getTotalPrice()
            binding.totalPrice.text = "Total: $${String.format("%.2f", total)}"
        })


        // Checkout button

        binding.checkOut.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            intent.putParcelableArrayListExtra("cart_items", ArrayList(currentCartItems)) // ✅ this works
            startActivity(intent)
        }
    }
}
