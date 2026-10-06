package com.example.panedemo.panecomparison.common.message

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MessageViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<MessageUiState> = MutableStateFlow(MessageUiState())
    val uiState: StateFlow<MessageUiState> = _uiState.asStateFlow()

    fun onMessageChanged(text: String) = _uiState.update { it.copy(message = text) }

    fun onSalutationSelected(value: DemoSalutationOption) =
        _uiState.update { it.copy(salutation = value) }

    fun onContactFieldChanged(field: DemoContactField, value: String) = _uiState.update {
        when (field) {
            DemoContactField.FirstName -> it.copy(firstName = value)
            DemoContactField.LastName -> it.copy(lastName = value)
            DemoContactField.Street -> it.copy(street = value)
            DemoContactField.ZipCode -> it.copy(zipCode = value)
            DemoContactField.LocationId -> it.copy(locationId = value)
            DemoContactField.PhoneNumber -> it.copy(phoneNumber = value)
        }
    }

    fun onSchufaSelected(value: DemoCreditReportOption) =
        _uiState.update { it.copy(creditReport = value) }

    fun onEmploymentSelected(value: DemoEmploymentOption) =
        _uiState.update { it.copy(employment = value) }

    fun onHouseholdIncomeSelected(value: DemoHouseholdIncomeOption) =
        _uiState.update { it.copy(householdIncome = value) }

    fun onPersonsInHouseholdSelected(value: DemoPersonsInHouseholdOption) =
        _uiState.update { it.copy(personsInHousehold = value) }

    fun onInformationFieldChanged(field: DemoInformationField, value: String) = _uiState.update {
        when (field) {
            DemoInformationField.AdultsInHousehold -> it.copy(adultsInHousehold = value)
            DemoInformationField.KidsInHousehold -> it.copy(kidsInHousehold = value)
            DemoInformationField.Pets -> it.copy(pets = value)
        }
    }

    fun onMoveInDateSelected(value: DemoMoveInDateOption) =
        _uiState.update { it.copy(moveInDate = value) }

    fun onSendClicked() = Unit
}