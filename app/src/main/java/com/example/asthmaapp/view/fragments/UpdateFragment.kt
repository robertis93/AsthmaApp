package com.example.asthmaapp.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.FragmentUpdateBinding
import com.example.asthmaapp.model.MeasureOfDay
import com.example.asthmaapp.viewmodel.viewModels.MeasureOfDayViewModel
import kotlinx.android.synthetic.main.fragment_update.view.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    lateinit var binding: FragmentUpdateBinding

    private lateinit var mMeasureViewModel: MeasureOfDayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMeasureViewModel = ViewModelProvider(this).get(MeasureOfDayViewModel::class.java)
        view.time1Update.setText(args.currentMeasure.firstTime)
        view.time2Update.setText(args.currentMeasure.secondTime)
        view.time3Update.setText(args.currentMeasure.thirdTime)

        view.saveBtnUpdate.setOnClickListener {
            updateItem()
        }
        //addMenu
        setHasOptionsMenu(true)
    }

    private fun updateItem() {
        val firstTime = binding.time1Update.text.toString()
        val secondTime = binding.time2Update.text.toString()
        val thirdTime = binding.time3Update.text.toString()
        val updateMeasure =
            MeasureOfDay(args.currentMeasure.id, 100, 11, 111, firstTime, secondTime, thirdTime)
        mMeasureViewModel.updateMeasure(updateMeasure)

        Toast.makeText(requireContext(), "Updated success", Toast.LENGTH_SHORT).show()

        //navigate back
        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteMeasure()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteMeasure() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mMeasureViewModel.deleteMeasure(args.currentMeasure)
            Toast.makeText(
                requireContext(),
                "Successfully removed: ${args.currentMeasure.firstTime}",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentMeasure.firstTime}?")
        builder.setMessage("Are you sure want to delete ${args.currentMeasure.firstTime} ?")
        builder.create().show()

    }
}