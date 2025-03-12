package cashierapp.presentations.ui.screens.home

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class ProductEvent {
    data object ProductAdded : ProductEvent()
    data object ProductUpdated : ProductEvent()
    data object ProductDeleted : ProductEvent()
}

object ProductEventBus {
    private val _events = MutableSharedFlow<ProductEvent>()
    val events = _events.asSharedFlow()

    suspend fun emitEvent(event: ProductEvent) {
        _events.emit(event)
    }
}