package com.example.asthmaapp.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentMeasureListBinding
import com.example.asthmaapp.view.adapters.MeasureListAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel

class MeasureListFragment : Fragment() {

    private lateinit var binding: FragmentMeasureListBinding
    private lateinit var measureViewModel: MeasureOfDayViewModel

    // TODO: вынести в базовый класс
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeasureListBinding.inflate(inflater, container, false)
        return binding.root
    }

   // TODO: разделить логичные, простые, маленькие и понятные методы
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.addMeasureButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.addFloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        binding.deleteFloatingActionButton.setOnClickListener {
            deleteAllMeasure()
        }

        val adapter = MeasureListAdapter()
        val recyclerView = binding.measureListRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)

        measureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
       // TODO
/*        measureViewModel.readMedicamentAndMeasure.observe(viewLifecycleOwner, Observer { measure ->
            adapter.setDayData(measure)
            if (adapter.itemCount == 0) {
                binding.addMeasureButton.visibility = View.VISIBLE
                binding.listFragmentAddMeasure.visibility = View.VISIBLE
                binding.addFloatingActionButton.visibility = View.GONE
                binding.deleteFloatingActionButton.visibility = View.GONE
            } else if (adapter.itemCount > 0) {
                binding.addMeasureButton.visibility = View.GONE
                binding.listFragmentAddMeasure.visibility = View.GONE
                binding.addFloatingActionButton.visibility = View.VISIBLE
                binding.deleteFloatingActionButton.visibility = View.VISIBLE
            }
        })

        measureViewModel.readAllTimeAndMeasure.observe(
            viewLifecycleOwner,
            { timeMeasure -> adapter.addTimeAndMeasure(timeMeasure) }
        )*/
    }

    private fun deleteAllMeasure() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            measureViewModel.deleteAllMeasure()
            measureViewModel.deleteAllTimeMeasure()
            measureViewModel.deleteAllMedicalTime()

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