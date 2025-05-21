package android.example.balesuk.data

data class CartItem(
    val productId: String,
    val name: String,
    val price: Double,
    var quantity: Int,
    val imageUrl: String
)
