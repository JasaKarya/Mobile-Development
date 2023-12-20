package com.jasakarya.ui.auth.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.R
import com.jasakarya.databinding.ActivityCategoryBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.home.HomeActivity

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: CategoryViewModel by viewModels {factory}

    var listSelected = mutableListOf<String>()
    var chipsSelected = mutableListOf<Chip>()

    private val selectedCategoryList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val userEmail = firebaseAuth.currentUser?.email.toString()

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
            getCategoryFromChips()
            if(listSelected.isNotEmpty()){
                viewModel.createPreference(userEmail, listSelected)

                viewModel.preferenceCreated.observe(this){
                    if(it){
                        Toast.makeText(this, "Terimakasih sudah mengisi preferensi!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Preferences Failed to Create", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{

                Toast.makeText(this, "Pilih 1-3 Kategori", Toast.LENGTH_SHORT).show()
            }
        }

        //add chip on click listener
        binding.chipCat1.setOnClickListener {
            if(binding.chipCat1.isChecked){
                addChipStack(binding.chipCat1)
                activateChips(chipsSelected)
            }
            else{
                activateChips(chipsSelected)
            }
        }
        binding.chipCat2.setOnClickListener {
            if(binding.chipCat2.isChecked){
                addChipStack(binding.chipCat2)
                activateChips(chipsSelected)
            }
            else{
                activateChips(chipsSelected)
            }
        }
        binding.chipCat3.setOnClickListener {
            if(binding.chipCat3.isChecked){
                addChipStack(binding.chipCat3)
                activateChips(chipsSelected)
            }
            else{
                activateChips(chipsSelected)
            }
        }
        binding.chipCat4.setOnClickListener {
            if(binding.chipCat4.isChecked){
                addChipStack(binding.chipCat4)
                activateChips(chipsSelected)
            }
            else{
                activateChips(chipsSelected)
            }
        }
        binding.chipCat5.setOnClickListener {
            if(binding.chipCat5.isChecked){
                addChipStack(binding.chipCat5)
                activateChips(chipsSelected)
            }
            else{
                activateChips(chipsSelected)
            }
        }
        binding.chipCat6.setOnClickListener {
            if(binding.chipCat6.isChecked){
                addChipStack(binding.chipCat6)
                activateChips(chipsSelected)
            }
            else{
                activateChips(chipsSelected)
            }
        }
        binding.chipCat7.setOnClickListener {
            if(binding.chipCat7.isChecked){
                addChipStack(binding.chipCat7)
                activateChips(chipsSelected)
            }
            else{
                activateChips(chipsSelected)
            }
        }
        binding.chipCat8.setOnClickListener {
            if(binding.chipCat8.isChecked){
                addChipStack(binding.chipCat8)
                activateChips(chipsSelected)
            }
            else{
                activateChips(chipsSelected)
            }
        }

    }

    private fun addChipStack(checkedChip: Chip){
        if(chipsSelected.size < 3){
            chipsSelected.add(checkedChip)
        } else {
            chipsSelected.removeAt(0)
            chipsSelected.add(checkedChip)
        }
    }
    private fun activateChips(listOfChips: MutableList<Chip>){
        binding.chipCat1.isChecked = false
        binding.chipCat2.isChecked = false
        binding.chipCat3.isChecked = false
        binding.chipCat4.isChecked = false
        binding.chipCat5.isChecked = false
        binding.chipCat6.isChecked = false
        binding.chipCat7.isChecked = false
        binding.chipCat8.isChecked = false
        binding.chipCat1.setChipBackgroundColorResource(R.color.white)
        binding.chipCat2.setChipBackgroundColorResource(R.color.white)
        binding.chipCat3.setChipBackgroundColorResource(R.color.white)
        binding.chipCat4.setChipBackgroundColorResource(R.color.white)
        binding.chipCat5.setChipBackgroundColorResource(R.color.white)
        binding.chipCat6.setChipBackgroundColorResource(R.color.white)
        binding.chipCat7.setChipBackgroundColorResource(R.color.white)
        binding.chipCat8.setChipBackgroundColorResource(R.color.white)

        when (listOfChips.size) {
            1 -> {
                listOfChips[0].isChecked = true
                listOfChips[0].setChipBackgroundColorResource(R.color.gold)
            }
            2 -> {
                listOfChips[0].isChecked = true
                listOfChips[1].isChecked = true
                listOfChips[0].setChipBackgroundColorResource(R.color.gold)
                listOfChips[1].setChipBackgroundColorResource(R.color.silver)
            }
            3 -> {
                listOfChips[0].isChecked = true
                listOfChips[1].isChecked = true
                listOfChips[2].isChecked = true
                listOfChips[0].setChipBackgroundColorResource(R.color.gold)
                listOfChips[1].setChipBackgroundColorResource(R.color.silver)
                listOfChips[2].setChipBackgroundColorResource(R.color.bronze)
            }
        }


    }

    private fun getCategoryFromChips(){
        listSelected.clear()
        for(chip in chipsSelected){

        val chipId = chip.id.toString()
            if(chipId.equals(binding.chipCat1.id.toString())){
                listSelected.add("cat_1")
            }
            else if(chipId.equals(binding.chipCat2.id.toString())){
                listSelected.add("cat_2")
            }
            else if(chipId.equals(binding.chipCat3.id.toString())){
                listSelected.add("cat_3")
            }
            else if(chipId.equals(binding.chipCat4.id.toString())){
                listSelected.add("cat_4")
            }
            else if(chipId.equals(binding.chipCat5.id.toString())){
                listSelected.add("cat_5")
            }
            else if(chipId.equals(binding.chipCat6.id.toString())){
                listSelected.add("cat_6")
            }
            else if(chipId.equals(binding.chipCat7.id.toString())){
                listSelected.add("cat_7")
            }
            else if(chipId.equals(binding.chipCat8.id.toString())) {
                listSelected.add("cat_8")
            }
            Log.d("listSelected", listSelected.toString())
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