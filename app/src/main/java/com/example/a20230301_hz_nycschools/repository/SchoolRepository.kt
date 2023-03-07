package com.example.a20230301_hz_nycschools.repository

import androidx.lifecycle.LiveData
import com.example.a20230301_hz_nycschools.network.MainNetwork

class SchoolRepository(val network: MainNetwork) {


//    val schoolListLiveData: LiveData<List<School>> = schoolDao.titleLiveData.map { it?.title }
//    val schoolSATResultLiveData: LiveData<List<SATResult>> = schoolDao.titleLiveData.map { it?.}


}


class RefreshError(message: String, cause: Throwable) : Throwable(message, cause)

interface RefreshCallback {
    fun onCompleted()
    fun onError(cause: Throwable)
}