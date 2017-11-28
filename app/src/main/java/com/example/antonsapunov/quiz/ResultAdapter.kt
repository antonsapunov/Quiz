package com.example.antonsapunov.quiz

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ResultAdapter : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    private var resultArray: MutableList<Result> = mutableListOf()

    fun setResults(results: MutableList<Result>) {
        resultArray = results
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.setInfo(resultArray, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = resultArray.size


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var mPosition: TextView = itemView.findViewById(R.id.number)
        private var mName: TextView = itemView.findViewById(R.id.person)
        private var mResult: TextView = itemView.findViewById(R.id.result)

        fun setInfo(resultArray: MutableList<Result>, position: Int) {
            mPosition.text = (position + 1).toString()
            mName.text = resultArray[position].person
            mResult.text = "${resultArray[position].result}%"
        }

    }
}