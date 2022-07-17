package com.bitwisor.quizy.Adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bitwisor.quizy.R
import com.bitwisor.quizy.utils.TotalQuizzesInfo

class TotalQuizzesAdapter(private val QuizDetails:ArrayList<TotalQuizzesInfo>) : RecyclerView.Adapter<TotalQuizzesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.sample_quiz_details,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem=QuizDetails[position]
        holder.quizName.text=currentItem.QuizName
        holder.uniqueCode.text=currentItem.QuizId

        holder.mybox.setOnClickListener {
            var bundle =Bundle()
            bundle.putString("QuizId",currentItem.QuizId)
            holder.mybox.findNavController().navigate(R.id.action_totalQuizzesInfoFragment_to_detailsPerQuizFragment,bundle)
        }

    }

    override fun getItemCount(): Int {
        return QuizDetails.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        val quizName:TextView=itemView.findViewById(R.id.quiz_name)
        val uniqueCode:TextView=itemView.findViewById(R.id.quiz_unique_code)
        val mybox:ConstraintLayout = itemView.findViewById(R.id.mbox)
    }

}