package android.example.balesuk.ui.activities

import ProductViewModel
import Products
import android.annotation.SuppressLint
import android.content.Intent
import android.example.balesuk.R
import android.example.balesuk.model.CartItem
import android.example.balesuk.data.models.Catagory

import android.example.balesuk.databinding.ActivityHomeBinding
import android.example.balesuk.ui.adapters.ProductCardAdapter
import android.example.balesuk.ui.adapters.circularImageTextAdapter
import android.example.balesuk.ui.viewmodel.CartViewModel
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : AppCompatActivity() {
    private val cartViewModel: CartViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var  catagory:Catagory
    private lateinit var binding: ActivityHomeBinding
    private lateinit var circularImageTextRecyclerView: RecyclerView
    private lateinit var circularImageTextAdapter: circularImageTextAdapter
    private lateinit var productCardAdapter: ProductCardAdapter
    private lateinit var productCardRecyclerView: RecyclerView

    private var allProducts: List<Products> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        setupCategoryRecycler()
        setupProductRecycler()
        observeProductViewModel()

        productViewModel.getAllProducts()

        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("Age", 1)
        intent.putExtra("school", "Addis Ababa University")
        setResult(RESULT_OK, intent)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Already on Home
                    true
                }
                R.id.navigation_dashboard -> {
                    startActivity(Intent(this, Login::class.java))
                    true
                }
                R.id.navigation_notifications -> {
                    startActivity(Intent(this, Login::class.java))
                    true
                }
                else -> false
            }
        }

    }

    private fun setupCategoryRecycler() {
        val categories = listOf(
            Catagory(1,"Clothing", "","","https://img.freepik.com/free-photo/still-life-spring-wardrobe-switch_23-2150479001.jpg"),
            Catagory(2,"Electronics", "","","https://img.freepik.com/premium-photo/well-organised-white-office-objects-colorful-background_264197-16469.jpg"),
            Catagory(3,"Foods","","", "https://img.freepik.com/premium-photo/emergency-survival-food-set-white-kitchen-table_571379-3659.jpg"),
            Catagory(4,"Drinks", "","","https://img.freepik.com/premium-vector/soft-drinks-liquid-snacks-with-sugar-soda-drinks-vector-colored-hills-steel-bottles_80590-23567.jpg")
        )
        circularImageTextAdapter = circularImageTextAdapter(categories) { category ->
            val intent = Intent(this, CatagoriesProductList::class.java)
            intent.putExtra("category_slug", category.id.toString()) // use slug or id as needed
            startActivity(intent)
        }




        circularImageTextRecyclerView = findViewById(R.id.circularImageTextRecyclerView)
        circularImageTextRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        circularImageTextRecyclerView.adapter = circularImageTextAdapter
    }

    private fun setupProductRecycler() {
        productCardRecyclerView = findViewById(R.id.productsCardRecyclerView)
        productCardRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        productCardAdapter = ProductCardAdapter(
            productList = listOf(),
            Screen = "Home",
            onAddToCartClicked = { product ->
                val item = CartItem(
                    productId = product.id.toString(),
                    name = product.name,
                    price = product.price,
                    imageUrl = product.images?.firstOrNull()?.path ?: "",
                    quantity = 1

                )
                cartViewModel.addItem(item)
            },
            onClick = { product ->
                val intent = Intent(this, ProductDetail::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
                Toast.makeText(this, "Clicked: ${product.name}", Toast.LENGTH_SHORT).show()
            }
        )
        productCardRecyclerView.adapter = productCardAdapter
    }

    private fun observeProductViewModel() {
        productViewModel.products.observe(this, Observer { productList ->
            allProducts = productList ?: listOf()
            productCardAdapter.updateProducts(allProducts)
        })

        productViewModel.error.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        })

        productViewModel.loading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.let {
            val searchItem = it.findItem(R.id.action_search)
            val searchView = searchItem.actionView as? SearchView
            searchView?.isIconified = false

            val searchPlate = searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
            searchPlate?.setBackgroundResource(R.drawable.rounded_search_background)

            val searchEditText = searchView?.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(
                androidx.appcompat.R.id.search_src_text
            )
            searchEditText?.setBackgroundColor(Color.TRANSPARENT)
            searchEditText?.setHintTextColor(Color.GRAY)
            searchEditText?.setTextColor(Color.BLACK)

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

            val cartItem = it.findItem(R.id.action_cart)
            val actionView = cartItem?.actionView
            val cartBadge = actionView?.findViewById<TextView>(R.id.cart_badge)
            cartViewModel.cartItems.observe(this) { items ->
                cartBadge?.text = items.size.toString()
                cartBadge?.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
            }

            actionView?.setOnClickListener {
                onOptionsItemSelected(cartItem)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performSearch(query: String) {
//        val filtered = allProducts.filter {
//            it.name.contains(query, ignoreCase = true)
//        }
        productViewModel.getAllProducts(null,query)

    }
}
