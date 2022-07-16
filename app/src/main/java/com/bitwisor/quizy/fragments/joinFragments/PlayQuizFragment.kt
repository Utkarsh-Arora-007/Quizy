package com.bitwisor.quizy.fragments.joinFragments

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentPlayQuizBinding
import com.bitwisor.quizy.utils.QuizQuestions
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.annotations.NotNull
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class PlayQuizFragment : Fragment(),View.OnClickListener {

    lateinit var binding: FragmentPlayQuizBinding
    var quizCode = ""
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database: DatabaseReference
    var isQuizExist = true
    var quizName = ""
    var quizId = ""
    var duration = ""
    var numberOfQuestions = 0
    var questionNumber = 1
    var androidid=""
    var displayName=""
    var score =0
    var currentPosition = 1
    var selectedOptionPosition = 0
    var correct_answerInt = 0
    var START_TIME_IN_MILLIS by Delegates.notNull<Long>()
    lateinit var mCountDownTimer:CountDownTimer
    var mTimeLeftInMillis by Delegates.notNull<Long>()
    var mTimerRunning:Boolean = false
    var questionList=ArrayList<QuizQuestions>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayQuizBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bundle = arguments
        if(bundle!=null){
            quizCode = bundle.getString("QuizCode","0000")
        }
        androidid= Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("LeaderBoard")
        binding.quizRoomCode.text = quizCode
        binding.playquizMprogress.visibility = View.VISIBLE
        binding.playquizQuestionNumber.text = "Q.No. $questionNumber"
        // dont touch below line of code, for any doubt contact me
        FirebaseDatabase.getInstance().reference
            .child("JoinRooms")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(@NotNull snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        for (child in snapshot.getChildren()) {
                            val key = child.key
                            val value = child.value.toString()
                            if (quizCode == key){
                                binding.playquizMprogress.visibility = View.GONE
                                isQuizExist = true
                                quizName ="${child.child("quizName").value}"
                                quizId ="${child.child("quizId").value}"
                                duration ="${child.child("duration").value}"
                                numberOfQuestions ="${child.child("numberOfQuestions").value}".toInt()
                                for (i in child.child("Questions").children){
                                    val question = "${i.child("question").value}"
                                    val option1 = "${i.child("option1").value}"
                                    val option2 = "${i.child("option2").value}"
                                    val option3 = "${i.child("option3").value}"
                                    val option4 = "${i.child("option4").value}"
                                    val correctOption = "${i.child("correctOption").value}"
                                    val q = QuizQuestions(question,option1,option2,option3,option4,correctOption)
                                    questionList.add(q)
                                }
                                break
                            }
                        }
                        if(isQuizExist){
                            setQuestion()
                            binding.playquizQuizNametxt.text = quizName
                            binding.playquizProgressBar1.max = numberOfQuestions.toInt()
                            binding.playquizProgressBar1.progress = questionNumber
                            START_TIME_IN_MILLIS = duration.toLong() * 60000
                            mTimeLeftInMillis = START_TIME_IN_MILLIS
                            startTimer()
                        }
                        else{
                            binding.playquizMprogress.visibility = View.GONE
                            Snackbar.make(view,"Quiz doesn't exists", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(@NotNull error: DatabaseError) {
                    Snackbar.make(view,"Error in database", Snackbar.LENGTH_SHORT).show()

                }
            })

        if(isQuizExist){
            var mexist= false
            binding.playquizMprogress.visibility = View.VISIBLE
            FirebaseDatabase.getInstance().reference
                .child("LeaderBoard")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(@NotNull snapshot: DataSnapshot) {
                        if (snapshot.value != null) {
                            for (child in snapshot.getChildren()) {
                                val key = child.key
                                val value = child.value.toString()
                                if (quizCode == key){
                                    mexist = true
                                    binding.playquizMprogress.visibility = View.GONE
                                    displayName =" ${child.child(androidid).child("DisplayName").value}"
                                    score ="${child.child(androidid).child("Score").value}".toInt()
                                    break
                                }
                            }
                            if (mexist){
                                binding.yourscore.text = "$score"
                            }
                            else{
                                binding.playquizMprogress.visibility = View.GONE
                                Snackbar.make(view,"User Doesn't Exist", Snackbar.LENGTH_SHORT).show()

                            }

                        }
                    }

                    override fun onCancelled(@NotNull error: DatabaseError) {
                        Snackbar.make(view,"Error in database", Snackbar.LENGTH_SHORT).show()

                    }
                })
        }
        else{
            Snackbar.make(view,"Quiz doesn't exists", Snackbar.LENGTH_SHORT).show()
        }
        Log.e("QuestionList",questionList.toString())
        // All data has been fetched from firebase including questionList.

        binding.playquizOptionAbox.setOnClickListener (this)
        binding.playquizOptionBbox.setOnClickListener (this)
        binding.playquizOptionCbox.setOnClickListener (this)
        binding.playquizOptionDbox.setOnClickListener (this)
        binding.playquizNextQuestionbtn.setOnClickListener (this)
        binding.playquizFinishQuestionAddingbtn.setOnClickListener (this)

    }

    private fun startTimer() {
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimerRunning = false
                finishQuiz()
//                mButtonStartPause.setText("Start")
//                mButtonStartPause.setVisibility(View.INVISIBLE)
//                mButtonReset.setVisibility(View.VISIBLE)
            }
        }.start()
        mTimerRunning = true
