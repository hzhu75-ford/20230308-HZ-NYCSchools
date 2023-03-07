package com.example.a20230301_hz_nycschools.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.a20230301_hz_nycschools.network.getNetworkService
import com.example.a20230301_hz_nycschools.network.response.SATResult
import com.example.a20230301_hz_nycschools.network.response.School
import com.example.a20230301_hz_nycschools.network.response.SchoolWithSATResult
import com.example.a20230301_hz_nycschools.repository.SchoolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SchoolViewModelTest {

    val dispatcher = TestCoroutineDispatcher()
    private lateinit var repository: SchoolRepository
    private lateinit var viewModel: SchoolViewModel
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        MockitoAnnotations.initMocks(this)
        repository = SchoolRepository(getNetworkService())
        viewModel = SchoolViewModel(repository)
    }


    @Test
    fun updateSchools_success() {
        val schools = mutableListOf(School(
            dbn = "28Q328",
            schoolName = "LIBERATON DIPLOMA PLUS",
            overview_paragraph ="High School for Community Leadership (HSCL) encourages the development of well-rounded individuals who are prepared for university success and capable of exercising leadership in the global community.",
            primary_address_line_1 = "2865 Wewt 19th Stree",
            total_students = "206",
            city = "Brooklyn",
            zip = "11432",
            phone_number = "718-558-9801",
            school_email ="Cborrero@schools.nyc.gov",
            website= "www.nychscl.org",
            state = "NY"
        ))

        val results = mutableListOf(SATResult(
            dbn = "28Q328",
            school_name = "LIBERATON DIPLOMA PLUS",
            sat_critical_reading_avg_score = "411",
            sat_writing_avg_score = "373",
            num_of_sat_test_takers = "10",
            sat_math_avg_score = "400"
        ))

        viewModel.updateLivedata(schools,results)

        val expectedValue = mutableListOf(SchoolWithSATResult(
            dbn = "28Q328",
            schoolName = "LIBERATON DIPLOMA PLUS",
            overviewParagraph ="High School for Community Leadership (HSCL) encourages the development of well-rounded individuals who are prepared for university success and capable of exercising leadership in the global community.",
            address1 = "2865 Wewt 19th Stree",
            totalStudents = "206",
            city = "Brooklyn",
            zip = "11432",
            phone_number = "718-558-9801",
            school_email ="Cborrero@schools.nyc.gov",
            website= "www.nychscl.org",
            sat_critical_reading_avg_score = "411",
            sat_writing_avg_score = "373",
            num_of_sat_test_takers = "10",
            sat_math_avg_score = "400"
        )
        )

        assertEquals(expectedValue, viewModel.schoolsWithSATListLivedata.getOrAwaitValue())


    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}