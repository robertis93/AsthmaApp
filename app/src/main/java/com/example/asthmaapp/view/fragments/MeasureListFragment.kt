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
import com.example.asthmaapp.viewmodel.viewModels.MeasureListViewModel

class MeasureListFragment : BaseFragment<FragmentMeasureListBinding>() {

    private val measureListViewModel: MeasureListViewModel by lazy {
        ViewModelProvider(this).get(MeasureListViewModel::class.java)
    }

    override fun inflate(inflater: LayoutInflater): FragmentMeasureListBinding =
        FragmentMeasureListBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val measureListAdapter = MeasureListAdapter(measureListViewModel)
        val recyclerView = binding.measureListRecyclerView
        recyclerView.adapter = measureListAdapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        measureListViewModel.takeMedicamentTimeGroupByDate.observe(
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

        measureListViewModel.getAllMeasures.observe(viewLifecycleOwner) {
                timeMeasure -> measureListAdapter.addMeasures(timeMeasure)
        }


        binding.addMeasureButton.setOnClickListener {
            val action = MeasureListFragmentDirections.actionListFragmentToAddFragment(null)
            findNavController().navigate(action)
        }

        binding.addFloatingActionButton.setOnClickListener {
            val action = MeasureListFragmentDirections.actionListFragmentToAddFragment(null)
            findNavController().navigate(action)
        }

        binding.deleteFloatingActionButton.setOnClickListener {
            deleteAllMeasure()
        }
    }

    override fun onResume() {
        super.onResume()
        measureListViewModel.getAllMeasuresAndTakeMedicamentTime()
    }

    private fun deleteAllMeasure() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            measureListViewModel.deleteAllMeasuresWithMedicaments()

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