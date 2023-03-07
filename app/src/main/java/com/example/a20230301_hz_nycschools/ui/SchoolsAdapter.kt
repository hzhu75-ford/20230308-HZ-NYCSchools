package com.example.a20230301_hz_nycschools.ui

import android.content.Intent
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a20230301_hz_nycschools.databinding.ListSchoolsBinding
import com.example.a20230301_hz_nycschools.network.response.SchoolWithSATResult


class SchoolsAdapter :
    ListAdapter<SchoolWithSATResult, SchoolsAdapter.ViewHolder>(SchoolDiffCallback) {


    class ViewHolder private constructor(val binding:ListSchoolsBinding ) : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListSchoolsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }


        fun bind(item: SchoolWithSATResult){

            var loadMore = false
            var context = itemView.context

            with(binding) {
                schoolNameTextView.text = item.schoolName
                readingScoreTextView.text = item.sat_critical_reading_avg_score
                mathScoreTextView.text = item.sat_math_avg_score
                writingScoreTextView.text = item.sat_writing_avg_score
                numberEditTextView.text = item.num_of_sat_test_takers

                button.setOnClickListener {
                    loadMore = !loadMore

                    if( loadMore) {
                        button.text = "Show Less"
                        overviewParagraphEditText.text = item.overviewParagraph
                        overviewParagraph.visibility = View.VISIBLE
                        overviewParagraphEditText.visibility = View.VISIBLE
                        totalStudentsEditText.text = item.totalStudents
                        totalStudentsEditText.visibility = View.VISIBLE
                        totalStudents.visibility = View.VISIBLE
                        addressEditText.text = item.address1
                        addressEditText.visibility = View.VISIBLE
                        address.visibility = View.VISIBLE
                        cityEditText.text = item.city
                        cityEditText.visibility = View.VISIBLE
                        city.visibility = View.VISIBLE
                        stateEditText.visibility = View.VISIBLE
                        state.visibility = View.VISIBLE
                        phoneNumberEditText.text = item.phone_number
                        phoneNumberEditText.visibility = View.VISIBLE
                        phoneNumber.visibility = View.VISIBLE
                        schoolEmailEditText.text = item.school_email
                        schoolEmailEditText.visibility = View.VISIBLE
                        schoolEmail.visibility = View.VISIBLE
                        schoolWebsiteEditText.text = item.website
                        schoolWebsiteEditText.visibility = View.VISIBLE
                        schoolWebsite.visibility = View.VISIBLE

//                        schoolEmailEditText.setOnClickListener {
//
//                            val intent = Intent(Intent.ACTION_SEND)
//                            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(schoolEmailEditText))
//                            intent.putExtra(
//                                android.content.Intent.EXTRA_SUBJECT,
//                                "Subject for email"
//                            )
//                            intent.putExtra(
//                                android.content.Intent.EXTRA_TEXT,
//                                "Description for email"
//                            )
//
//                            startActivity(itemView.context,intent, bundleOf())
//                        }
                    }
                    else {
                        button.text = "Show More"
                        overviewParagraph.visibility = View.GONE
                        overviewParagraphEditText.visibility = View.GONE
                        totalStudentsEditText.visibility = View.GONE
                        totalStudents.visibility = View.GONE
                        addressEditText.visibility = View.GONE
                        address.visibility = View.GONE
                        cityEditText.visibility = View.GONE
                        city.visibility = View.GONE
                        stateEditText.visibility = View.GONE
                        state.visibility = View.GONE
                        phoneNumberEditText.visibility = View.GONE
                        phoneNumber.visibility = View.GONE
                        schoolEmailEditText.visibility = View.GONE
                        schoolEmail.visibility = View.GONE
                        schoolWebsiteEditText.visibility = View.GONE
                        schoolWebsite.visibility = View.GONE

                    }
                }

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

object SchoolDiffCallback : DiffUtil.ItemCallback<SchoolWithSATResult>(){
    override fun areItemsTheSame(oldItem: SchoolWithSATResult, newItem: SchoolWithSATResult) =oldItem.schoolName == newItem.schoolName


    override fun areContentsTheSame(oldItem: SchoolWithSATResult, newItem: SchoolWithSATResult)= oldItem == newItem

}
