package android.example.balesuk.ui.activities

import android.accessibilityservice.MagnificationConfig.Builder
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.example.balesuk.R
import android.example.balesuk.databinding.ActivityAddProductBinding
import android.net.Uri
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import android.app.AlertDialog
import android.widget.Toast


class AddProduct : AppCompatActivity() {
    private lateinit var binding : ActivityAddProductBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener{
            var openGoogle = Intent(Intent.ACTION_VIEW,Uri.parse("http://www.google.com"))
            startActivity(openGoogle)
        }
        // Send an email


        binding.button2.setOnClickListener{
            showDialog();
//            val shiftToHome = Intent(this,Home::class.java);
//            val bundle = Bundle();
//            bundle.putString("name","Mike");
//            bundle.putInt("age",21);
//            shiftToHome.putExtras(bundle);
//            startActivityForResult(shiftToHome,1001)

        }
        val countries=resources.getStringArray(R.array.country_array);
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,countries);
        binding.listItem.adapter=adapter;






    }



    override fun onActivityResult(requestCode:Int,resultCode:Int,data:Intent?){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1001  && resultCode == RESULT_OK) {
            val result  = data?.getStringExtra("result_key")
            // Process the result
        }
    }
    private fun showDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to continue");
        builder.setPositiveButton("yes"){
            dialog,which->
            Toast.makeText(this,"confirmed",Toast.LENGTH_SHORT)
        }
        builder.setNegativeButton("No"){
            dialog,which->Toast.makeText(this,"unconfirmed",Toast.LENGTH_SHORT)
        }
        builder.show()
    }





}