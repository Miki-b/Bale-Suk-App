package android.example.balesuk.ui.adapters

import android.example.balesuk.R
import android.example.balesuk.data.circular_image_text
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class circularImageTextAdapter(private val CircularImageTexts: List<circular_image_text>,
                               private val onClick: (circular_image_text) -> Unit) :
    RecyclerView.Adapter<circularImageTextAdapter.CircularImageTextViewHolder>() {

    inner class CircularImageTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.Icon)
        val label: TextView = itemView.findViewById(R.id.Label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularImageTextViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.circular_image_text, parent, false)
        return CircularImageTextViewHolder(view)
    }
    override fun onBindViewHolder(holder: CircularImageTextViewHolder, position: Int) {
        val CircularImageText = CircularImageTexts[position]
        holder.icon.load(CircularImageText.imageURL) {
            crossfade(true)
        }
        holder.label.text = CircularImageText.label
        holder.itemView.setOnClickListener{
            onClick(CircularImageText)
        }
        }



    override fun getItemCount(): Int = CircularImageTexts.size
}
