package com.tgad.savemydinner.findrecipes

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tgad.savemydinner.domain.Recipe

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Recipe>?) {
    val adapter = recyclerView.adapter as RecipeAdapter
    adapter.submitList(data)
}