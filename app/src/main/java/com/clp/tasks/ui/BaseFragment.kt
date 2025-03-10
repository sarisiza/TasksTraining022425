package com.clp.tasks.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.clp.tasks.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    protected val viewModel by viewModels<TasksViewModel>()

}