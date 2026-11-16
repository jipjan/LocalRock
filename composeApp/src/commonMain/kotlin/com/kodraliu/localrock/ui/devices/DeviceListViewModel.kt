package com.kodraliu.localrock.ui.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodraliu.localrock.shared.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DeviceListViewModel(
    private val container: AppContainer,
) : ViewModel() {

    val devices = container.deviceRepository.devices
    val productsByDuid = container.deviceRepository.productsByDuid
    val reachability = container.deviceRepository.reachability
    val statuses = container.deviceRepository.statuses

    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> = _refreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            _error.value = null
            try {
                container.deviceRepository.refresh()
                container.deviceRepository.probeReachability()
            } catch (e: Throwable) {
                _error.value = e.message ?: "Failed to load devices"
            } finally {
                _refreshing.value = false
            }
        }
    }

    /** Re-reads every robot's status for the cards, without the pull-to-refresh spinner. */
    fun refreshStatuses() = container.deviceRepository.probeReachability()

    private val _actionError = MutableStateFlow<String?>(null)
    val actionError: StateFlow<String?> = _actionError.asStateFlow()

    /** A card button; the card's status is read again afterwards. */
    fun quickAction(device: com.kodraliu.localrock.shared.model.Device, action: AppContainer.QuickAction) {
        viewModelScope.launch {
            _actionError.value = null
            runCatching { container.quickAction(device, action) }
                .onFailure { _actionError.value = it.message ?: "The robot did not accept that" }
            kotlinx.coroutines.delay(1_500)
            container.deviceRepository.probeReachability()
        }
    }

    fun logout() = container.authRepository.logout()
}
