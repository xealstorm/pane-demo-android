package com.example.panedemo.panecomparison.common.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun ContactGroup(
    uiState: MessageUiState,
    viewModel: MessageViewModel,
    horizontalPadding: Dp,
    isSecondPaneOpen: Boolean,
    isNarrowPane: Boolean,
    onOpenSelector: (DemoSelectorSpec<*>) -> Unit,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = horizontalPadding,
            vertical = 12.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = GROUP_LABEL_CONTACT, style = MaterialTheme.typography.titleMedium)
        AdaptiveFieldRowWithLead(
            lead = { fieldModifier ->
                SelectorInputField(
                    label = FIELD_LABEL_SALUTATION,
                    options = DemoSalutationOption.entries,
                    selected = uiState.salutation,
                    onSelect = viewModel::onSalutationSelected,
                    optionLabel = DemoSalutationOption::label,
                    isSecondPaneOpen = isSecondPaneOpen,
                    onOpenSelector = onOpenSelector,
                    modifier = fieldModifier,
                )
            },
            fields = { fieldModifier ->
                OutlinedTextField(
                    value = uiState.firstName,
                    onValueChange = { viewModel.onContactFieldChanged(DemoContactField.FirstName, it) },
                    label = { Text(FIELD_LABEL_FIRST_NAME) },
                    singleLine = true,
                    modifier = fieldModifier,
                )
                OutlinedTextField(
                    value = uiState.lastName,
                    onValueChange = { viewModel.onContactFieldChanged(DemoContactField.LastName, it) },
                    label = { Text(FIELD_LABEL_LAST_NAME) },
                    singleLine = true,
                    modifier = fieldModifier,
                )
            },
            isNarrowPane = isNarrowPane,
        )
        OutlinedTextField(
            value = uiState.street,
            onValueChange = { viewModel.onContactFieldChanged(DemoContactField.Street, it) },
            label = { Text(FIELD_LABEL_STREET) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        AdaptiveFieldRow(isSecondPaneOpen = isSecondPaneOpen, isNarrowPane = isNarrowPane) { fieldModifier ->
            OutlinedTextField(
                value = uiState.zipCode,
                onValueChange = { viewModel.onContactFieldChanged(DemoContactField.ZipCode, it) },
                label = { Text(FIELD_LABEL_ZIP_CODE) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = fieldModifier,
            )
            OutlinedTextField(
                value = uiState.locationId,
                onValueChange = { viewModel.onContactFieldChanged(DemoContactField.LocationId, it) },
                label = { Text(FIELD_LABEL_LOCATION_ID) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = fieldModifier,
            )
        }
        OutlinedTextField(
            value = uiState.phoneNumber,
            onValueChange = { viewModel.onContactFieldChanged(DemoContactField.PhoneNumber, it) },
            label = { Text(FIELD_LABEL_PHONE_NUMBER) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
