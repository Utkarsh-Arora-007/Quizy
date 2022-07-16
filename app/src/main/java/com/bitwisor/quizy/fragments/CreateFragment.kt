package com.bitwisor.quizy.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentCreateBinding
import com.bitwisor.quizy.utils.QuizInfoOfUser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class CreateFragment : Fragment() {

    lateinit var binding: FragmentCreateBinding
    lateinit var fromdatePickerDialog:DatePickerDialog
    lateinit var todatePickerDialog:DatePickerDialog
    var fromYear =0
    var fromMonth =0
    var fromDay =0
    var toYear =0
    var toMonth =0
    var toDay =0
    var fromHr=0
    var fromMin =0
    var toHr = 0
    var toMin = 0
    var fromflag = false
    var toflag = false
    var quizId = 0
    var quiz_duration=0
    var numberOfQuestions=0
    lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        quizId = RandomUnrepeated(1000,9999).nextInt()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        binding.createProgresscircle.visibility = View.VISIBLE
        FirebaseDatabase.getInstance().reference
            .child("UserProfiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("MyQuiz")
            .child(quizId.toString())
            .child("Questions")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.createProgresscircle.visibility = View.GONE
                    if (snapshot!=null){
                        numberOfQuestions = snapshot.childrenCount.toInt()

                    }
                    else{
                        numberOfQuestions = 0
                    }
                    binding.numberOfQuestionsAdded.text = "Count : $numberOfQuestions"
                }

                override fun onCancelled(error: DatabaseError) {
                    Snackbar.make(view,"Database Error",Snackbar.LENGTH_SHORT).show()
                }

            })
        binding.createBackbtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.fromdatebtn.setOnClickListener {
            initFromDatePicker()
            fromdatePickerDialog.show()
            fromflag = true
        }
        binding.todatebtn.setOnClickListener {
            initToDatePicker()
            todatePickerDialog.show()
            toflag = true
        }
        binding.fromTimeBtn.setOnClickListener {
             val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
                object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                        // logic to properly handle
                        // the picked timings by user
                        var formattedTime: String =""
                        if (minute < 10 && hourOfDay <10) {
                            formattedTime = "0${hourOfDay}:0${minute}"
                        } else if(minute<10 && hourOfDay >=10) {
                            formattedTime = "${hourOfDay}:0${minute} "
                        }
                        else if (minute>=10 && hourOfDay<10){
                            formattedTime = "0${hourOfDay}:${minute}"

                        }
                        else{
                            formattedTime = "${hourOfDay}:${minute}"
                        }
                        fromHr = hourOfDay
                        fromMin = minute
                        binding.fromTimetxt.text = formattedTime
                    }
                }

            val timePicker: TimePickerDialog = TimePickerDialog(
                // pass the Context
                requireContext(),
                // listener to perform task
                // when time is picked
                timePickerDialogListener,
                // default hour when the time picker
                // dialog is opened
                12,
                // default minute when the time picker
                // dialog is opened
                10,
                // 24 hours time picker is
                // false (varies according to the region)
                true
            )

            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()
        }

        binding.toTimeBtn.setOnClickListener {
             val timePickerDialogListener: TimePickerDialog.OnTimeSetListener =
                object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

                        // logic to properly handle
                        // the picked timings by user
                        var formattedTime: String =""
                        if (minute < 10 && hourOfDay <10) {
                            formattedTime = "0${hourOfDay}:0${minute}"
                        } else if(minute<10 && hourOfDay >=10) {
                            formattedTime = "${hourOfDay}:0${minute} "
                        }
                        else if (minute>=10 && hourOfDay<10){
                            formattedTime = "0${hourOfDay}:${minute}"

                        }
                        else{
                            formattedTime = "${hourOfDay}:${minute}"
                        }
                        toHr = hourOfDay
                        toMin = minute
                        binding.toTimetxt.text = formattedTime
                    }
                }
            val timePicker: TimePickerDialog = TimePickerDialog(
                // pass the Context
                requireContext(),
                // listener to perform task
                // when time is picked
                timePickerDialogListener,
                // default hour when the time picker
                // dialog is opened
                12,
                // default minute when the time picker
                // dialog is opened
                10,
                // 24 hours time picker is
                // false (varies according to the region)
                true
            )

            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()
        }

        binding.addQbtn.setOnClickListener {
            var numberOfQuestion = binding.quizNumberofquestionInputEdittext.text.toString()
            if(!numberOfQuestion.isNullOrEmpty()){
                var bundle = Bundle()
                bundle.putInt("NoOfQuestion",binding.quizNumberofquestionInputEdittext.text.toString().toInt())
                bundle.putInt("QuizId",quizId)
                bundle.putString("QuizName",binding.quizNameInputEdittext.text.toString())// Added by Shivendu, to show the quiz name at Add Question Fragment.
                findNavController().navigate(R.id.action_createFragment_to_addQuestionsFragment,bundle)
            }
            else{
                Snackbar.make(view,"Please Enter Number Of Questions",Snackbar.LENGTH_SHORT).show()
            }

        }
        binding.createQuizBtn.setOnClickListener {
            val quizName = binding.quizNameInputEdittext.text.toString()
            val numberOfQ = binding.quizNumberofquestionInputEdittext.text.toString()
            val duration= binding.quizDurationInputEdittext.text.toString()
            if (fromflag){
                if (toflag){
                    if(!quizName.isNullOrEmpty() && !numberOfQ.isNullOrEmpty() && !duration.isNullOrEmpty()){
                        if (checkDuration(duration.toInt())  ){
                            if(fromandToCondition(fromDay,fromMonth,fromYear,toDay,toMonth,toYear,fromHr,fromMin,toHr,toMin)
                               ){
                                binding.createProgresscircle.visibility = View.VISIBLE
                                val quizInfo = QuizInfoOfUser(quizName,quizId.toString(),fromDay,fromMonth,fromYear,toMonth,toDay,toYear,fromHr,fromMin,toHr,toMin,duration,numberOfQuestions.toString(),false)

                                database.child("JoinRooms")
                                    .child(quizId.toString())
                                    .child("Details")
                                    .setValue(quizInfo)
                                database = database.child("UserProfiles")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .child("MyQuiz")
                                    .child(quizId.toString())
                                    .child("Details")
                                database.setValue(quizInfo).addOnCompleteListener {
                                    binding.createProgresscircle.visibility = View.GONE
                                    val unique_code=quizId.toString()
                                    val bundle = Bundle()
                                    bundle.putString("uniqueId",unique_code)
                                    findNavController().navigate(R.id.action_createFragment_to_uniqueCode,bundle)

                                }.addOnFailureListener {
                                    Snackbar.make(view,"Error While creating quiz",Snackbar.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                Snackbar.make(view,"Please enter a Valid From and To Date",Snackbar.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Snackbar.make(view,"Duration must be between 1 and 60 min",Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Snackbar.make(view,"Please fill the required details",Snackbar.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(requireContext(),"Please Enter To Date",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(requireContext(),"Please Enter From Date",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkDuration(dur: Int): Boolean {
        if(dur>=1 && dur <=60){
            return true
        }
        else{
            return false
        }
    }

    private fun initFromDatePicker() {
        var mDate=""
        var dateSetListener:DatePickerDialog.OnDateSetListener = OnDateSetListener { datePicker, year, month, date ->
            getFromDate(year,month+1,date)
            mDate = makeStringFormat(year,month+1,date)
            binding.fromDatetxt.text = mDate
        }
        var style = AlertDialog.THEME_HOLO_LIGHT
        val cal:Calendar = Calendar.getInstance()
        val mdate = cal.get(Calendar.DATE)
        val mmonth = cal.get(Calendar.MONTH)
        val myear = cal.get(Calendar.YEAR)
        fromdatePickerDialog = DatePickerDialog(requireContext(),style,dateSetListener,myear,mmonth,mdate)
    }
    private fun initToDatePicker() {
        var mDate=""
        var dateSetListener:DatePickerDialog.OnDateSetListener = OnDateSetListener { datePicker, year, month, date ->
            getToDate(year,month+1,date)
            mDate = makeStringFormat(year,month+1,date)
            binding.toDatetxt.text = mDate
        }
        var style = AlertDialog.THEME_HOLO_LIGHT
        val cal:Calendar = Calendar.getInstance()
        val mdate = cal.get(Calendar.DATE)
        val mmonth = cal.get(Calendar.MONTH)
        val myear = cal.get(Calendar.YEAR)
        todatePickerDialog = DatePickerDialog(requireContext(),style,dateSetListener,myear,mmonth,mdate)
    }
    private fun makeStringFormat(year: Int, month: Int, date: Int):String{
        return "${getMonthName(month)} ${date} ${year}"
    }

    private fun getMonthName(month: Int): String {
        if (month == 1)
            return "JAN"
        if (month == 2)
            return "FEB"
        if (month == 3)
            return "MAR"
        if (month == 4)
            return "APR"
        if (month == 5)
            return "MAY"
        if (month == 6)
            return "JUN"
        if (month == 7)
            return "JUL"
        if (month == 8)
            return "AUG"
        if (month == 9)
            return "SPT"
        if (month == 10)
            return "OCT"
        if (month == 11)
            return "NOV"
        if (month == 12)
            return "DEC"
        return "JAN"
    }

    private fun getFromDate(year: Int, month: Int, date: Int){
        fromDay = date
        fromMonth = month
        fromYear = year
    }
    private fun getToDate(year: Int, month: Int, date: Int){
        toDay = date
        toMonth = month
        toYear = year
    }
    class RandomUnrepeated(from: Int, to: Int) {
        private val numbers = (from..to).toMutableList()
        fun nextInt(): Int {
            val index = kotlin.random.Random.nextInt(numbers.size)
            val number = numbers[index]
            numbers.removeAt(index)

            return number
        }
    }
    private fun fromandToCondition(fromDate:Int,fromMonth:Int,fromYear:Int,toDate:Int,toMonth:Int,toYear:Int,fromHr:Int,fromMin:Int,toHr:Int,toMin:Int):Boolean{
        if(fromYear>toYear){
            return false
        }
        if(fromYear<=toYear){
            if (fromMonth>toMonth){
                return false
            }
            if (fromMonth<=toMonth){
                if (fromDate>toDate){
                    return false
                }
                if(fromDate<=toDate){

                    if(fromDate == toDate){
                        if (fromHr>toHr){
                            return false
                        }
                        if(fromHr<=toHr){
                            if(fromMin>toMin){
                                return false
                            }
                            if(fromMin<=toMin){
                                return true
                            }
                        }
                    }else{
                        return true
                    }
                }
            }
        }
        return true
    }
    private fun CurrDateTimeEnteredFromDateTime(fromDate:Int,fromMonth:Int,fromYear:Int,fromHr:Int,fromMin:Int):Boolean{
        val c = Calendar.getInstance()
        val curr_year = c.get(Calendar.YEAR)
        val curr_month = c.get(Calendar.MONTH)
        val curr_day = c.get(Calendar.DAY_OF_MONTH)
        val curr_hour = c.get(Calendar.HOUR_OF_DAY)
        val curr_minute = c.get(Calendar.MINUTE)

        if(fromYear<curr_year){
            return false
        }
        else{
            if(fromYear>=curr_year){
                if (fromMonth<curr_month){
                    return false
                }
                else{
                    if (fromMonth>=curr_month){
                        if (fromDate<curr_day){
                            return false
                        }
                        else{
                            if (fromDate>=curr_day){
                                if (fromHr<curr_hour){
                                    return false
                                }
                                else{
                                    if(fromHr>=curr_hour){
                                        if (fromMin<curr_minute){
                                            return false
                                        }
                                        else{
                                            return true
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }
}