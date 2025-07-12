package android.example.balesuk.ui.activities

import ProductViewModel
import Products
import android.annotation.SuppressLint
import android.content.Intent
import android.example.balesuk.R
import android.example.balesuk.data.api.SessionManager
import android.example.balesuk.model.CartItem
import android.example.balesuk.data.models.Catagory

import android.example.balesuk.databinding.ActivityHomeBinding
import android.example.balesuk.ui.adapters.ProductCardAdapter
import android.example.balesuk.ui.adapters.circularImageTextAdapter
import android.example.balesuk.viewmodel.CartViewModel
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
import coil.load
import coil.transform.RoundedCornersTransformation

class Home : AppCompatActivity() {
    private val cartViewModel: CartViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var  catagory:Catagory
    private lateinit var binding: ActivityHomeBinding
    private lateinit var circularImageTextRecyclerView: RecyclerView
    private lateinit var circularImageTextAdapter: circularImageTextAdapter
    private lateinit var productCardAdapter: ProductCardAdapter
    private lateinit var productCardRecyclerView: RecyclerView
    private lateinit var newProductCardAdapter: ProductCardAdapter
    private lateinit var newProductCardRecyclerView: RecyclerView

    private var allProducts: List<Products> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        setupCategoryRecycler()
        setupProductRecycler()
        observeProductViewModel()
        setupNewProductRecycler()

        val bannerImageView: ImageView = findViewById(R.id.bannerImage)
        bannerImageView.load("https://images.all-free-download.com/images/thumbjpg/ecommerce_website_banner_template_shoppers_sketch_6920121.jpg") {
            crossfade(true)
            transformations(RoundedCornersTransformation(16f))
        }
        productViewModel.getAllProducts()

        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("Age", 1)
        intent.putExtra("school", "Addis Ababa University")
        setResult(RESULT_OK, intent)

        binding.fabCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        productViewModel.products.observe(this, Observer { productList ->
            allProducts = productList ?: listOf()

            productCardAdapter.updateProducts(allProducts)

            val newProducts = allProducts.takeLast(5) // or filter using createdAt
            newProductCardAdapter.updateProducts(newProducts)
        })
        val cartBadge: TextView = findViewById(R.id.cart_badge)

        cartViewModel.cartItems.observe(this) { items ->
            cartBadge.text = items.size.toString()
            cartBadge.visibility = if (items.isNotEmpty()) View.VISIBLE else View.GONE
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
    private fun setupNewProductRecycler() {
        newProductCardRecyclerView = findViewById(R.id.productsCardRecyclerView2)
        newProductCardRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        newProductCardAdapter = ProductCardAdapter(
            productList = listOf(),
            Screen = "New", // optional label if you handle different screens
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
        newProductCardRecyclerView.adapter = newProductCardAdapter
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

            // Styling the search view
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
        }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {  // Replace this ID with logout action ID in `menu_main.xml`
                val sessionManager = SessionManager(this)
                sessionManager.clearSession()
                startActivity(Intent(this, Login::class.java))
                finish()  // finish Home activity
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
