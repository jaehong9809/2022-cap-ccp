package com.example.test_project_1.addrecy

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project_1.R
import com.example.test_project_1.databinding.AddItemBinding
import kotlin.collections.ArrayList

class AddAdapter(val context: Context, val datalist: ArrayList<AddModel>):
    RecyclerView.Adapter<AddAdapter.Holder>() {

    inner class Holder(private val binding: AddItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(addModel: AddModel) {
            if(addModel.add_date % 10 == 0) {
                binding.adddate.text="아침"
            }
            else if(addModel.add_date % 10 == 1) {
                binding.adddate.text="점심"
            }
            else if(addModel.add_date % 10 == 2) {
                binding.adddate.text="저녁"
            }
            else if(addModel.add_date % 10 == 3) {
                binding.adddate.text="간식"
            }
            else {
                binding.adddate.text="야식"
            }

            binding.addname.text=addModel.add_name
            binding.addweight.text=addModel.add_weight.toString()
            binding.addunit.text=addModel.add_unit
            binding.addkcal.text=addModel.add_kcal.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding=AddItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun getItemCount(): Int =datalist.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(datalist[position])
    }

    // 뷰 갱신
    fun setData() {
        notifyDataSetChanged()
    }
}