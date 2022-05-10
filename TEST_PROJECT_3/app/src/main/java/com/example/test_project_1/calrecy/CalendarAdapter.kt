package com.example.test_project_1.calrecy

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project_1.R
import com.example.test_project_1.databinding.CalendarItemBinding


class CalendarAdapter(private val listener: (calendarDateModel: CalendarDateModel, position: Int) -> Unit) :
     RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {
    private val list = ArrayList<CalendarDateModel>()

    inner class MyViewHolder(private val binding: CalendarItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(calendarDateModel: CalendarDateModel) {
            if (calendarDateModel.isSelected) {
                binding.calWeek.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
                binding.calDay.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
                binding.cardCalendar.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.red
                    )
                )

                binding.calDay.getText()
            } else {
                binding.calWeek.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.black
                    )
                )
                binding.calDay.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.black
                    )
                )
                binding.cardCalendar.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
            }

            binding.calWeek.text = calendarDateModel.cal_week
            binding.calDay.text = calendarDateModel.cal_day
            binding.cardCalendar.setOnClickListener {
                listener.invoke(calendarDateModel, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CalendarItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(calendarList: ArrayList<CalendarDateModel>) {
        list.clear()
        list.addAll(calendarList)
        notifyDataSetChanged()
    }

    fun getDate(calday: TextView, calendarDateModel: CalendarDateModel): Int {
        var datenum: Int = 0
        if (calendarDateModel.isSelected) {
            datenum = calday.getText().toString().toInt()
        }
        return datenum
    }
}