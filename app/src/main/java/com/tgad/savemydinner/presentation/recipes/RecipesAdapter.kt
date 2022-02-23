package com.tgad.savemydinner.presentation.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tgad.savemydinner.databinding.ItemRecipeBinding
import com.tgad.savemydinner.domain.entities.Recipe

class RecipesAdapter(private val onClickListener: RecipeClickListener) :
    ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(DiffCallback) {

    class RecipeViewHolder(private var binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.recipe = recipe
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.recipeId == newItem.recipeId
        }
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(ItemRecipeBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(recipe)
        }
        holder.bind(recipe)
    }

    class RecipeClickListener(val clickListener: (recipe: Recipe) -> Unit) {
        fun onClick(recipe: Recipe) = clickListener(recipe)
    }

}