package com.example.a20230301_hz_nycschools

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.example.a20230301_hz_nycschools.databinding.ActivityMainBinding
import com.example.a20230301_hz_nycschools.databinding.NavHeaderBinding
import com.example.a20230301_hz_nycschools.network.getNetworkService
import com.example.a20230301_hz_nycschools.repository.SchoolRepository
import com.example.a20230301_hz_nycschools.viewmodel.SchoolViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var selection  = 0


    private var _navBinding: NavHeaderBinding? = null
    private val navBinding get() = _navBinding!!
    val repository = SchoolRepository(getNetworkService())
    private val viewModel by lazy {
        ViewModelProvider(this, SchoolViewModel.FACTORY(repository))
        .get(SchoolViewModel::class.java)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        val headerView = binding.navView.getHeaderView(0)
        _navBinding = NavHeaderBinding.bind(headerView)

        setupDrawer()
    }

    private fun setupDrawer() = with(navBinding){
        sortRadioGroup.setOnCheckedChangeListener(object: RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.sort_by_name_a_to_z -> {
                        SORTBYNAME = true
                        SORTBYNAMEREVERSED = false
                        SORTBYMATH = false
                        SORTBYREADING = false
                        SORTBYWRITING = false
                        SORTBYTAKER = false
                        Log.d(TAG, ""
                                +SORTBYNAME
                                + SORTBYNAMEREVERSED
                                +SORTBYMATH
                                +SORTBYREADING
                                +SORTBYWRITING
                                +SORTBYTAKER)
                    }
                    R.id.sort_by_name_z_to_a -> {
                        SORTBYNAME = false
                        SORTBYNAMEREVERSED = true
                        SORTBYMATH = false
                        SORTBYREADING = false
                        SORTBYWRITING = false
                        SORTBYTAKER = false
                        Log.d(TAG, ""
                                +SORTBYNAME
                                + SORTBYNAMEREVERSED
                                +SORTBYMATH
                                +SORTBYREADING
                                +SORTBYWRITING
                                +SORTBYTAKER)
                    }
                    R.id.sort_by_math_score -> {
                        SORTBYNAME = false
                        SORTBYNAMEREVERSED = false
                        SORTBYMATH = true
                        SORTBYREADING = false
                        SORTBYWRITING = false
                        SORTBYTAKER = false
                    }

                    R.id.sort_by_number_test_takers -> {
                        SORTBYNAME = false
                        SORTBYNAMEREVERSED = false
                        SORTBYMATH = false
                        SORTBYREADING = false
                        SORTBYWRITING = false
                        SORTBYTAKER = true
                    }
                    R.id.sort_by_critical_writing_score -> {

                        SORTBYNAME = false
                        SORTBYNAMEREVERSED = false
                        SORTBYMATH = false
                        SORTBYREADING = false
                        SORTBYWRITING = true
                        SORTBYTAKER = false
                    }
                    else -> {

                        SORTBYNAME = false
                        SORTBYNAMEREVERSED = false
                        SORTBYMATH = false
                        SORTBYREADING = true
                        SORTBYWRITING = false
                        SORTBYTAKER = false
                    }
                }


            }

        })


        applyButton.setOnClickListener {
            Log.d(TAG, "ApplyButton:"
                    +SORTBYNAME
                    + SORTBYNAMEREVERSED
                    +SORTBYMATH
                    +SORTBYREADING
                    +SORTBYWRITING
                    +SORTBYTAKER)

            if(SORTBYNAME) {
                viewModel.sortListByNameAToZ()
                SORTBYNAME = false
            }
            else if(SORTBYNAMEREVERSED){
                SORTBYNAMEREVERSED = false
                viewModel.sortListByNameZToA()
            }
            else if(SORTBYMATH){
                SORTBYMATH = false
                viewModel.sortByMathScore()
            }
            else if(SORTBYREADING){
                SORTBYREADING = false
                viewModel.sortByReadingScore()
            }
            else if(SORTBYWRITING){
                SORTBYWRITING = false
                viewModel.sortByWritingScore()
            }
            else if (SORTBYTAKER) {
                SORTBYTAKER = false
                viewModel.sortByNumberOfTakers()
            }

            drawerLayout.closeDrawer(GravityCompat.START)

        }

        clearButton.setOnClickListener {
            sortRadioGroup.clearCheck()
            SORTBYNAME = false
            SORTBYNAMEREVERSED = false
            SORTBYMATH = false
            SORTBYREADING = false
            SORTBYWRITING = false
            SORTBYTAKER= false
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    companion object{
        private const val TAG = "MainActivity"
        private var SORTBYNAME = false
        private var SORTBYNAMEREVERSED = false
        private var SORTBYMATH = false
        private var SORTBYREADING = false
        private var SORTBYWRITING = false
        private var SORTBYTAKER= false
    }




}