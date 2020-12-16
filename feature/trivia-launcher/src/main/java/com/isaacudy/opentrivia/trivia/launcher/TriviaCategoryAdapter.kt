package com.isaacudy.opentrivia.trivia.launcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.isaacudy.opentrivia.createDefaultDiffUtil
import com.isaacudy.opentrivia.trivia.launcher.data.TriviaCategoryEntity
import com.isaacudy.opentrivia.trivia.launcher.databinding.TriviaLauncherCategoryItemBinding

class TriviaCategoryAdapter(
    private val onItemSelected: (TriviaCategoryEntity) -> Unit
) : ListAdapter<TriviaCategoryEntity, TriviaCategoryAdapter.TriviaCategoryViewHolder>(createDefaultDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriviaCategoryViewHolder {
        return TriviaCategoryViewHolder(
            TriviaLauncherCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TriviaCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TriviaCategoryViewHolder(
        private val binding: TriviaLauncherCategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TriviaCategoryEntity) {
            binding.itemName.text = item.name
            binding.root.setOnClickListener { onItemSelected(item) }
        }
    }
}
