package com.bitwisor.quizy.fragments.joinFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.databinding.FragmentQuizDetailsBinding
import com.bitwisor.quizy.utils.QuizInfoOfUser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.annotations.NotNull


class QuizDetailsFragment : Fragment() {
    lateinit var binding: FragmentQuizDetailsBinding
    var quizCode:String=""
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database: DatabaseReference
    var quizName:String =" "
    var quizId:String =" "
    var currDate:String =" "
    var currMonth:String =" "
    var currYear:String =" "
    var currTimeHr:String =" "
    var currTimeMin:String =" "
    var endDate:String =" "
    var endMonth:String =" "
    var endYear:String =" "
    var endTimeHr:String =" "
    var endTimeMin:String =" "
    var duration:String =" "
    var numberOfQuestions:String =" "
    var isExpired:Boolean = false
    var isQuizExist = false
    var androidId =""
    var displayName =""
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
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("LeaderBoard")
        if(bundle!=null){
            quizCode = bundle.getString("QuizId","NONE")
            displayName = bundle.getString("DisplayName","NONE")
            androidId = bundle.getString("androidId","NONE")
        }
        binding.quizCodetxt.text = quizCode
        binding.mprogresscircle.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().reference
            .child("JoinRooms")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(@NotNull snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        for (child in snapshot.getChildren()) {
                            val key = child.key
                            val value = child.value.toString()
                            if (quizCode == key){
                                binding.mprogresscircle.visibility = View.GONE
                                isQuizExist = true
                                quizName =" ${child.child("quizName").value}"
                                quizId =" ${child.child("quizId").value}"
                                currDate =" ${child.child("currDate").value}"
                                currMonth =" ${child.child("currMonth").value}"
                                currYear =" ${child.child("currYear").value}"
                                currTimeHr =" ${child.child("currTimeHr").value}"
                                currTimeMin =" ${child.child("currTimeMin").value}"
                                endDate =" ${child.child("endDate").value}"
                                endMonth =" ${child.child("endMonth").value}"
                                endYear =" ${child.child("endYear").value}"
                                endTimeHr =" ${child.child("endTimeHr").value}"
                                endTimeMin =" ${child.child("endTimeMin").value}"
                                duration =" ${child.child("duration").value}"
                                numberOfQuestions =" ${child.child("numberOfQuestions").value}"
                                if(child.child("isExpired").value == null){
                                    isExpired  = false
                                }
                                else{
                                    isExpired  =child.child("isExpired").value as Boolean
                                }

                                break
                            }
                        }
                        if(isQuizExist){
                            binding.quizNametxtview.text = quizName
                            binding.quizDurationview.text = duration
                            if (isExpired){
                                binding.quizAvailtxtview.text = " Expired "
                            }
                            else{
                                binding.quizAvailtxtview.text = " Available "
                            }
                            binding.quizNoQuestiontxtview.text = numberOfQuestions
                        }
                        else{
                            binding.mprogresscircle.visibility = View.GONE
                            Snackbar.make(view,"Quiz doesn't exists",Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(@NotNull error: DatabaseError) {
                    Snackbar.make(view,"Error in database",Snackbar.LENGTH_SHORT).show()

                }
            })

        binding.startQuizBtn.setOnClickListener {
            var isAlreadyGiven = false
            binding.mprogresscircle.visibility = View.VISIBLE
            FirebaseDatabase.getInstance().reference
                .child("LeaderBoard")
                .child(quizCode)
                .child(androidId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(@NotNull snapshot: DataSnapshot) {
                        if(snapshot.value!=null){
                            binding.mprogresscircle.visibility = View.GONE
                            isAlreadyGiven = true
                            if (isQuizExist and !isExpired and !isAlreadyGiven){
                                var bundle:Bundle = Bundle()
                                bundle.putString("QuizCode",quizCode)
                                database.child(quizCode).child(androidId).child("DisplayName").setValue(displayName)
                                database.child(quizCode).child(androidId).child("Score").setValue("0")
                                database.child(quizCode).child(androidId).child("isAlreadyGiven").setValue(true)
                                findNavController().navigate(com.bitwisor.quizy.R.id.action_quizDetailsFragment_to_playQuizFragment,bundle)
                                Snackbar.make(view,"Quiz id : ${quizCode}",Snackbar.LENGTH_SHORT).show()
                            }
                            else{
                                if (isAlreadyGiven){
                                    Snackbar.make(view,"You have already attempted.",Snackbar.LENGTH_SHORT).show()
                                }
                                else{
                                    Snackbar.make(view,"Unavailable to Play",Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                        else{
                            binding.mprogresscircle.visibility = View.GONE
                            isAlreadyGiven = false
                            if (isQuizExist and !isExpired and !isAlreadyGiven){
                                var bundle:Bundle = Bundle()
                                bundle.putString("QuizCode",quizCode)
                                database.child(quizCode).child(androidId).child("DisplayName").setValue(displayName)
                                database.child(quizCode).child(androidId).child("Score").setValue("0")
                                database.child(quizCode).child(androidId).child("isAlreadyGiven").setValue(true)
                                findNavController().navigate(com.bitwisor.quizy.R.id.action_quizDetailsFragment_to_playQuizFragment,bundle)
                                Snackbar.make(view,"Quiz id : ${quizCode}",Snackbar.LENGTH_SHORT).show()
                            }
                            else{
                                if (isAlreadyGiven){
                                    Snackbar.make(view,"You have already attempted.",Snackbar.LENGTH_SHORT).show()
                                }
                                else{
                                    Snackbar.make(view,"Unavailable to Play",Snackbar.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }

                    override fun onCancelled(@NotNull error: DatabaseError) {
                        Snackbar.make(view,"Error in database",Snackbar.LENGTH_SHORT).show()

                    }
                })

        }
    }
}