package android.example.balesuk.ui.activities

import ImageSliderAdapter
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
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        //setContentView(R.layout.activity_product_detail)
        val imageUrls = listOf(
            "https://img.freepik.com/free-photo/still-life-spring-wardrobe-switch_23-2150479001.jpg",
            "https://img.freepik.com/premium-photo/well-organised-white-office-objects-colorful-background_264197-16469.jpg",
            "https://img.freepik.com/premium-photo/emergency-survival-food-set-white-kitchen-table_571379-3659.jpg"
        )

        val imageSlider = binding.imageSlider
        imageSlider.adapter = ImageSliderAdapter(imageUrls)


    }
}