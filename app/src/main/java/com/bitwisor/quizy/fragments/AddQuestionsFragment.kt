package com.bitwisor.quizy.fragments

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.LoginActivity
import com.bitwisor.quizy.MainActivity
import com.bitwisor.quizy.databinding.FragmentAddQuestionsBinding
import com.bitwisor.quizy.utils.QuizQuestions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AddQuestionsFragment : Fragment() {
    lateinit var binding: FragmentAddQuestionsBinding
    var question =""
    var option1 =""
    var option2 =""
    var option3 =""
    var option4 =""
    var correctOption =""
    var questionNo =1
    var totalQuestions = 0
    var quizId = 0
    var quizName=""
    lateinit var database: FirebaseDatabase
    lateinit var firebaseAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionsBinding.inflate(layoutInflater)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
                val i = Intent(requireActivity(), MainActivity::class.java)
                startActivity(i)
                requireActivity().finish()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mprogress.visibility = View.GONE
        binding.donelottie.visibility = View.GONE

        var bundle = this.arguments
        if(bundle!=null){
            totalQuestions = bundle.getInt("NoOfQuestion")
            quizId = bundle.getInt("QuizId")
            quizName = bundle.getString("QuizName","No Name")
        }
        binding.yourlastscoretxt.visibility = View.INVISIBLE
        binding.yourscore.visibility = View.INVISIBLE
        binding.questionNumber.text = "Q.No. $questionNo"
        binding.quizRoomCode.text = "Quiz Code : $quizId"
        binding.quizNametxt.text = "$quizName"
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding.progressBar1.progress = 0
        binding.progressBar1.max = totalQuestions
        if (firebaseAuth.currentUser==null){
            val i = Intent(requireActivity(),LoginActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }//You need to add the following line for this solution to work; thanks skayred
        //You need to add the following line for this solution to work; thanks skayred
        // .getView().setFocusableInTouchMode(true)

        binding.addnextQuestionbtn.setOnClickListener {
            val qnu = questionNo

            question = binding.questionedittxt.text.toString()
            option1 = binding.optionAedittxt.text.toString()
            option2 = binding.optionBedittxt.text.toString()
            option3 = binding.optionCedittxt.text.toString()
            option4 = binding.optionDedittxt.text.toString()
            correctOption = binding.correctOptiontxt.text.toString()
            if(!question.isNullOrEmpty() &&
                !option1.isNullOrEmpty() &&
                !option2.isNullOrEmpty() &&
                !option3.isNullOrEmpty() &&
                !option4.isNullOrEmpty() &&
                !correctOption.isNullOrEmpty()){
                if (checkoptions(option1,option2,option3,option4)){

                    var options = arrayOf(option1,option2,option3,option4)
                    if(isCorrectOptionValid(options,correctOption)){
                        binding.mprogress.visibility = View.VISIBLE
                        val questionData = QuizQuestions(question,option1,option2,option3,option4,correctOption)

                        database.reference.child("UserProfiles")
                            .child(firebaseAuth.currentUser!!.uid)
                            .child("MyQuiz")
                            .child(quizId.toString())
                            .child("Questions")
                            .child(qnu.toString())
                            .setValue(questionData)
                            .addOnCompleteListener {
                                if (it.isSuccessful){
                                    database.reference.child("JoinRooms")
                                        .child(quizId.toString())
                                        .child("Questions")
                                        .child(qnu.toString())
                                        .setValue(questionData)
                                        .addOnCompleteListener {
                                            binding.mprogress.visibility = View.GONE
                                            Snackbar.make(view,"Question $qnu added",Snackbar.LENGTH_SHORT).show()
                                            binding.questionedittxt.text.clear()
                                            binding.optionAedittxt.text.clear()
                                            binding.optionBedittxt.text.clear()
                                            binding.optionCedittxt.text.clear()
                                            binding.optionDedittxt.text.clear()
                                            binding.correctOptiontxt.text.clear()
                                            binding.progressBar1.progress = questionNo
                                            questionNo++
                                            binding.questionNumber.text = "Q.No. $questionNo"
                                            // correct option options me se he hona chahiye
                                            if(questionNo>totalQuestions){
                                                binding.questionNumber.text = "Done"
                                                binding.donelottie.visibility = View.VISIBLE
                                                binding.addnextQuestionbtn.visibility = View.GONE
                                                binding.doneQuestionAddingbtn.visibility = View.VISIBLE
                                                binding.qna.visibility = View.INVISIBLE
                                            }
                                        }.addOnFailureListener {
                                            binding.mprogress.visibility = View.GONE
                                            Snackbar.make(view,"Question $questionNo Add Failed Please Retry",Snackbar.LENGTH_SHORT).show()
                                        }

                                }
                                else{
                                    binding.mprogress.visibility = View.GONE
                                    Snackbar.make(view,"Question $questionNo Add Failed Please Retry",Snackbar.LENGTH_SHORT).show()

                                }
                            }.addOnFailureListener {
                                Snackbar.make(view,"Failed to Add Questions Please Retry",Snackbar.LENGTH_SHORT).show()
                            }

                    }
                    else{
                        Snackbar.make(view,"Correct option must be from all the given options",Snackbar.LENGTH_SHORT).show()
                    }

                }
                else{
                    Snackbar.make(view,"All options must be unique",Snackbar.LENGTH_SHORT).show()
                }
            }
            else{
                Snackbar.make(view,"Please Fill All the Fields",Snackbar.LENGTH_SHORT).show()
            }

        }
        binding.doneQuestionAddingbtn.setOnClickListener {
            findNavController().popBackStack()

        }
    }

    private fun checkoptions(option1: String, option2: String, option3: String, option4: String): Boolean {
        if ( option1 != option2 && option1!=option3 && option1!=option4 && option2!=option3 && option2!=option4 && option3!=option4){
            return true
        }
        return false
    }


    private fun isCorrectOptionValid(options: Array<String>, correctOption: String): Boolean {
        for (option in options){
            if (correctOption == option){
                return true
            }
        }
        return false
    }

}