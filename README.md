# BaleSuk Android App

BaleSuk is a modern e-commerce Android application built with Kotlin, following MVVM architecture. It allows users to browse products, manage a shopping cart, register/login, and place orders. The app uses Retrofit for networking, Room for local data, and modern Android libraries for a smooth user experience.

## Features

- **User Authentication**: Register and login with secure token-based authentication.
- **Product Catalog**: Browse products by category, view product details, and search/filter products.
- **Shopping Cart**: Add, update, and remove products from the cart. View total price and proceed to checkout.
- **Order Management**: Place orders and view order details.
- **Category Browsing**: Explore products by categories with visually appealing UI.
- **Image Slider**: Product images are displayed in a slider for better visualization.
- **Modern UI**: Material Design 3, dark mode support, and responsive layouts.

## Architecture

- **MVVM Pattern**: Separation of concerns using ViewModel, Repository, and LiveData.
- **Networking**: Retrofit for API calls, OkHttp for logging, and Gson for JSON parsing.
- **Image Loading**: Coil and Glide for efficient image loading and caching.
- **Navigation**: Android Navigation Component for seamless screen transitions.
- **View Binding**: Type-safe access to views.

## Main Modules & Structure

```
app/src/main/java/android/example/balesuk/
├── data/
│   ├── api/           # Retrofit API service, session management
│   ├── models/        # Data models (Product, Category, User, etc.)
│   ├── repository/    # Repository pattern for data access
│   ├── product_db.kt  # (If Room DB is used)
├── ui/
│   ├── activities/    # Main screens: Home, Login, Register, Cart, Orders, AddProduct, etc.
│   ├── adapters/      # RecyclerView adapters for products, cart, categories, image slider
│   └── activities/ui/ # Fragments for Home, Dashboard, Notifications
├── viewmodel/         # ViewModels for products and cart
├── MainActivity.kt    # App entry point
```

## Key Screens

- **Login/Register**: User authentication flows
- **Home**: Product listing, categories, banners, and quick add-to-cart
- **Product Detail**: Detailed product info and image slider
- **Cart**: View and update cart items, proceed to checkout
- **Orders**: View order summary after checkout
- **Add Product**: (For admin or demo) Add new products with images

## Data Models

- **User**: id, name, email, timestamps
- **Product**: id, name, description, price, stock, category, images, etc.
- **Category**: id, name, slug, description, imageURL
- **CartItem**: productId, name, price, imageUrl, quantity
- **AuthResponse**: success, token, user

## API Endpoints (via Retrofit)
- Register, Login
- Get all products, get product by ID, filter by category
- Create, update, delete product

## Tech Stack
- **Language**: Kotlin
- **Min SDK**: 24
- **Target SDK**: 35
- **Libraries**: Retrofit, OkHttp, Gson, Coil, Glide, Material Components, Navigation, ViewModel, LiveData

## Theming
- Material 3 DayNight theme
- Custom styles for forms and UI elements
- Dark mode supported

## Getting Started

1. **Clone the repository**
   ```bash
   git clone <repo-url>
   ```
2. **Open in Android Studio**
3. **Build the project** (Gradle sync will fetch dependencies)
4. **Run on emulator or device**

## Project Setup
- All dependencies are managed via Gradle Kotlin DSL (`build.gradle.kts`).
- Uses ViewBinding for type-safe UI code.
- API base URL and keys should be set in `RetrofitInstance.kt` or via build config.

## Contribution
Pull requests are welcome! Please open issues for suggestions or bugs.

## License
[MIT](LICENSE) (or specify your license) 