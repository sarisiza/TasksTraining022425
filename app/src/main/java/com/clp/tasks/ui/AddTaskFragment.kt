package com.clp.tasks.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.clp.tasks.R
import com.clp.tasks.data.room.Task
import com.clp.tasks.databinding.FragmentAddTaskBinding
import com.clp.tasks.utils.UiState
import java.time.LocalDate

private const val TAG = "AddTaskFragment"
class AddTaskFragment : BaseFragment() {

    private val binding by lazy {
        FragmentAddTaskBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding.addTaskBtn.setOnClickListener {
            val task = verifyValues()
            if(task != null){
                viewModel.insertTask(task)
            } else {
                Toast.makeText(requireContext(),"Verify your information",Toast.LENGTH_LONG).show()
            }
        }

        viewModel.insertTask.observe(viewLifecycleOwner){ state ->
            when (state){
                is UiState.ERROR -> {
                    Log.e(TAG, "onCreateView: ", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiState.LOADING -> {}
                is UiState.SUCCESS -> {
                    viewModel.getTasks()
                    findNavController().navigate(R.id.action_add_task_to_tasks)
                }
            }
        }

        return binding.root
    }

    private fun verifyValues(): Task? {
        return try {
            with(binding){
                val title = if(titleEt.text.isNotEmpty()) titleEt.text.toString() else throw Exception("Title is empty")
                val description = if(titleEt.text.isNotEmpty()) titleEt.text.toString() else throw Exception("Description is empty")
                val day = dayEt.text.toString().toInt()
                val month = monthEt.text.toString().toInt()
                val year = yearEt.text.toString().toInt()
                val date = LocalDate.of(year,month,day)
                Task(
                    title = title,
                    description = description,
                    dueDate = date
                )
            }
        } catch (e: Exception){
            null
        }
    }

}