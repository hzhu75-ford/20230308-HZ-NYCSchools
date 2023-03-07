package com.example.a20230301_hz_nycschools.network.response

data class School(
    val dbn: String,
    val schoolName: String?,
    val overview_paragraph: String?,
    val total_students: String?,
    val city: String?,
    val zip: String?,
    val primary_address_line_1: String?,
    val state: String?,
    val phone_number: String?,
    val school_email: String?,
    val website: String?,
)