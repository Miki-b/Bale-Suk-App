package android.example.balesuk.ui.activities

import ProductViewModel
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.example.balesuk.model.CartItem
import android.example.balesuk.databinding.ActivityCatagoriesProductListBinding
import android.example.balesuk.ui.adapters.ProductCardAdapter
import android.example.balesuk.viewmodel.CartViewModel
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager

class CatagoriesProductList : AppCompatActivity() {
    private val cartViewModel: CartViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels() // ✅ correct viewmodel
    private lateinit var productCardAdapter: ProductCardAdapter
    private lateinit var binding: ActivityCatagoriesProductListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatagoriesProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val categorySlug = intent.getStringExtra("category_slug")
        if (categorySlug != null) {
            productViewModel.getAllProducts(categorySlug.toInt())
             // ✅ correct usage
        }

        observeProductViewModel()

        val recyclerView = binding.GridRecyclerview
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Start with empty list
        productCardAdapter = ProductCardAdapter(
            listOf(),
            Screen = "ProductListPage",
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
                startActivity(intent)
                Toast.makeText(this, "Clicked: ${product.name}", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerView.adapter = productCardAdapter
    }

    private fun observeProductViewModel() {
        productViewModel.products.observe(this) { productList ->
            productCardAdapter.updateProducts(productList)
        }

        productViewModel.error.observe(this) {
            Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
        }
    }
}
