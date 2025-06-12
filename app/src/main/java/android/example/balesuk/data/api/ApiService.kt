package android.example.balesuk.data.api

import ApiResponse
import ProductRequest
import Products
import android.example.balesuk.data.models.AuthResponse
import android.example.balesuk.data.models.LoginRequest

import android.example.balesuk.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/v1/register")
    suspend fun register(@Body user: RegisterRequest): Response<AuthResponse>

    @POST("api/v1/login")
    suspend fun login(@Body credentials: LoginRequest): Response<AuthResponse>



    @GET("api/v1/products")
        suspend fun getProducts(
        @Query("category_id") categoryId: Int? = null,
        @Query("search") search: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_direction") sortDirection: String? = null,
        @Query("per_page") perPage: Int? = 15
        ): Response<ApiResponse<List<Products>>>

        @GET("api/v1/products/{id}")
        suspend fun getProduct(@Path("id") id: Int): Response<ApiResponse<Products>>
    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category_id") categoryId: Int
    ): Response<List<Products>>

    @POST("api/v1/products")
        suspend fun createProduct(@Body productRequest: ProductRequest): Response<ApiResponse<Products>>

        @PUT("api/v1/products/{id}")
        suspend fun updateProduct(@Path("id") id: Int, @Body productRequest: Map<String, Any>): Response<ApiResponse<Products>>

        @DELETE("api/v1/products/{id}")
        suspend fun deleteProduct(@Path("id") id: Int): Response<ApiResponse<Unit>>
    }

