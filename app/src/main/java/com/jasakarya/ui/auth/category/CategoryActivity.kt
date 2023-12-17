package com.jasakarya.ui.auth.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.jasakarya.R
import com.jasakarya.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding

    private val selectedCategoryList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            selectedCategoryList.clear()
            if (checkedIds.isEmpty()){
                selectedCategoryList.add("No category selected")
            } else {
                selectedCategoryList.clear()
                checkedIds.forEach{checkedId ->
                    val selectedCategory = findViewById<Chip>(checkedId)
                    addCategory(selectedCategory.text.toString())

                }
            }
        }

        binding.btnHome.setOnClickListener {
            onGetSelectedChipsClick()
        }
    }

    private fun onGetSelectedChipsClick() {
        val selectedChipsText = selectedCategoryList.joinToString(", ")
        showToast("Selected Chip Names: $selectedChipsText")
    }

    private fun addCategory(name: String) {
        if (!selectedCategoryList.contains(name)) {
            selectedCategoryList.add(name)
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}