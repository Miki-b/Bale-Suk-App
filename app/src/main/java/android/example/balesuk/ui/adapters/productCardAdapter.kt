package android.example.balesuk.ui.adapters

import android.example.balesuk.R
import android.example.balesuk.data.Product
import android.example.balesuk.data.circular_image_text
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class ProductCardAdapter(
    private var productList: List<Product>,
    private val Screen:String,
    private val onAddToCartClicked: (Product) -> Unit,
    private val onClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductCardAdapter.ProductCardViewHolder>() {

    inner class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = if (Screen == "Home") {
            itemView.findViewById(R.id.productImage)
        } else {
            itemView.findViewById(R.id.productCardImage)
        }

        private val productName: TextView = if (Screen == "Home") {
            itemView.findViewById(R.id.name)
        } else {
            itemView.findViewById(R.id.Productname)
        }

        private val productPrice: TextView = if (Screen == "Home") {
            itemView.findViewById(R.id.price)
        } else {
            itemView.findViewById(R.id.Productprice)
        }

        private val addToCartButton: Button = if (Screen == "Home") {
            itemView.findViewById(R.id.addToCartButton)
        } else {
            itemView.findViewById(R.id.ProductaddToCartButton)
        }

        fun bind(product: Product) {
            productImage.load(product.productimageURL) {
                crossfade(true)
            }
            productName.text = product.productName
            productPrice.text = product.productPrice.toString()

            addToCartButton.setOnClickListener {
                onAddToCartClicked(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val layoutRes = if (Screen == "Home") R.layout.products else R.layout.product_card
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ProductCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        val productCard= productList[position]
        holder.bind(productList[position])
        holder.itemView.setOnClickListener{
            onClick(productCard)
        }

    }

    override fun getItemCount(): Int = productList.size

    fun updateProducts(newProducts: List<Product>) {
        productList = newProducts
        notifyDataSetChanged()
    }
}
