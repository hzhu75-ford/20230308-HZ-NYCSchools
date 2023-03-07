package com.example.a20230301_hz_nycschools.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a20230301_hz_nycschools.network.getNetworkService
import com.example.a20230301_hz_nycschools.network.response.SATResult
import com.example.a20230301_hz_nycschools.network.response.School
import com.example.a20230301_hz_nycschools.network.response.SchoolWithSATResult
import com.example.a20230301_hz_nycschools.repository.RefreshError
import com.example.a20230301_hz_nycschools.repository.SchoolRepository
import com.example.a20230301_hz_nycschools.util.singleArgViewModelFactory
import kotlinx.coroutines.*


/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param repository the data source this ViewModel will fetch results from.
 */
class SchoolViewModel(private val repository: SchoolRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [MainViewModel]
         *
         * @param arg the repository to pass to [MainViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::SchoolViewModel)
        private val TAG = "SchoolViewModel"
    }

    /**
     * Request a snackbar to display a string.
     *
     * This variable is private because we don't want to expose MutableLiveData
     *
     * MutableLiveData allows anyone to set a value, and MainViewModel is the only
     * class that should be setting values.
     */
    private val _snackBar = MutableLiveData<String?>()

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<String?>
        get() = _snackBar

    /**
     * Store schools List via this LiveData
     */
//    private val _schoolsListLivedata = repository.schoolListLiveData
//
//    val schoolsListLivedata: LiveData<List<School>>
//        get() = _schoolsListLivedata

    private var schoolDetails = mutableListOf<SchoolWithSATResult>()


    private var _schoolsWithSATListLivedata = MutableLiveData<List<SchoolWithSATResult>>()

    val schoolsWithSATListLivedata: LiveData<List<SchoolWithSATResult>>
        get() = _schoolsWithSATListLivedata

    /**
     * Store SAT result  via this LiveData
     */

//    private val _satResultLivedata = repository.satResultLiveData
//
//    val satResultLivedata: LiveData<List<SATResult>>
//        get() = _satResultLivedata

    private val _spinner = MutableLiveData<Boolean>(false)

    /**
     * Show a loading spinner if true
     */
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * Count of taps on the screen
     */
    private var keyWord = ""

    init {
        fetchAllSchool()
    }


    /**
     * Respond to onClick events by refreshing the title.
     *
     * The loading spinner will display until a result is returned, and errors will trigger
     * a snackbar.
     */
//    fun onMainViewClicked() {
//        refreshTitle()
//    }



    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    /**
     * Refresh the title, showing a loading spinner while it refreshes and errors via snackbar.
     */
//    fun refreshTitle() = launchDataLoad {
//        repository.refreshTitle()
//    }

    /**
     * Helper function to call a data load function with a loading spinner, errors will trigger a
     * snackbar.
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop
     */
    private fun launchDataLoad(block: suspend () -> Unit): Unit {
        viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            } catch (error: RefreshError) {
                _snackBar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }

    @VisibleForTesting
    fun fetchAllSchool() = viewModelScope.launch{
        withContext(Dispatchers.IO) {
            try{
                _spinner.postValue(true)
                var schoolList = mutableListOf<School>()
                var result = mutableListOf<SATResult>()
                async {
                    schoolList.addAll(getNetworkService().fetchAllSchoolList())
                    result.addAll(getNetworkService().fetchSATResults())
                }.await()
                updateLivedata(schoolList,result)
            } catch (e: Exception){
                _snackBar.postValue(e.message)
            } finally {
                _spinner.postValue(false)
            }
        }
    }

    fun updateLivedata(schoolList: MutableList<School>, result: MutableList<SATResult>) {
        var currList = mutableListOf<SchoolWithSATResult>()
        for ( school in schoolList) {
                var schoolSAT = result.find { it.dbn == school.dbn } ?: run {
                    SATResult(
                        "${school.dbn}",
                        "${school.schoolName}",
                        "Not Available",
                        "Not Available",
                        "Not Available",
                        "Not Available"
                    )
                }

            currList.run {
                    add(
                        SchoolWithSATResult(
                            school.dbn,
                            schoolSAT.school_name!!,
                            school.overview_paragraph,
                            school.total_students,
                            school.city!!,
                            school.zip,
                            school.primary_address_line_1,
                            schoolSAT.num_of_sat_test_takers,
                            schoolSAT.sat_critical_reading_avg_score,
                            schoolSAT.sat_math_avg_score,
                            schoolSAT.sat_writing_avg_score,
                            school.phone_number,
                            school.school_email,
                            school.website
                        )
                    )
                    if (school.dbn == "06M540") {
                        Log.d(TAG, "${schoolSAT.num_of_sat_test_takers}")
                    }
                }
        }
        schoolDetails.addAll(currList.filter {it.sat_critical_reading_avg_score !="Not Available"})

        _schoolsWithSATListLivedata.postValue(schoolDetails)
    }

    fun searchSchool(query: String) {
        var curr = schoolDetails.filter { it.schoolName.contains(query) }
        if(curr.isEmpty()) {
            _snackBar.value = "No School Found"
            _schoolsWithSATListLivedata.setValue(emptyList())
        }
        else {
        _schoolsWithSATListLivedata.setValue(schoolDetails.filter { it.schoolName.contains(query) })}
    }

    fun hideSpinner(flag: Boolean) {
        _spinner.value = flag
    }

    fun hideSnackbar(str: String) {
        _snackBar.value = ""
    }

    fun sortListByNameAToZ() {
        Log.d(TAG,schoolDetails.sortedBy { it.schoolName}.joinToString { it.schoolName })
        _schoolsWithSATListLivedata.value = schoolDetails.sortedBy { it.schoolName}
    }
    fun sortListByNameZToA() { _schoolsWithSATListLivedata.value = schoolDetails.sortedBy { it.schoolName}.asReversed()}
    fun sortByMathScore() { _schoolsWithSATListLivedata.value = schoolDetails.sortedBy { it.sat_math_avg_score}.asReversed()}
    fun sortByWritingScore() {_schoolsWithSATListLivedata.value = schoolDetails.sortedBy { it.sat_writing_avg_score}.asReversed()}
    fun sortByReadingScore() {_schoolsWithSATListLivedata.value = schoolDetails.sortedBy { it.sat_critical_reading_avg_score}.asReversed()}
    fun sortByNumberOfTakers(){ _schoolsWithSATListLivedata.value = schoolDetails.sortedBy { it.num_of_sat_test_takers}.asReversed()}
}
