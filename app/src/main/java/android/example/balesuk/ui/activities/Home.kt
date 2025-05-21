package android.example.balesuk.ui.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.example.balesuk.R
import android.example.balesuk.data.CartItem
import android.example.balesuk.data.Product
import android.example.balesuk.data.circular_image_text
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import android.example.balesuk.databinding.ActivityHomeBinding
import android.example.balesuk.ui.CartActivity
import android.example.balesuk.ui.adapters.circularImageTextAdapter
import android.example.balesuk.ui.adapters.ProductCardAdapter
import android.example.balesuk.ui.viewmodel.CartViewModel
import android.graphics.Color
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import coil.load

class Home : AppCompatActivity() {
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var circularImageTextRecyclerView: RecyclerView
    private lateinit var circularImageTextAdapter: circularImageTextAdapter
    private lateinit var productCardRecyclerView: RecyclerView
    private lateinit var productCardAdapter: ProductCardAdapter

    private lateinit var binding: ActivityHomeBinding
    private lateinit var products: List<Product>  // Store full product list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartViewModel.cartItems.observe(this) { items ->
            Log.d("HomeActivity", "Updated cart: $items")
            Toast.makeText(this, "Cart has ${items.size} item(s)", Toast.LENGTH_SHORT).show()
        }


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.navView
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )

        // Setup category adapter
        val categories = listOf(
            circular_image_text("Clothing", "https://img.freepik.com/free-photo/still-life-spring-wardrobe-switch_23-2150479001.jpg"),
            circular_image_text("Electronics", "https://img.freepik.com/premium-photo/well-organised-white-office-objects-colorful-background_264197-16469.jpg"),
            circular_image_text("Foods", "https://img.freepik.com/premium-photo/emergency-survival-food-set-white-kitchen-table_571379-3659.jpg"),
            circular_image_text("Drinks", "https://img.freepik.com/premium-vector/soft-drinks-liquid-snacks-with-sugar-soda-drinks-vector-colored-hills-steel-bottles_80590-23567.jpg")
        )

        circularImageTextRecyclerView = findViewById(R.id.circularImageTextRecyclerView)
        circularImageTextRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        circularImageTextAdapter = circularImageTextAdapter(categories)
        circularImageTextRecyclerView.adapter = circularImageTextAdapter

        // Products list
        products = listOf(
            Product("1", "Clothing", "https://img.freepik.com/free-photo/still-life-spring-wardrobe-switch_23-2150479001.jpg", 100.0, 0),
            Product("2", "Electronics", "https://img.freepik.com/premium-photo/well-organised-white-office-objects-colorful-background_264197-16469.jpg", 150.0, 0),
            Product("3", "Foods", "https://img.freepik.com/premium-photo/emergency-survival-food-set-white-kitchen-table_571379-3659.jpg", 80.0, 0),
            Product("4", "Drinks", "https://img.freepik.com/premium-vector/soft-drinks-liquid-snacks-with-sugar-soda-drinks-vector-colored-hills-steel-bottles_80590-23567.jpg", 50.0, 0),
        )

        setupProductRecycler(R.id.productsCardRecyclerView)
        setupProductRecycler(R.id.productsCardRecyclerView2)
    }

    private fun setupProductRecycler(recyclerId: Int) {
        val recyclerView = findViewById<RecyclerView>(recyclerId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ProductCardAdapter(products) { product ->
            val item = CartItem(
                productId = product.productId,
                name = product.productName,
                price = product.productPrice,
                imageUrl = product.productimageURL,
                quantity = 1
            )
            cartViewModel.addItem(item)



            Toast.makeText(this, cartViewModel.cartItems.toString(), Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        if (recyclerId == R.id.productsCardRecyclerView) {
            productCardAdapter = adapter  // Save reference to first adapter for filtering
            productCardRecyclerView = recyclerView
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.let {

            val searchItem = it.findItem(R.id.action_search)
            val searchView = searchItem.actionView as? SearchView
            searchView?.isIconified = false

            // Configure searchView here
            searchView?.let {
                val searchPlate = it.findViewById<View>(androidx.appcompat.R.id.search_plate)
                searchPlate?.setBackgroundResource(R.drawable.rounded_search_background)

                // Optional: tweak EditText if you want to round the input field as well
                val searchEditText = it.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(
                    androidx.appcompat.R.id.search_src_text
                )
                searchEditText?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
            val cartItem = menu?.findItem(R.id.action_cart)
            val actionView = cartItem?.actionView
            val cartBadge = actionView?.findViewById<TextView>(R.id.cart_badge)
            val cartIcon = actionView?.findViewById<ImageView>(R.id.cart_icon)
            cartIcon?.load("https://cdn-icons-png.flaticon.com/128/3514/3514491.png")
            cartViewModel.cartItems.observe(this) { items ->
                cartBadge?.text = items.size.toString()
                cartBadge?.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
            }

            cartBadge?.visibility = View.VISIBLE

            val searchAutoComplete = searchView?.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(
                androidx.appcompat.R.id.search_src_text
            )
            searchAutoComplete?.setHintTextColor(Color.GRAY)         // Hint color
            searchAutoComplete?.setTextColor(Color.BLACK)            // Input text color

            searchView?.queryHint = "Search products..."
            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { performSearch(it) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { performSearch(it) }
                    return true
                }
            })

            actionView?.setOnClickListener {
                onOptionsItemSelected(cartItem)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performSearch(query: String) {
        val filtered = products.filter {
            it.productName.contains(query, ignoreCase = true)
        }
        productCardAdapter.updateProducts(filtered)
    }
}
