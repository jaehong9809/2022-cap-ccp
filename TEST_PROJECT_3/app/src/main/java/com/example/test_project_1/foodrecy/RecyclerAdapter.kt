package com.example.test_project_1.foodrecy

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project_1.R
import com.example.test_project_1.databinding.FoodItemBinding
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAdapter(val context: Context, val datalist: ArrayList<Foodmodel>):
    RecyclerView.Adapter<RecyclerAdapter.Holder>() {

    inner class Holder(private val binding: FoodItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(foodModel: Foodmodel) {
            if (foodModel.food_img != "") {
                val resourceId = context.resources.getIdentifier(foodModel.food_img, "drawable", context.packageName)
                binding.foodImg.setImageResource(resourceId)
            } else {
                binding.foodImg.setImageResource(R.mipmap.ic_launcher)
            }

            if(foodModel.food_time % 10 == 0) {
                binding.fooddate.text="아침"
            }
            else if(foodModel.food_time % 10 == 1) {
                binding.fooddate.text="점심"
            }
            else if(foodModel.food_time % 10 == 2) {
                binding.fooddate.text="저녁"
            }
            else if(foodModel.food_time % 10 == 3) {
                binding.fooddate.text="간식"
            }
            else {
                binding.fooddate.text="야식"
            }

            binding.foodNameinfo.text=foodModel.food_name
            binding.foodKcalinfo.text=foodModel.food_kcal.toString()
            binding.foodCarbinfo.text=foodModel.food_carb.toString()
            binding.foodProtinfo.text=foodModel.food_prot.toString()
            binding.foodFatinfo.text=foodModel.food_fat.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding=FoodItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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