package com.clp.tasks.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.clp.tasks.R
import com.clp.tasks.databinding.FragmentTasksBinding
import com.clp.tasks.utils.UiState

private const val TAG = "TasksFragment"
class TasksFragment : BaseFragment() {

    private val binding by lazy {
        FragmentTasksBinding.inflate(layoutInflater)
    }

    private val tasksAdapter by lazy {
        TasksAdapter(
            onCompleteTask = { task ->
                task.completed = true
                viewModel.insertTask(task)
            },
            onDeleteTask = {task ->
                viewModel.deleteTask(task)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding.tasksList.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = tasksAdapter
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.logOut()
        }

        binding.addTask.setOnClickListener {
            findNavController().navigate(R.id.action_tasks_to_add_task)
        }

        updateTasks()
        viewModel.getTasks()
        handleLogOut()

        return binding.root
    }

    private fun updateTasks(){
        viewModel.tasksState.observe(viewLifecycleOwner){state ->
            when (state){
                is UiState.ERROR -> {
                    Log.e(TAG, "error: ${state.error.localizedMessage}", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiState.LOADING -> {

                }
                is UiState.SUCCESS -> {
                    tasksAdapter.updateTasks(state.message)
                }
            }
        }
        viewModel.deleteTask.observe(viewLifecycleOwner){state ->
            when (state){
                is UiState.ERROR -> {
                    Log.e(TAG, "error: ${state.error.localizedMessage}", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiState.LOADING -> {

                }
                is UiState.SUCCESS -> {
                    Toast.makeText(requireContext(),"Task deleted successfully",Toast.LENGTH_LONG).show()
                    viewModel.getTasks()
                }
            }
        }
        viewModel.insertTask.observe(viewLifecycleOwner){state ->
            when (state){
                is UiState.ERROR -> {
                    Log.e(TAG, "error: ${state.error.localizedMessage}", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiState.LOADING -> {

                }
                is UiState.SUCCESS -> {
                    Toast.makeText(requireContext(),"Task updated successfully",Toast.LENGTH_LONG).show()
                    viewModel.getTasks()
                }
            }
        }
    }

    private fun handleLogOut(){
        viewModel.logoutState.observe(viewLifecycleOwner){state ->
            when(state){
                is UiState.ERROR -> {
                    Log.e(TAG, "error: ${state.error.localizedMessage}", )
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
                }
                UiState.LOADING -> {

                }
                is UiState.SUCCESS -> {
                    findNavController().navigate(R.id.action_tasks_to_login)
                }
            }
        }
    }

}