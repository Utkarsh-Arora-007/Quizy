package com.bitwisor.quizy.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitwisor.quizy.LoginActivity
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentAddQuestionsBinding
import com.bitwisor.quizy.utils.QuizQuestions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddQuestionsFragment : Fragment() {
    lateinit var binding:FragmentAddQuestionsBinding
    var question =""
    var option1 =""
    var option2 =""
    var option3 =""
    var option4 =""
    var correctOption =""
    var questionNumber =1
    var totalQuestions = 0
    var quizId = 0
    lateinit var database: FirebaseDatabase
    lateinit var firebaseAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddQuestionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mprogress.visibility = View.GONE
        var bundle = this.arguments
        if(bundle!=null){
            totalQuestions = bundle.getInt("NoOfQuestion")
            quizId = bundle.getInt("QuizId")
        }
        binding.questionNumber.text = "Q.No. $questionNumber"
        binding.quizRoomCode.text = "$quizId"
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        if (firebaseAuth.currentUser==null){
            val i = Intent(requireActivity(),LoginActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }
        binding.addnextQuestionbtn.setOnClickListener {
            binding.questionNumber.text = "Q.No. $questionNumber"
            question = binding.questionedittxt.text.toString()
            option1 = binding.optionAedittxt.text.toString()
            option2 = binding.optionBedittxt.text.toString()
            option3 = binding.optionCedittxt.text.toString()
            option4 = binding.optionDedittxt.text.toString()
            correctOption = binding.correctOptiontxt.text.toString()
            if(!question.isNullOrEmpty() and
                !option1.isNullOrEmpty() and
                !option2.isNullOrEmpty() and
                !option3.isNullOrEmpty() and
                !option4.isNullOrEmpty() and
                !correctOption.isNullOrEmpty()){
                binding.mprogress.visibility = View.VISIBLE
                val questionData = QuizQuestions(question,option1,option2,option3,option4,correctOption)
                database.reference.child("UserProfiles")
                    .child(firebaseAuth.currentUser!!.uid)
                    .child("MyQuiz")
                    .child(quizId.toString())
                    .child("Questions")
                    .child(questionNumber.toString())
                    .setValue(questionData)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            binding.mprogress.visibility = View.GONE
                            Snackbar.make(view,"Question $questionNumber added",Snackbar.LENGTH_SHORT).show()
                            binding.questionedittxt.text.clear()
                            binding.optionAedittxt.text.clear()
                            binding.optionBedittxt.text.clear()
                            binding.optionCedittxt.text.clear()
                            binding.optionDedittxt.text.clear()
                            binding.correctOptiontxt.text.clear()
                        }
                        else{
                            binding.mprogress.visibility = View.GONE
                            Snackbar.make(view,"Question $questionNumber Add Failed Please Retry",Snackbar.LENGTH_SHORT).show()

                        }
                    }.addOnFailureListener {
                        Snackbar.make(view,"Failed to Add Questions Please Retry",Snackbar.LENGTH_SHORT).show()
                    }


            }
            else{
                Snackbar.make(view,"Please Fill All the Fields",Snackbar.LENGTH_SHORT).show()
            }
            questionNumber++
        }
    }

}