//        mButtonStartPause.setText("pause")
//        mButtonReset.setVisibility(View.INVISIBLE)
    }

    private fun finishQuiz() {
        database.child(quizCode).child(androidid).child("Score").setValue(score)
        questionNumber = numberOfQuestions+1
        findNavController().navigate(R.id.action_playQuizFragment_to_resultFragmentFragment)
    }

    private fun pauseTimer() {
        mCountDownTimer.cancel()
        mTimerRunning = false
//        mButtonStartPause.setText("Start")
//        mButtonReset.setVisibility(View.VISIBLE)
    }

    private fun resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS
        updateCountDownText()
//        mButtonReset.setVisibility(View.INVISIBLE)
//        mButtonStartPause.setVisibility(View.VISIBLE)
    }

    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String =
            java.lang.String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

        binding.yourtimeleft.text = timeLeftFormatted
//        mTextViewCountDown.setText(timeLeftFormatted)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.playquiz_optionAbox->{
                selectedOptionView(binding.optionA,1)
                selectedOptionPosition = 1
            }
            R.id.playquiz_optionBbox->{
                selectedOptionView(binding.optionB,2)
                selectedOptionPosition = 2
            }
            R.id.playquiz_optionCbox->{
                selectedOptionView(binding.optionC,3)
                selectedOptionPosition = 3
            }
            R.id.playquiz_optionDbox->{
                selectedOptionView(binding.optionD,4)
                selectedOptionPosition = 4
            }
            R.id.playquiz_nextQuestionbtn->{

                if(selectedOptionPosition == 0){
                    questionNumber++
                    when{
                        questionNumber <= numberOfQuestions ->{
                            setQuestion()
                            enableClick()
                        }else->{
                        // finish quiz
                        disableClick()
                        Toast.makeText(requireContext(),"You have completed the quiz", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_playQuizFragment_to_resultFragmentFragment)
                    }
                    }
                }
                else{
                    disableClick()
                    when(questionList[questionNumber-1].correctOption){
                        questionList[questionNumber-1].option1->{
                            correct_answerInt = 1
                        }
                        questionList[questionNumber-1].option2->{
                            correct_answerInt = 2
                        }
                        questionList[questionNumber-1].option3->{
                            correct_answerInt = 3
                        }
                        questionList[questionNumber-1].option4->{
                            correct_answerInt = 4
                        }
                    }
                    if(correct_answerInt== selectedOptionPosition){
                        score+=10
                        binding.yourscore.text = "$score"
                        database.child(quizCode).child(androidid).child("Score").setValue(score)

                    }
                    if(correct_answerInt != selectedOptionPosition){
                        answer(selectedOptionPosition,R.drawable.red_bg)
                    }
                    answer(correct_answerInt,R.drawable.green_bg)
                    if (questionNumber == numberOfQuestions){
                        binding.playquizNextQuestionbtn.text ="Finish"
                    }
                    else{
                        binding.playquizNextQuestionbtn.text = " Next "
                    }
                    selectedOptionPosition = 0
                }

            }
        }
    }
    private fun setQuestion(){
        if(questionNumber>numberOfQuestions){
            Snackbar.make(binding.palyqwew,"Quiz already given", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }else{

            defaultOptionsView()
            binding.playquizNextQuestionbtn.text = "Submit"
            if (questionNumber == numberOfQuestions){
                binding.playquizNextQuestionbtn.text = "Finish"
            }
            else{
                binding.playquizNextQuestionbtn.text = " Next "
            }
            binding.playquizProgressBar1.progress = questionNumber
            binding.playquizQuestionNumber.text = "Q.No.$questionNumber"
            binding.playquizQuestiontxt.text =if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml( questionList[questionNumber-1].question, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(questionList[questionNumber-1].question)
            }
            binding.playquizOptionAedittxt.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml( questionList[questionNumber-1].option1, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml( questionList[questionNumber-1].option1)
            }
            binding.playquizOptionBedittxt.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml( questionList[questionNumber-1].option2, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml( questionList[questionNumber-1].option2 )
            }
            binding.playquizOptionCedittxt.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(questionList[questionNumber-1].option3, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(questionList[questionNumber-1].option3)
            }
            binding.playquizOptionDedittxt.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml( questionList[questionNumber-1].option4, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml( questionList[questionNumber-1].option4)
            }
        }


    }
    private fun defaultOptionsView(){
        val opt = ArrayList<ConstraintLayout>()
        opt.add(0,binding.optionA)
        opt.add(1,binding.optionB)
        opt.add(2,binding.optionC)
        opt.add(3,binding.optionD)

        for(o in opt){
            o.background = ContextCompat.getDrawable(requireContext(),R.drawable.white_bg)
        }

    }
    private fun selectedOptionView(mv:ConstraintLayout,selectedOption:Int){
        defaultOptionsView()
        selectedOptionPosition = selectedOption
        mv.background = ContextCompat.getDrawable(requireContext(),R.drawable.grey_bg)
    }
    private fun answer(ans:Int,drawable:Int){
        when (ans){
            1->{
                binding.optionA.background = ContextCompat.getDrawable(requireContext(),drawable)
            }
            2->{
                binding.optionB.background = ContextCompat.getDrawable(requireContext(),drawable)
            }
            3->{
                binding.optionC.background = ContextCompat.getDrawable(requireContext(),drawable)
            }
            4->{
                binding.optionD.background = ContextCompat.getDrawable(requireContext(),drawable)
            }
        }
    }
    private fun disableClick(){
        val opt = ArrayList<MaterialCardView>()
        opt.add(0,binding.playquizOptionAbox)
        opt.add(1,binding.playquizOptionBbox)
        opt.add(2,binding.playquizOptionCbox)
        opt.add(3,binding.playquizOptionDbox)
        for(o in opt){
            o.isClickable = false
        }
    }
    private fun enableClick(){
        val opt = ArrayList<MaterialCardView>()
        opt.add(0,binding.playquizOptionAbox)
        opt.add(1,binding.playquizOptionBbox)
        opt.add(2,binding.playquizOptionCbox)
        opt.add(3,binding.playquizOptionDbox)
        for(o in opt){
            o.isClickable = true
        }
    }
}