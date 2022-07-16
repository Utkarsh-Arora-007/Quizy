package com.bitwisor.quizy.fragments.joinFragments

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentResultFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ResultFragmentFragment : Fragment() {

    lateinit var binding:FragmentResultFragmentBinding
    var quizName=""
    var numberOfQuestion:String=""
    var displayName =""
    var score:String =""
    var quizCode=""
    var androidId=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resultMprogress.visibility = View.VISIBLE
        var bundle =arguments
        androidId= Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        if (bundle!=null){
            quizCode = bundle.getString("QuizCode","None")
        }
        binding.resultsBackbtn.setOnClickListener { 
            findNavController().popBackStack()
        }
        binding.goToHomeBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        Log.e("MYQUIZCODE",quizCode)
        FirebaseDatabase.getInstance().reference
            .child("JoinRooms")
            .child(quizCode)
            .child("Details")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot!=null){
                        quizName = "${snapshot.child("quizName").value}"
                        numberOfQuestion = "${snapshot.child("numberOfQuestions").value}"
                        FirebaseDatabase.getInstance().reference
                            .child("LeaderBoard")
                            .child(quizCode)
                            .child(androidId)
                            .addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot!=null){
                                        binding.resultMprogress.visibility = View.GONE
                                        displayName = "${snapshot.child("DisplayName").value}"
                                        score = "${snapshot.child("Score").value}"

                                        binding.resultQuizNametxtview.text = quizName
                                        binding.resultQuizNoQuestiontxtview.text = numberOfQuestion
                                        binding.resultQuizDurationview.text = displayName
                                        binding.resultQuizAvailtxtview.text = score
                                    }
                                    else{
                                        Snackbar.make(view,"Some Error Occured",Snackbar.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Snackbar.make(view,"Database Error Occured",Snackbar.LENGTH_SHORT).show()
                                }

                            })
                    }
                    else{
                        Snackbar.make(view,"Database Error Occured",Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Snackbar.make(view,"Some Error Occured",Snackbar.LENGTH_SHORT).show()
                }

            })

    }


}