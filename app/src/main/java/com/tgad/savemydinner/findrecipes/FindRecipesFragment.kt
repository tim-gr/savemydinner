package com.tgad.savemydinner.findrecipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.tgad.savemydinner.R
import com.tgad.savemydinner.databinding.FragmentFindRecipesBinding
import timber.log.Timber

class FindRecipesFragment : Fragment() {

    private val viewModel: FindRecipesViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, FindRecipesViewModel.Factory(activity.application))
            .get(FindRecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFindRecipesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initIncludeAutocomplete(binding.includeAutocomplete)
        observeIncludedIngredients(binding.includeChips)

        binding.recipeGrid.adapter = RecipeAdapter(RecipeAdapter.RecipeClickListener {
            Timber.i("Recipe was clicked: %s", it.title)
        })

        binding.searchIngredientsButton.setOnClickListener {
            viewModel.searchForRecipes()
        }

        return binding.root
    }

    private fun initIncludeAutocomplete(includeAutocomplete: AutoCompleteTextView) {
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1,
            viewModel.getAvailableIngredients()
        )
        includeAutocomplete.setAdapter(adapter)
        includeAutocomplete.setOnItemClickListener { _, _, position, _ ->
            val newIngredient = adapter.getItem(position).toString()
            viewModel.addIncludedIngredient(newIngredient)
            includeAutocomplete.setText("")
        }
    }

    private fun observeIncludedIngredients(includeChips: ChipGroup) {
        viewModel.includedIngredients.observe(viewLifecycleOwner) {
            includeChips.removeAllViews()
            for (ingredient in it) {
                addChip(includeChips, ingredient)
            }
        }
    }

    private fun addChip(chipGroup: ChipGroup, ingredient: String) {
        val chipInflater = LayoutInflater.from(chipGroup.context)
        val chip = chipInflater.inflate(R.layout.chip_ingredient, chipGroup, false) as Chip
        chip.text = ingredient
        chip.tag = ingredient
        chip.setOnCloseIconClickListener {
            viewModel.removeIngredient(chip.text as String)
        }
        chipGroup.addView(chip)
    }

}