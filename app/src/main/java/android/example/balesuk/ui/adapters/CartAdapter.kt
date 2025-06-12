import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.example.balesuk.R
import android.example.balesuk.model.CartItem

class CartAdapter(
    private var items: List<CartItem>,
    private val onQuantityChange: (CartItem, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.cart_item_name)
        val productPrice: TextView = itemView.findViewById(R.id.cart_item_price)
        val productImage: ImageView = itemView.findViewById(R.id.cart_item_image)
        val quantityText: TextView = itemView.findViewById(R.id.cart_item_quantity)
        val increaseButton: View = itemView.findViewById(R.id.increase_quantity_button)
        val decreaseButton: View = itemView.findViewById(R.id.decrease_quantity_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]

        holder.productName.text = item.name
        // Format price with 2 decimals
        holder.productPrice.text = "Price: $${"%.2f".format(item.price * item.quantity)}"
        holder.quantityText.text = item.quantity.toString()

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.productImage)

        holder.increaseButton.setOnClickListener {
            onQuantityChange(item, item.quantity + 1)
        }

        holder.decreaseButton.setOnClickListener {
            if (item.quantity > 1) {
                onQuantityChange(item, item.quantity - 1)
            }
        }
    }

    fun updateItems(newItems: List<CartItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}
