package com.appdem.studentsFeature

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdem.databinding.ItemStudentRecViewBinding

class StudentAdapter(private var students: List<StudentMock>) :
    RecyclerView.Adapter<StudentAdapter.ItemViewHolder>() {

    private lateinit var binding: ItemStudentRecViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = ItemStudentRecViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = students[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = students.size

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filterList: ArrayList<StudentMock>) {
        students = filterList
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(private val binding: ItemStudentRecViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, binding.student.toString())
                itemView.context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }

        fun bind(student: StudentMock) {
            binding.student = student
        }
    }
}
