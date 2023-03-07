package com.example.a20230301_hz_nycschools.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.a20230301_hz_nycschools.R
import com.example.a20230301_hz_nycschools.databinding.FragmentSchoolsBinding
import com.example.a20230301_hz_nycschools.network.getNetworkService
import com.example.a20230301_hz_nycschools.repository.SchoolRepository
import com.example.a20230301_hz_nycschools.viewmodel.SchoolViewModel
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass to hold the list of schools
 * Use the [SchoolsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SchoolsFragment : Fragment() {
    private val TAG = "SchoolsFragment"

    private var _binding: FragmentSchoolsBinding? = null
    private val binding get() = _binding!!
    private lateinit var schoolsItemAdatper: SchoolsAdapter
    private lateinit var rootLayoutBinding:ConstraintLayout

    private lateinit var spinnerBinding: ProgressBar

//    val database = getDatabase(this)
    val repository = SchoolRepository(getNetworkService(), )
    private val viewModel by lazy {ViewModelProvider(this, SchoolViewModel.FACTORY(repository))
        .get(SchoolViewModel::class.java)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_schools, container, false
        )

        schoolsItemAdatper = SchoolsAdapter()


        with(binding){
            recyclerview.apply{
                adapter = schoolsItemAdatper
                addItemDecoration(
                    RecyclerViewItemDecoration(
                        spacing = resources.getDimensionPixelSize(R.dimen.recycler_view_item_spacing)
                    )
                )
            }

            rootLayoutBinding = rootLayout



        }
        spinnerBinding = binding.spinner
        setHasOptionsMenu(true)
        setupSearch()
        subscribeUI()
        return binding.root
    }

    private fun setupSearch() = with(binding) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    viewModel.hideSpinner(true)
                    viewModel.hideSnackbar("")

                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchSchool(newText.uppercase())
                return true
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getShareIntent() : Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_success_text ))
        return shareIntent
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
        if(getShareIntent().resolveActivity(requireActivity().packageManager)==null){
            menu.findItem(R.id.share).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeUI() = with(viewModel) {
        schoolsWithSATListLivedata.observe(viewLifecycleOwner) { value ->
            value?.let {
                Log.d(TAG,it.joinToString { schoolWithSatResult ->  schoolWithSatResult.schoolName })

                schoolsItemAdatper.submitList(it)

            }?: run{
                Log.d(TAG,"No school from View Model")
            }
        }



        // show the spinner when [MainViewModel.spinner] is true
        spinner.observe(viewLifecycleOwner) { value ->
            value.let { show ->
                spinnerBinding.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(rootLayoutBinding, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        }
    }

}









