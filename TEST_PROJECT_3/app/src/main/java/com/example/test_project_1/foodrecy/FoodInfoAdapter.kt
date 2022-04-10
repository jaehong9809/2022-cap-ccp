package com.example.test_project_1.foodrecy

import android.content.Context
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project_1.Food
import com.example.test_project_1.FoodService
import com.example.test_project_1.R
import com.example.test_project_1.Retro
import com.example.test_project_1.addrecy.AddModel
import com.example.test_project_1.databinding.FoodItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class FoodInfoAdapter(val context: Context, val datalist: ArrayList<FoodModel>, val textId: String, val time: String):
    RecyclerView.Adapter<FoodInfoAdapter.Holder>() {

    inner class Holder(private val binding: FoodItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(foodModel: FoodModel) {
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

            binding.deletebtn.setOnClickListener {
                var retro = Retro()
                var retrofit = retro.retrofit
                var deleteFood = retrofit.create(DeleteFood::class.java)

                deleteFood.deleteFood(time, textId, foodModel.food_name).enqueue(object: Callback<Food> {
                    override fun onResponse(call: Call<Food>, response: Response<Food>) {
                        var food = response.body() as Food
                        if(food.code == "0000"){
                            println("success")
                        }
                    }

                    override fun onFailure(call: Call<Food>, t: Throwable) {
                        println("failed")
                    }

                })

            }
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