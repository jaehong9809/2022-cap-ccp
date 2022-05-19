package com.example.test_project_1.addrecy

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.test_project_1.databinding.AddItemBinding


class AddAdapter(val context: Context, val datalist: ArrayList<AddModel>):
    RecyclerView.Adapter<AddAdapter.Holder>() {

    inner class Holder(private val binding: AddItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(addModel: AddModel) {

            binding.addname.text=addModel.add_name
            binding.addweight.text=addModel.add_weight
            binding.addkcal.text=addModel.add_kcal.toString()

            val position = adapterPosition
            binding.canclebtn.setOnClickListener {
                delete(position)
                if(datalist.size == 0) {
                    with(datalist){
                        add(AddModel("", 0, "", 0))
                    }
                }
            }
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

    fun delete(positon: Int) {
        datalist.removeAt(positon)
        setData()
    }

    // 뷰 갱신
    fun setData() {
        notifyDataSetChanged()
    }
}