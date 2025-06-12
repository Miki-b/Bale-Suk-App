package android.example.balesuk.ui.activities

import ProductRequest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.example.balesuk.data.api.RetrofitInstance
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.example.balesuk.databinding.ActivityAddProductBinding
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class AddProduct : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private val PICK_IMAGES_REQUEST = 101
    private val selectedImageUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar title
        binding.toolbarTitle.text = "Add Product"

        // Setup category spinners with dummy data
        setupSpinners()

        // Select additional images
        binding.btnSelectImages.setOnClickListener {
            selectAdditionalImages()
        }

        // Submit product button
        binding.btnSubmit.setOnClickListener {
            addProduct()
        }
    }

    private fun setupSpinners() {
        val categories = listOf("Electronics", "Clothing", "Home", "Grocery")
        val subcategories = listOf("Sub1", "Sub2", "Sub3")

        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        val subCategoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subcategories)

        binding.spinnerCategory.adapter = categoryAdapter
        binding.spinnerSubCategory.adapter = subCategoryAdapter
    }

    private fun selectAdditionalImages() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGES_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUris.clear()
            binding.selectedImagesContainer.removeAllViews()

            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    selectedImageUris.add(imageUri)
                    addImageView(imageUri)
                }
            } else if (data?.data != null) {
                val imageUri = data.data!!
                selectedImageUris.add(imageUri)
                addImageView(imageUri)
            }
        }
    }

    private fun addImageView(uri: Uri) {
        val imageView = ImageView(this).apply {
            layoutParams = android.view.ViewGroup.LayoutParams(300, 300)
            setPadding(8, 8, 8, 8)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        // Use Glide to load and resize image safely
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .override(300, 300) // Resize to avoid memory crash
            .into(imageView)

        binding.selectedImagesContainer.addView(imageView)
    }


    private fun submitProduct() {
        val productName = binding.editProductName.text.toString().trim()
        val description = binding.editDescription.text.toString().trim()
        val price = binding.editPrice.text.toString().trim()
        val unitType = binding.editUnitType.text.toString().trim()

        if (productName.isEmpty() || price.isEmpty() || unitType.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Confirm")
            .setMessage("Submit product: $productName?")
            .setPositiveButton("Yes") { _, _ ->
                Toast.makeText(this, "Product submitted!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
    private fun addProduct() {
        val name = binding.editProductName.text.toString().trim()
        val description = binding.editDescription.text.toString().trim()
        val price = binding.editPrice.text.toString().toDoubleOrNull()
        val stock = binding.editStock.text.toString().toIntOrNull()
        val moq = binding.editMOQ.text.toString().toIntOrNull() ?: 1
        val weight = binding.editWeight.text.toString().toDoubleOrNull() ?: 0.0
        val unitType = binding.editUnitType.text.toString().trim()
        val length = binding.editLength.text.toString().toDoubleOrNull() ?: 0.0
        val width = binding.editWidth.text.toString().toDoubleOrNull() ?: 0.0
        val height = binding.editHeight.text.toString().toDoubleOrNull() ?: 0.0
        val tags = binding.editTags.text.toString().trim()
        val isVisible = binding.switchVisibility.isChecked

        // Validate required fields
        if (name.isEmpty() || description.isEmpty() || price == null || stock == null || unitType.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Adjust these according to how you're populating your spinners
        val categoryId = binding.spinnerCategory.selectedItemPosition + 1
        val brandId = binding.spinnerSubCategory.selectedItemPosition + 1

        val productRequest = ProductRequest(
            name = name,
            description = description,
            price = price,
            stock = stock,
            category_id = categoryId,
            brand_id = brandId,
            sku = "sku-${System.currentTimeMillis()}",
            status = "active",
            featured = false,
            meta_title = name,
            meta_description = description.take(160),
            slug = name.lowercase().replace(" ", "-"),
            min_order_qty = moq,
            unit_type = unitType,
            weight = weight,
            length = length,
            width = width,
            height = height,
            tags = tags,
            visibility = isVisible
        )


        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.createProduct(productRequest)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        Toast.makeText(this@AddProduct, "Product added successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddProduct, responseBody?.message ?: "Failed to add product", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AddProduct, "Server error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddProduct, "Network error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

}
