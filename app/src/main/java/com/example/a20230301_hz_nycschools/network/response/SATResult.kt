package com.example.a20230301_hz_nycschools.network.response



/**
 * Created SchoolDetails data class to handle SchoolDetails data.
 * And extending with Parcelable to pass data class from one Activity to another Activity.
 * */
data class SATResult(
    val dbn: String,
    val school_name: String? = null,
    val num_of_sat_test_takers: String,
    val sat_critical_reading_avg_score: String,
    val sat_math_avg_score: String,
    val sat_writing_avg_score: String
)