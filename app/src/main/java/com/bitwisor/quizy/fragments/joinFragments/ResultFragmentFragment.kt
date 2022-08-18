package com.bitwisor.quizy.fragments.joinFragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bitwisor.quizy.MainActivity
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentResultFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ResultFragmentFragment : Fragment() {

    lateinit var binding:FragmentResultFragmentBinding
    var quizName=""
    var numberOfQuestion:String=""
    var displayName =""
    var score:String =""
    var quizCode=""
    var androidId=""
    var STORAGE_CODE = 1001
    lateinit var pieChart:PieChart
    lateinit var outOfScore:String
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
        var totalScore:Int
        pieChart=binding.piechart
        androidId= Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        if (bundle!=null){
            quizCode = bundle.getString("QuizCode","None")
        }
        binding.resultsBackbtn.setOnClickListener { 
            findNavController().popBackStack()
        }
        binding.goToHomeBtn.setOnClickListener {
            var i = Intent(requireActivity(), MainActivity::class.java)
            startActivity(i)
            requireActivity().finishAffinity()
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

                                        totalScore=numberOfQuestion.toInt()*10

                                        outOfScore="$score/$totalScore"

                                        binding.resultQuizNametxtview.text = quizName
                                        binding.resultQuizNoQuestiontxtview.text = numberOfQuestion
                                        binding.resultQuizDurationview.text = displayName
                                        binding.resultQuizAvailtxtview.text = outOfScore


                                        binding.constraintLayout.setOnClickListener {
                                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                                            {
                                                if(requireContext()!!.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                                    val permission= arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                    requestPermissions(permission,STORAGE_CODE)

                                                }else{
                                                    savePDF()
                                                }
                                            }
                                            else
                                            {
                                                savePDF()
                                            }
                                        }


                                        pieChart.addPieSlice(
                                            PieModel(
                                                "CorrectScore", score.toFloat(), resources.getColor(android.R.color.holo_green_light)
                                            )
                                        )
                                        val incorrectScore=((numberOfQuestion.toInt()*10)-score.toInt())
                                        pieChart.addPieSlice(
                                            PieModel(
                                                "incorrectScore",
                                                incorrectScore.toFloat(),
                                                resources.getColor(android.R.color.holo_red_dark)
                                            )
                                        )

                                        pieChart.startAnimation();
                                        
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

    private fun savePDF() {
        val mDoc = Document()
        val mFileName = SimpleDateFormat("yyMMdd_HHmmss", Locale.getDefault())
            .format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + mFileName + ".pdf"
        Log.e("PATH",mFilePath.toString())

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.addTitle("Quiz App")
            mDoc.addAuthor("Quizy App")
            mDoc.add(Paragraph("Your Score Card"))
            mDoc.add(Paragraph("Quiz Name : ${quizName} QuizID : ${quizCode}"))
            mDoc.add(Paragraph("Quiz taker Name : ${displayName}"))
            mDoc.add(Paragraph("Score : ${score}"))
            mDoc.close()
            Toast.makeText(requireContext(),"${mFileName}.pdf \n is created to \n ${mFilePath}",
                Toast.LENGTH_SHORT).show()

        }
        catch(e:Exception){
            Log.e("Exception",e.toString())
            Toast.makeText(requireContext(), ""+e.toString(), Toast.LENGTH_SHORT).show()
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_CODE->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    savePDF()
                }
                else{
                    Toast.makeText(requireContext(),"Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}