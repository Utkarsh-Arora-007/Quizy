package com.bitwisor.quizy.fragments.joinFragments

import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentJoinHomeBinding

class JoinHomeFragment : Fragment() {
    lateinit var binding: FragmentJoinHomeBinding
    var quizid = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findQuizBtn.setOnClickListener {
            quizid = binding.quizRoomCodeEditText.text.toString()
            var bundle= Bundle()
            bundle.putString("QuizId",quizid)
            findNavController().navigate(R.id.action_joinHomeFragment_to_quizDetailsFragment,bundle)
        }

    }

}