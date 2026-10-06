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
internal fun InformationGroup(
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
        Text(text = GROUP_LABEL_INFORMATION, style = MaterialTheme.typography.titleMedium)
        SelectorInputField(
            label = FIELD_LABEL_CREDIT_REPORT,
            options = DemoCreditReportOption.entries,
            selected = uiState.creditReport,
            onSelect = viewModel::onSchufaSelected,
            optionLabel = DemoCreditReportOption::label,
            isSecondPaneOpen = isSecondPaneOpen,
            onOpenSelector = onOpenSelector,
        )
        SelectorInputField(
            label = FIELD_LABEL_EMPLOYMENT,
            options = DemoEmploymentOption.entries,
            selected = uiState.employment,
            onSelect = viewModel::onEmploymentSelected,
            optionLabel = DemoEmploymentOption::label,
            isSecondPaneOpen = isSecondPaneOpen,
            onOpenSelector = onOpenSelector,
        )
        SelectorInputField(
            label = FIELD_LABEL_INCOME,
            options = DemoHouseholdIncomeOption.entries,
            selected = uiState.householdIncome,
            onSelect = viewModel::onHouseholdIncomeSelected,
            optionLabel = DemoHouseholdIncomeOption::label,
            isSecondPaneOpen = isSecondPaneOpen,
            onOpenSelector = onOpenSelector,
        )
        SelectorInputField(
            label = FIELD_LABEL_PERSONS_IN_HOUSEHOLD,
            options = DemoPersonsInHouseholdOption.entries,
            selected = uiState.personsInHousehold,
            onSelect = viewModel::onPersonsInHouseholdSelected,
            optionLabel = DemoPersonsInHouseholdOption::label,
            isSecondPaneOpen = isSecondPaneOpen,
            onOpenSelector = onOpenSelector,
        )
        AdaptiveFieldRow(isSecondPaneOpen = isSecondPaneOpen, isNarrowPane = isNarrowPane) { fieldModifier ->
            OutlinedTextField(
                value = uiState.adultsInHousehold,
                onValueChange = { viewModel.onInformationFieldChanged(DemoInformationField.AdultsInHousehold, it) },
                label = { Text(FIELD_LABEL_ADULTS) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = fieldModifier,
            )
            OutlinedTextField(
                value = uiState.kidsInHousehold,
                onValueChange = { viewModel.onInformationFieldChanged(DemoInformationField.KidsInHousehold, it) },
                label = { Text(FIELD_LABEL_KIDS) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = fieldModifier,
            )
        }
        OutlinedTextField(
            value = uiState.pets,
            onValueChange = { viewModel.onInformationFieldChanged(DemoInformationField.Pets, it) },
            label = { Text(FIELD_LABEL_PETS) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        SelectorInputField(
            label = FIELD_LABEL_MOVE_IN_DATE,
            options = DemoMoveInDateOption.entries,
            selected = uiState.moveInDate,
            onSelect = viewModel::onMoveInDateSelected,
            optionLabel = DemoMoveInDateOption::label,
            isSecondPaneOpen = isSecondPaneOpen,
            onOpenSelector = onOpenSelector,
        )
    }
}
