package com.example.asthmaapp.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentListBinding
import com.example.asthmaapp.view.adapters.ListAdapter
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel


class ListFragment : Fragment() {

    private lateinit var mMeasureViewModel: MeasureOfDayViewModel
    lateinit var binding: FragmentListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //был виден action bar
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        //recycler
        val adapter = ListAdapter()
        val recyclerView = binding.listRecycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)


        //MeasureViewModel
        mMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
        mMeasureViewModel.readMedicamentAndMeasure.observe(viewLifecycleOwner, Observer { measure ->
            adapter.setDayData(measure)
            if (adapter.itemCount == 0) {
                binding.buttonAdd.visibility = View.VISIBLE
                binding.listFragmentAddMeasure.visibility = View.VISIBLE
            }
            else if (adapter.itemCount > 0){
                binding.buttonAdd.visibility = View.GONE
                binding.listFragmentAddMeasure.visibility = View.GONE
            }

        })

        mMeasureViewModel.readAllTimeAndMeasure.observe(
            viewLifecycleOwner,
            Observer { timeMeasure ->
                adapter.addTimeAndMeasure(timeMeasure)
            })

        //add menu
        setHasOptionsMenu(true)


        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteAllMeasure()
        }
        if (item.itemId == R.id.menu_add) {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllMeasure() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(R.string.yes) { _, _ ->
            mMeasureViewModel.deleteAllMeasure()
            mMeasureViewModel.deleteAllTimeMeasure()
            mMeasureViewModel.deleteAllMedicalTime()

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