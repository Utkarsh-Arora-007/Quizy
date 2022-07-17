package com.bitwisor.quizy.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bitwisor.quizy.R
import com.bitwisor.quizy.utils.DetailsPerQuiz

class DetailsPerQuizAdapter(private val detailsofQuiz:ArrayList<DetailsPerQuiz>) : RecyclerView.Adapter<DetailsPerQuizAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.sample_leaderboard_per_quiz,parent,false)
        return DetailsPerQuizAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem=detailsofQuiz[position]
        holder.name.text=currentItem.person_name
        holder.score.text=currentItem.p_score
    }

    override fun getItemCount(): Int {
       return detailsofQuiz.size
    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val name:TextView=itemView.findViewById(R.id.person_name)
        val score:TextView= itemView.findViewById(R.id.person_score)

    }
}