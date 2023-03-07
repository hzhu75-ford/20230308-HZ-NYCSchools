package com.example.a20230301_hz_nycschools.network.response

data class SchoolWithSATResult(
    val dbn: String,
    val schoolName: String,
    val overviewParagraph: String?,
    val totalStudents: String?,
    val city: String?,
    val zip: String?,
    val address1: String?,
    val num_of_sat_test_takers: String,
    val sat_critical_reading_avg_score: String,
    val sat_math_avg_score: String,
    val sat_writing_avg_score: String,
    val phone_number: String?,
    val school_email: String?,
    val website: String?,
)
