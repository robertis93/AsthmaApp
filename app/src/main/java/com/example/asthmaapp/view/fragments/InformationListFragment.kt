package com.example.asthmaapp.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentInformationListBinding
import com.example.asthmaapp.view.adapters.MeasureListAdapter
import com.example.asthmaapp.viewmodel.viewModels.InformationListViewModel

class InformationListFragment : BaseFragment<FragmentInformationListBinding>() {

    private val informationListViewModel: InformationListViewModel by lazy {
        ViewModelProvider(this).get(InformationListViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): FragmentInformationListBinding =
        FragmentInformationListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val measureListAdapter = MeasureListAdapter(informationListViewModel)
        val recyclerView = binding.measureListRecyclerView
        recyclerView.adapter = measureListAdapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        informationListViewModel.takeMedicamentTimeGroupByDate.observe(
            viewLifecycleOwner,
            Observer { measure ->
                measureListAdapter.setData(measure)
                if (measureListAdapter.itemCount == 0) {
                    binding.addMeasureButton.visibility = View.VISIBLE
                    binding.listFragmentAddMeasure.visibility = View.VISIBLE
                    binding.addFloatingActionButton.visibility = View.GONE
                    binding.deleteFloatingActionButton.visibility = View.GONE
                } else if (measureListAdapter.itemCount > 0) {
                    binding.addMeasureButton.visibility = View.GONE
                    binding.listFragmentAddMeasure.visibility = View.GONE
                    binding.addFloatingActionButton.visibility = View.VISIBLE
                    binding.deleteFloatingActionButton.visibility = View.VISIBLE
                }
            }
        )

        informationListViewModel.getAllMeasures.observe(viewLifecycleOwner) {
                timeMeasure -> measureListAdapter.addMeasures(timeMeasure)
        }

        binding.addMeasureButton.setOnClickListener {
            val action = InformationListFragmentDirections.actionListFragmentToAddFragment(null)
            findNavController().navigate(action)
        }

        binding.addFloatingActionButton.setOnClickListener {
            val action = InformationListFragmentDirections.actionListFragmentToAddFragment(null)
            findNavController().navigate(action)
        }

        binding.deleteFloatingActionButton.setOnClickListener {
            deleteAllMeasure()
        }
    }

    override fun onResume() {
        super.onResume()
        informationListViewModel.getAllMeasuresAndTakeMedicamentTime()
    }

    private fun deleteAllMeasure() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            informationListViewModel.deleteAllMeasuresWithMedicaments()

            Toast.makeText(
                requireContext(),
                R.string.all_deleted,
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton(R.string.no) { _, _ -> }
        builder.setMessage(R.string.are_you_sure_delete_all)
        builder.create().show()
    }
}