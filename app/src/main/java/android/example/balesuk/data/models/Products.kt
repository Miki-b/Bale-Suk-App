import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// Product.kt
@Parcelize
data class Products(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int?,
    val category_id: Int?,
    val brand_id: Int?,
    val sku: String?,
    val status: String?,
    val featured: Boolean?,
    val meta_title: String?,
    val meta_description: String?,
    val slug: String?,
    val images: List<ProductImage>?
) : Parcelable
@Parcelize
data class ProductImage(
    val id: Int,
    val path: String
):Parcelable

// Generic API response with success flag, message, data, errors
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val errors: Map<String, List<String>>? = null
)
data class ProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val category_id: Int,
    val brand_id: Int,
    val sku: String,
    val status: String,
    val featured: Boolean,
    val meta_title: String,
    val meta_description: String,
    val slug: String,
    val min_order_qty: Int,
    val unit_type: String,
    val weight: Double,
    val length: Double,
    val width: Double,
    val height: Double,
    val tags: String,
    val visibility: Boolean
)
