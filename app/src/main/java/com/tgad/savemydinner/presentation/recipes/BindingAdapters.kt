package com.tgad.savemydinner.presentation.recipes

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tgad.savemydinner.R
import com.tgad.savemydinner.domain.entities.Recipe

/**
 * Binds the list data to the recycler view used at the recipes tab.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Recipe>?) {
    val adapter = recyclerView.adapter as RecipesAdapter
    adapter.submitList(data)
}

/**
 * Binds the images of the recipes at the recipes tab.
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}