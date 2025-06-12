package android.example.balesuk.ui.activities

import android.example.balesuk.databinding.ActivityOrdersBinding
import android.example.balesuk.model.CartItem     // ✅ Corrected import
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import CartAdapter


class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrdersBinding
    private var cartItems: MutableList<CartItem> = mutableListOf()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedItems = intent.getParcelableArrayListExtra<CartItem>("cart_items") ?: emptyList()
        cartItems = receivedItems.toMutableList()

        setupToolbar()
        setupRecyclerView()
        updateTotal()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(cartItems) { cartItem, newQuantity ->
            val index = cartItems.indexOfFirst { it.productId == cartItem.productId }
            if (index != -1) {
                cartItems[index] = cartItems[index].copy(quantity = newQuantity)
                cartAdapter.updateItems(cartItems)
                updateTotal()
            }
        }
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = cartAdapter
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price * it.quantity }
        binding.tvTotal.text = "Total: $${"%.2f".format(total)}"
    }
}
