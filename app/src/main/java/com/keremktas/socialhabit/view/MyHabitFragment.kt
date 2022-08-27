package com.keremktas.socialhabit.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keremktas.socialhabit.databinding.FragmentMyHabitBinding


class MyHabitFragment : Fragment() {

    private var _binding: FragmentMyHabitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentMyHabitBinding.inflate(inflater, container, false)
        var view = binding.root
        return view
    }
}