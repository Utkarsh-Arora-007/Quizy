package com.bitwisor.quizy.fragments.joinFragments

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitwisor.quizy.Adapters.CurrentLeaderBoardAdapter
import com.bitwisor.quizy.Adapters.DetailsPerQuizAdapter
import com.bitwisor.quizy.R
import com.bitwisor.quizy.databinding.FragmentCurrentLeaderboardBinding
import com.bitwisor.quizy.utils.DetailsPerQuiz
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class CurrentLeaderboardFragment : Fragment() {

    lateinit var binding : FragmentCurrentLeaderboardBinding
    var quizCode=""
    private lateinit var adapter: CurrentLeaderBoardAdapter
    private lateinit var detailsArrayList: ArrayList<DetailsPerQuiz>
    private lateinit var userRecyclerView: RecyclerView
    private val STORAGE_CODE = 1001
    var quizId=""
    lateinit var export_pdf: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentLeaderboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bundle = arguments
        if (bundle!=null){
            quizCode = bundle.getString("QuizCode","5154")
        }
        binding.leaderProgresscircle.visibility = View.VISIBLE
        detailsArrayList= arrayListOf<DetailsPerQuiz>()
        export_pdf = binding.exportPdf
        binding.nothingtoshowlottie.visibility = View.GONE
        export_pdf.setOnClickListener {
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

        try{
            FirebaseDatabase.getInstance().getReference("LeaderBoard")
                .child(quizId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot!=null)
                        {
                            for(userSnapshot in snapshot.children){
                                val pname = "${userSnapshot.child("DisplayName").value}"
                                val pscore= "${userSnapshot.child("Score").value}"
                                val op = DetailsPerQuiz(pname,pscore)
                                detailsArrayList.add(op)
                            }
                            if (detailsArrayList.isEmpty()){
                                binding.nothingtoshowlottie.visibility = View.VISIBLE
                            }
                            else{
                                binding.nothingtoshowlottie.visibility = View.GONE
                            }
                            binding.leaderProgresscircle.visibility = View.GONE
                            userRecyclerView.adapter= CurrentLeaderBoardAdapter(detailsArrayList)
                            userRecyclerView.layoutManager= LinearLayoutManager(requireContext())
                            userRecyclerView.setHasFixedSize(true)
                        }
                        else{
                            binding.leaderProgresscircle.visibility = View.GONE
                            binding.nothingtoshowlottie.visibility = View.GONE
                        }

                    }


                    override fun onCancelled(error: DatabaseError) {
                        Snackbar.make(view,"Some Error Occured", Snackbar.LENGTH_SHORT).show()
                    }

                })
        }
        catch (e:java.lang.Exception){
            Toast.makeText(requireContext(),"${e.toString()}", Toast.LENGTH_SHORT).show()
        }

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
            val data = detailsArrayList
            mDoc.addAuthor("Quizy App")
            mDoc.add(Paragraph("LeaderBoard"))
            mDoc.add(Paragraph("Quiz Taker Name - Quiz Taker Score"))
            for (ele in data){
                mDoc.add(Paragraph("${ele.person_name} - ${ele.p_score}"))
            }

            mDoc.close()
            Toast.makeText(requireContext(),"${mFileName}.pdf \n is created to \n ${mFilePath}",Toast.LENGTH_SHORT).show()

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
                    Toast.makeText(requireContext(),"Permission Denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}