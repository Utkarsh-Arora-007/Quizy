package com.bitwisor.quizy.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
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
import com.google.android.material.snackbar.Snackbar
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
    var fromflag = false
    var toflag = false
    var quizId = 0
    var quiz_duration=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quizId = (1000..9999).random()
        binding.createQuizBtn.setOnClickListener {
            val unique_code=quizId.toString()

            val bundle = Bundle()
            bundle.putString("uniqueId",unique_code)
            findNavController().navigate(R.id.action_createFragment_to_uniqueCode,bundle)

        }
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
                        val formattedTime: String = when {
                            hourOfDay == 0 -> {
                                if (minute < 10) {
                                    "${hourOfDay + 12}:0${minute} am"
                                } else {
                                    "${hourOfDay + 12}:${minute} am"
                                }
                            }
                            hourOfDay > 12 -> {
                                if (minute < 10) {
                                    "${hourOfDay - 12}:0${minute} pm"
                                } else {
                                    "${hourOfDay - 12}:${minute} pm"
                                }
                            }
                            hourOfDay == 12 -> {
                                if (minute < 10) {
                                    "${hourOfDay}:0${minute} pm"
                                } else {
                                    "${hourOfDay}:${minute} pm"
                                }
                            }
                            else -> {
                                if (minute < 10) {
                                    "${hourOfDay}:${minute} am"
                                } else {
                                    "${hourOfDay}:${minute} am"
                                }
                            }
                        }
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
                        val formattedTime: String = "${hourOfDay}:${minute} "
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
//        binding.createQuizBtn.setOnClickListener {
//            if (fromflag){
//                if (toflag){
//
//                }
//                else{
//                    Toast.makeText(requireContext(),"Please Enter To Date",Toast.LENGTH_SHORT).show()
//                }
//            }
//            else{
//                Toast.makeText(requireContext(),"Please Enter From Date",Toast.LENGTH_SHORT).show()
//            }
//        }

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
            getFromDate(year,month+1,date)
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

}