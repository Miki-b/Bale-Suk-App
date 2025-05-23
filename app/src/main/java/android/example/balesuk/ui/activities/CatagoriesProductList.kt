package android.example.balesuk.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.example.balesuk.R
import android.example.balesuk.data.CartItem
import android.example.balesuk.data.Product
import android.example.balesuk.databinding.ActivityCatagoriesProductListBinding
import android.example.balesuk.ui.adapters.ProductCardAdapter
import android.example.balesuk.ui.viewmodel.CartViewModel
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import coil.load

class CatagoriesProductList : AppCompatActivity() {
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var products: List<Product>
    private lateinit var productCardAdapter: ProductCardAdapter
    private val context:String="ProductListPage"
    private lateinit var binding:  ActivityCatagoriesProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatagoriesProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        //setContentView(R.layout.activity_catagories_product_list) // Or your correct layout file

        val recyclerView =binding.GridRecyclerview

        products = listOf(
            Product("1", "Clothing", "https://img.freepik.com/free-photo/still-life-spring-wardrobe-switch_23-2150479001.jpg", 100.0, 0),
            Product("2", "Electronics", "https://img.freepik.com/premium-photo/well-organised-white-office-objects-colorful-background_264197-16469.jpg", 150.0, 0),
            Product("3", "Foods", "https://img.freepik.com/premium-photo/emergency-survival-food-set-white-kitchen-table_571379-3659.jpg", 80.0, 0),
            Product("4", "Drinks", "https://img.freepik.com/premium-vector/soft-drinks-liquid-snacks-with-sugar-soda-drinks-vector-colored-hills-steel-bottles_80590-23567.jpg", 50.0, 0),
            Product("5", "Clothing", "https://img.freepik.com/free-photo/still-life-spring-wardrobe-switch_23-2150479001.jpg", 100.0, 0),
            Product("6", "Electronics", "https://img.freepik.com/premium-photo/well-organised-white-office-objects-colorful-background_264197-16469.jpg", 150.0, 0),
            Product("7", "Foods", "https://img.freepik.com/premium-photo/emergency-survival-food-set-white-kitchen-table_571379-3659.jpg", 80.0, 0),
            Product("8", "Drinks", "https://img.freepik.com/premium-vector/soft-drinks-liquid-snacks-with-sugar-soda-drinks-vector-colored-hills-steel-bottles_80590-23567.jpg", 50.0, 0),
            Product("9", "Clothing", "https://img.freepik.com/free-photo/still-life-spring-wardrobe-switch_23-2150479001.jpg", 100.0, 0),
            Product("10", "Electronics", "https://img.freepik.com/premium-photo/well-organised-white-office-objects-colorful-background_264197-16469.jpg", 150.0, 0),
            Product("11", "Foods", "https://img.freepik.com/premium-photo/emergency-survival-food-set-white-kitchen-table_571379-3659.jpg", 80.0, 0),
            Product("12", "Drinks", "https://img.freepik.com/premium-vector/soft-drinks-liquid-snacks-with-sugar-soda-drinks-vector-colored-hills-steel-bottles_80590-23567.jpg", 50.0, 0),
            Product("13", "Clothing", "https://img.freepik.com/free-photo/still-life-spring-wardrobe-switch_23-2150479001.jpg", 100.0, 0),
            Product("14", "Electronics", "https://img.freepik.com/premium-photo/well-organised-white-office-objects-colorful-background_264197-16469.jpg", 150.0, 0),
            Product("15", "Foods", "https://img.freepik.com/premium-photo/emergency-survival-food-set-white-kitchen-table_571379-3659.jpg", 80.0, 0),
            Product("16", "Drinks", "https://img.freepik.com/premium-vector/soft-drinks-liquid-snacks-with-sugar-soda-drinks-vector-colored-hills-steel-bottles_80590-23567.jpg", 50.0, 0),


            )

        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns
        productCardAdapter = ProductCardAdapter(
            products,
            context,
            onAddToCartClicked = { product ->
                val item = CartItem(
                    productId = product.productId,
                    name = product.productName,
                    price = product.productPrice,
                    imageUrl = product.productimageURL,
                    quantity = 1
                )
                cartViewModel.addItem(item)
            } ,
            onClick = { product ->
                // Handle click, e.g., navigate to product details
                // Example:
                val intent = Intent(this, ProductDetail::class.java)

                startActivity(intent)
                Toast.makeText(this, "Clicked: ${product.productName}", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter=productCardAdapter

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
            cartIcon?.load("https://drive.google.com/file/d/1V_pF43mHtSJRsfh0L_Hpa4N_9Uhi_N_t/view?usp=sharing")
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