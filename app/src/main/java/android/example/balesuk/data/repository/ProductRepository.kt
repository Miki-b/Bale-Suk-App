// ProductRepository.kt
package android.example.balesuk.data.repository

import ProductRequest
import Products
import android.example.balesuk.data.api.RetrofitInstance
import android.example.balesuk.data.models.LoginRequest
import android.example.balesuk.data.models.RegisterRequest
import retrofit2.Response


class ProductRepository {

    suspend fun register(user: RegisterRequest) =
        RetrofitInstance.api.register(user)

    suspend fun login(credentials: LoginRequest) =
        RetrofitInstance.api.login(credentials)

    suspend fun getProducts(categoryId: Int? = null, search: String? = null) =
        RetrofitInstance.api.getProducts(categoryId = categoryId, search = search)

    suspend fun getProduct(id: Int) =
        RetrofitInstance.api.getProduct(id)

    suspend fun createProduct(product: ProductRequest) =
        RetrofitInstance.api.createProduct(product)

    suspend fun updateProduct(id: Int, product: Map<String, Any>) =
        RetrofitInstance.api.updateProduct(id, product)

    suspend fun deleteProduct(id: Int) =
        RetrofitInstance.api.deleteProduct(id)

//    suspend fun getProductsByCategory(categoryId: Int): Response<List<Products>> {
//        return RetrofitInstance.api.getProducts(categoryId)
//    }


}
