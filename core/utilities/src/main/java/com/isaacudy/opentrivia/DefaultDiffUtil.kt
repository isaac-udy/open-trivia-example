package com.isaacudy.opentrivia

import androidx.recyclerview.widget.DiffUtil

// This isn't really great practice, but I'm too lazy to define a proper interface for "identifiable"
fun <T> createDefaultDiffUtil() = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(
        oldItem: T,
        newItem: T
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: T,
        newItem: T
    ): Boolean {
        return oldItem == newItem
    }
}