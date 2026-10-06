package com.example.panedemo.panecomparison.common.message

data class MessageUiState(
    val message: String = "",
    val salutation: DemoSalutationOption = DemoSalutationOption.NotSpecified,
    val firstName: String = "",
    val lastName: String = "",
    val street: String = "",
    val zipCode: String = "",
    val locationId: String = "",
    val phoneNumber: String = "",
    val creditReport: DemoCreditReportOption = DemoCreditReportOption.NotSpecified,
    val employment: DemoEmploymentOption = DemoEmploymentOption.NotSpecified,
    val householdIncome: DemoHouseholdIncomeOption = DemoHouseholdIncomeOption.NotSpecified,
    val personsInHousehold: DemoPersonsInHouseholdOption = DemoPersonsInHouseholdOption.NotSpecified,
    val adultsInHousehold: String = "",
    val kidsInHousehold: String = "",
    val pets: String = "",
    val moveInDate: DemoMoveInDateOption = DemoMoveInDateOption.NotSpecified,
)