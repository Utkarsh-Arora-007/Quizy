package com.bitwisor.quizy.fragments.joinFragments

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.databinding.FragmentQuizDetailsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.annotations.NotNull
import java.text.SimpleDateFormat
import java.util.*


class QuizDetailsFragment : Fragment() {
    lateinit var binding: FragmentQuizDetailsBinding
    var quizCode:String=""
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var database: DatabaseReference
    var quizName:String =" "
    var quizId:String =" "
    var fromDate =0L
    var fromMonth =0L
    var fromYear =0L
    var toMonth =0L
    var toDate =0L
    var toYear =0L
    var fromHr =0L
    var fromMin =0L
    var toHr =0L
    var toMin =0L
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
        }

        androidId= Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
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
                                quizName =" ${child.child("Details").child("quizName").value}"
                                quizId =" ${child.child("Details").child("quizId").value}"
                                fromDate = child.child("Details").child("fromDate").value as Long
                                fromMonth = child.child("Details").child("fromMonth").value as Long
                                fromYear = child.child("Details").child("fromYear").value as Long
                                toMonth = child.child("Details").child("toMonth").value as Long
                                toDate = child.child("Details").child("toDate").value as Long
                                toYear = child.child("Details").child("toYear").value as Long
                                fromHr = child.child("Details").child("fromHr").value as Long
                                fromMin = child.child("Details").child("fromMin").value as Long
                                toHr = child.child("Details").child("toHr").value  as Long
                                toMin = child.child("Details").child("toMin").value  as Long
                                duration =" ${child.child("Details").child("duration").value}"
                                numberOfQuestions =" ${child.child("Details").child("numberOfQuestions").value}"
                                if(child.child("Details").child("isExpired").value == null){
                                    if(checkQuizisValidorNot(toDate,toMonth,toYear,toHr,toMin)){
                                        isExpired  = false
                                    }
                                    else{
                                        isExpired = true
                                    }
                                }
                                else{
                                    isExpired  =child.child("Details").child("isExpired").value as Boolean
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
                                displayName = binding.quizDisplayNameEditText.text.toString()
                                database.child(quizCode).child(androidId).child("DisplayName").setValue(displayName)
                                database.child(quizCode).child(androidId).child("Score").setValue("0")
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
                                displayName = binding.quizDisplayNameEditText.text.toString()
                                database.child(quizCode).child(androidId).child("DisplayName").setValue(displayName)
                                database.child(quizCode).child(androidId).child("Score").setValue("0")
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
    private fun checkQuizisValidorNot(toDate:Long,toMonth:Long,toYear:Long,toHr:Long,toMin:Long):Boolean{
        val c = Calendar.getInstance()
        val curr_year = c.get(Calendar.YEAR)
        val curr_month = c.get(Calendar.MONTH)
        val curr_day = c.get(Calendar.DAY_OF_MONTH)
        val curr_hour = c.get(Calendar.HOUR_OF_DAY)
        val curr_minute = c.get(Calendar.MINUTE)
        Log.e("main","curr_day = $curr_day")
        Log.e("main","$curr_month")
        Log.e("main","curr_month = $curr_year")
        Log.e("main","curr_minute = $curr_minute")
        Log.e("main","curr_hour = $curr_hour")
        Log.e("main","toDate = $toDate")
        Log.e("main","toMonth = $toMonth")
        Log.e("main","toYear = $toYear")
        Log.e("main","toMin = $toMin")
        Log.e("main","toHr = $toHr")
        if (curr_year>toYear){
            return false
        }
        if (curr_year<=toYear.toInt()){
            if (curr_month>toMonth.toInt()){
                return false
            }
            if(curr_month<=toMonth.toInt()){
                if (curr_day>toDate.toInt()){
                    return false
                }
                if (curr_day<=toDate.toInt()){
                    if (curr_day == toDate.toInt()){
                        if (curr_hour>toHr.toInt()){
                            return false
                        }
                        if(curr_hour<=toHr.toInt()){
                            if (curr_minute>toMin.toInt()){
                                return false
                            }
                            if (curr_minute<=toMin.toInt()){
                                return true
                            }
                        }
                    }
                    else{
                        return true
                    }
                }
            }
        }
        return true
    }
}