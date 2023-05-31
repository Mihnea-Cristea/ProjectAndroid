package com.appdem.studentsFeature

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.appdem.R
import com.appdem.databinding.FragmentOneBinding
import java.util.*

class FragmentStudents : Fragment() {

    private lateinit var binding: FragmentOneBinding
    private val viewModel: FragmentStudentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_one, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)
        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search by name"
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchStudent(query)
                }
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun searchStudent(query: String) {
        val filtered = ArrayList<StudentMock>()
        for (item in viewModel.studentsMocked) {
            if (item.name.lowercase().contains(query.lowercase())) {
                filtered.add(item)
            }
        }
        if (filtered.isEmpty()) {
            Toast.makeText(this.context, "No Student Found..", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.adapter.filterList(filtered)
        }
    }
}