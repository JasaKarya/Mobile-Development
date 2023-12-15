package com.jasakarya.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.jasakarya.data.model.User
import com.jasakarya.data.source.remote.ApiServices

class Repository (private val apiServices: ApiServices, private val context: Context) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var database: DatabaseReference


    init{
        if(firebaseAuth.currentUser != null){
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
            database = Firebase.database.reference
        }
    }

    fun register(user: User) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    //post value error
                    userLiveData.postValue(null)
                    Log.d("Register", "register: ${task.exception?.message}")

                }
            }
        database.child("users").push().setValue(user)
            .addOnSuccessListener { userLiveData.postValue(firebaseAuth.currentUser)  }
            .addOnFailureListener { userLiveData.postValue(null) }
    }


    suspend fun login (email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    userLiveData.postValue(null)
                }
            }
    }

    suspend fun logout() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

}