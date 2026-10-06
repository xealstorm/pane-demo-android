package com.example.panedemo.panecomparison.common.message

enum class DemoContactField {
    FirstName,
    LastName,
    Street,
    ZipCode,
    LocationId,
    PhoneNumber,
}

enum class DemoInformationField {
    AdultsInHousehold,
    KidsInHousehold,
    Pets,
}

enum class DemoSalutationOption(val label: String) {
    NotSpecified("Not specified"),
    Female("Ms."),
    Male("Mr."),
    Divers("Diverse"),
}

enum class DemoCreditReportOption(val label: String) {
    NotSpecified("Not specified"),
    Available("Available (< 3 months)"),
    NotAvailable("Not available"),
}

enum class DemoEmploymentOption(val label: String) {
    NotSpecified("Not specified"),
    Employee("Employee"),
    Freelancer("Self-employed"),
    Official("Civil servant"),
    Apprentice("Apprentice"),
    Student("Student"),
    PhDStudent("PhD student"),
    Homemaker("Homemaker"),
    Jobseeker("Jobseeker"),
    Pensioner("Pensioner"),
    Other("Other"),
}

enum class DemoHouseholdIncomeOption(val label: String) {
    NotSpecified("Not specified"),
    LessThan500("Under €500"),
    From500To1000("€500 - €1,000"),
    From1000To1500("€1,000 - €1,500"),
    From1500To2000("€1,500 - €2,000"),
    From2000To3000("€2,000 - €3,000"),
    From3000To4000("€3,000 - €4,000"),
    From4000To5000("€4,000 - €5,000"),
    MoreThan5000("Over €5,000"),
}

enum class DemoPersonsInHouseholdOption(val label: String) {
    NotSpecified("Not specified"),
    OnePerson("Single person"),
    TwoPerson("Two people"),
    Family("Family"),
    FlatShare("Flat share"),
}

enum class DemoMoveInDateOption(val label: String) {
    NotSpecified("Not specified"),
    Immediately("Immediately"),
    Flexible("Flexible"),
}
