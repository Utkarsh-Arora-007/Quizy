package com.bitwisor.quizy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentUniqueCodeBinding


class UniqueCode : Fragment() {
    lateinit var binding: FragmentUniqueCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUniqueCodeBinding.inflate(layoutInflater)
        return binding.root
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView:TextView=binding.joiningCode
        val args=this.arguments
        val uniqueCode= args?.get("uniqueId")
    }


}