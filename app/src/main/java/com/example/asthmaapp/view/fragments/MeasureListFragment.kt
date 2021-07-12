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
import com.example.asthmaapp.databinding.FragmentMeasureListBinding
import com.example.asthmaapp.view.adapters.MeasureListAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasurementsPerDayViewModel

class MeasureListFragment : BaseFragment<FragmentMeasureListBinding>() {

    private val measureViewModel: MeasurementsPerDayViewModel by lazy {
        ViewModelProvider(this).get(MeasurementsPerDayViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): FragmentMeasureListBinding =
        FragmentMeasureListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val measureListAdapter = MeasureListAdapter()
        val recyclerView = binding.measureListRecyclerView
        recyclerView.adapter = measureListAdapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        measureViewModel.takeMedicamentTimeGroupByDate.observe(
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

        measureViewModel.getAllMeasures.observe(
            viewLifecycleOwner,
            { timeMeasure -> measureListAdapter.addMeasure(timeMeasure) }
        )

        binding.addMeasureButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.addFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.deleteFloatingActionButton.setOnClickListener {
            deleteAllMeasure()
        }
    }

    override fun onResume() {
        super.onResume()
        measureViewModel.getAllMeasuresAndTakeMedicamentTime()
    }

    private fun deleteAllMeasure() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            measureViewModel.deleteAllMeasuresWithMedicaments()

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