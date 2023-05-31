package com.appdem.studentsFeature

import androidx.lifecycle.ViewModel

class FragmentStudentsViewModel : ViewModel() {

    val studentsMocked = listOf(
        StudentMock("23", "Cristea", "A3G2", specialCode = "12345567"),
        StudentMock("25", "Bucurescu", "A2G1", specialCode = "93205567"),
        StudentMock("22", "Dumitran", "A3G3", specialCode = "1111567"),
        StudentMock("24", "Alecu", "A1G2", specialCode = "2225567"),
        StudentMock("55", "Ilie", "A4G2", specialCode = "32345567"),
        StudentMock("33", "Pascu", "A4G2", specialCode = "42333567"),
        StudentMock("18", "Dijescu", "A3G3", specialCode = "15567"),
        StudentMock("18", "Marian", "A3G3", specialCode = "92345567"),
        StudentMock("18", "Nairam", "A3G3", specialCode = "82345567"),
        StudentMock("18", "Ionut", "A3G3", specialCode = "72345567"),
        StudentMock("18", "Trayan", "A3G3", specialCode = "62345567"),
        StudentMock("18", "Ion", "A3G3", specialCode = "52345567")
    )

    val adapter = StudentAdapter(studentsMocked)
}