package merail.life.navigation.domain

import kotlinx.serialization.Serializable
import merail.life.data.model.SelectorFilterType
import merail.life.navigation.domain.error.ErrorType

sealed class NavigationRoute {

    @Serializable
    data object Splash : NavigationRoute()

    @Serializable
    data object Home : NavigationRoute()

    @Serializable
    data class Selector(
        val selectorFilterType: SelectorFilterType,
    ) : NavigationRoute()

    @Serializable
    data class Content(
        val contentId: String?,
    ) : NavigationRoute() {
        companion object {
            const val ROUTE_NAME = "content"
            const val CONTENT_ID_KEY = "contentId"
        }
    }

    @Serializable
    data class Error(
        val errorType: ErrorType,
    ) : NavigationRoute() {
        companion object {
            const val ERROR_TYPE_KEY = "errorType"
        }
    }
}