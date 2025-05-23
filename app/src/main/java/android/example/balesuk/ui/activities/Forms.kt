package android.example.balesuk.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.example.balesuk.R
import android.example.balesuk.databinding.ActivityFormsBinding
import android.example.forms.DisplayActivity
import java.util.Calendar
import java.util.Locale

class Forms : AppCompatActivity() {
    private lateinit var binding: ActivityFormsBinding
    private var imageUri: Uri? = null

    private var selectedGender: String = "Male"
    private var selectedCountry: String = ""

    private lateinit var genders: Array<String>
    private lateinit var countries: Array<String>

    private lateinit var sharedPrefs: SharedPreferences

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imageUri = it
            binding.imageView.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPrefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        loadSavedLocale()

        super.onCreate(savedInstanceState)
        binding = ActivityFormsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLocalizedText()
        registerForContextMenu(binding.imageView)

        setupSpinner()
        setupListView()

        savedInstanceState?.let {
            binding.editTextName.setText(it.getString("name"))
            binding.editTextDate.setText(it.getString("date"))
            selectedGender = it.getString("gender") ?: "Male"
            selectedCountry = it.getString("country") ?: ""
            val uriStr = it.getString("imageUri")
            uriStr?.let {
                imageUri = Uri.parse(it)
                binding.imageView.setImageURI(imageUri)
            }
        }

        binding.editTextDate.setOnClickListener { showDatePicker() }

        binding.buttonPickImageOld.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1001)
        }

        binding.buttonPickImageNew.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.buttonSubmit.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val date = binding.editTextDate.text.toString()

            val intent = Intent(this, DisplayActivity::class.java).apply {
                putExtra("name", name)
                putExtra("date", date)
                putExtra("gender", selectedGender)
                putExtra("country", selectedCountry)
                imageUri?.let { putExtra("imageUri", it.toString()) }
            }
            startActivity(intent)
        }
    }

    private fun setupSpinner() {
        genders = resources.getStringArray(R.array.gender_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner2.adapter = adapter

        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedGender = genders[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupListView() {
        countries = resources.getStringArray(R.array.country_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, countries)
        binding.countryListView.adapter = adapter
        binding.countryListView.choiceMode = android.widget.ListView.CHOICE_MODE_SINGLE

        binding.countryListView.setOnItemClickListener { _, _, position, _ ->
            selectedCountry = countries[position]
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val dateStr = "$dayOfMonth/${month + 1}/$year"
                binding.editTextDate.setText(dateStr)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.lang_amharic -> {
                saveLocale("am")
                setLocale("am")
                true
            }
            R.id.lang_english -> {
                saveLocale("en")
                setLocale("en")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }

    private fun saveLocale(language: String) {
        sharedPrefs.edit().putString("language", language).apply()
    }

    private fun loadSavedLocale() {
        val language = sharedPrefs.getString("language", "en") ?: "en"
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun setLocalizedText() {
        binding.editTextName.hint = getString(R.string.hint_name)
        binding.editTextDate.hint = getString(R.string.hint_date)
        binding.buttonPickImageOld.text = getString(R.string.btn_old)
        binding.buttonPickImageNew.text = getString(R.string.btn_new)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", binding.editTextName.text.toString())
        outState.putString("date", binding.editTextDate.text.toString())
        outState.putString("gender", selectedGender)
        outState.putString("country", selectedCountry)
        imageUri?.let { outState.putString("imageUri", it.toString()) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            binding.imageView.setImageURI(imageUri)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v?.id == R.id.imageView) {
            menuInflater.inflate(R.menu.context_menu, menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                imageUri = null
                binding.imageView.setImageResource(R.drawable.ic_launcher_background)
                Toast.makeText(this, "Image removed", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
