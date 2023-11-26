import androidx.annotation.StringRes
import com.example.loofmeals.R

enum class Screens(@StringRes val title: Int, val route: String? = null, val id: String? = null) {
    Overview(title = R.string.app_name, route = "overview"),
    Favorites(title = R.string.favorites, route = "favorites"),
    About(title = R.string.about, route = "about"),
    Detail(title = R.string.detail, route = "Detail/{restaurantId}", id = "restaurantId");

    companion object {
        fun getScreenByRoute(route: String): Screens? =
            values().find { it.route?.let { r -> route.startsWith(r, ignoreCase = true) } ?: false }
    }
}
