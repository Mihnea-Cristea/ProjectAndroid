package com.appdem.studentsFeature

data class StudentMock(
    val age: String,
    val name: String,
    val yearGroup: String,
    val specialCode: String
) {
    override fun toString(): String {
        return "Student data -> \nname: $name age: $age year and group: $yearGroup special code: $specialCode"
    }
}
