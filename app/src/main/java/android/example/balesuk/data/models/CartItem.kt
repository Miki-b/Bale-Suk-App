package android.example.balesuk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val productId: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int
) : Parcelable
