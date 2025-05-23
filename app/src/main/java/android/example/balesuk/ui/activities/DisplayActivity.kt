package android.example.forms

import android.annotation.SuppressLint
import android.example.balesuk.R
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisplayActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        val name = intent.getStringExtra("name") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val gender = intent.getStringExtra("gender") ?: ""
        val country = intent.getStringExtra("country") ?: ""
        val imageUri = intent.getStringExtra("imageUri")

        findViewById<TextView>(R.id.name_display).text = getString(R.string.name_display, name)
        findViewById<TextView>(R.id.date_display).text = getString(R.string.date_display, date)
        findViewById<TextView>(R.id.gender_display).text = getString(R.string.gender_display, gender)
        findViewById<TextView>(R.id.country_display).text = getString(R.string.country_display, country)

        imageUri?.let {
            findViewById<ImageView>(R.id.image_display).setImageURI(Uri.parse(it))
        }
    }
}
