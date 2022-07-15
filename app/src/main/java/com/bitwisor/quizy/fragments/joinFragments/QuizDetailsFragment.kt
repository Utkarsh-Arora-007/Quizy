package com.bitwisor.quizy.fragments.joinFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentQuizDetailsBinding


class QuizDetailsFragment : Fragment() {
    lateinit var binding: FragmentQuizDetailsBinding
    var quizCode:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentQuizDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bundle = arguments
        if(bundle!=null){
            quizCode = bundle.getString("QuizId","NONE")
        }
        binding.quizCodetxt.text = quizCode
    }
}