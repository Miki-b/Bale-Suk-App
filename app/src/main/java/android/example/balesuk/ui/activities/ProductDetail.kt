package android.example.balesuk.ui.activities

import ImageSliderAdapter
import Products
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.example.balesuk.R
import android.example.balesuk.databinding.ActivityCartBinding
import android.example.balesuk.databinding.ActivityProductDetailBinding
import androidx.viewpager2.widget.ViewPager2

class ProductDetail : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        val product = intent.getParcelableExtra<Products>("product")

        product?.let {
            binding.productName.text = it.name
            binding.productPrice.text = "$${it.price}"
            binding.productDescription.text = it.description

            val imageUrls = it.images?.map { img -> img.path } ?: emptyList()
            binding.imageSlider.adapter = ImageSliderAdapter(imageUrls)
        }
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //setContentView(R.layout.activity_product_detail)




    }
}