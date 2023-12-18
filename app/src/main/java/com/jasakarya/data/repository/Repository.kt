package com.jasakarya.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.jasakarya.data.model.Biodata
import com.jasakarya.data.model.Content
import com.jasakarya.data.model.User
import com.jasakarya.data.source.remote.ApiServices

class Repository (private val apiServices: ApiServices, private val context: Context) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()

    val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val biodataLiveData: MutableLiveData<Biodata?> = MutableLiveData()

    val talentsLiveData = MutableLiveData<List<Content>?>()

    val contentsLiveData = MutableLiveData<List<Content>?>()

    val preferenceCreatedLiveData = MutableLiveData<Boolean>()

    val userHasPreferencesLiveData = MutableLiveData<Boolean>()


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

    suspend fun getBiodata() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val userEmail = firebaseAuth.currentUser?.email ?: return

        databaseReference.orderByChild("email").equalTo(userEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val biodata = userSnapshot.getValue(Biodata::class.java)
                            biodata?.let {
                                biodataLiveData.postValue(it)
                            }
                        }
                    } else {
                        biodataLiveData.postValue(null)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Error", "onCancelled: ${databaseError.message}")
                }
            })
    }

    suspend fun fetchTalents(limit:Int){
        val databaseReference = FirebaseDatabase.getInstance().getReference("talents")
        databaseReference.limitToFirst(limit)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val talents = snapshot.children.mapNotNull { it.getValue(Content::class.java) }
                    talentsLiveData.postValue(talents)
                }

                override fun onCancelled(error: DatabaseError) {
                    talentsLiveData.postValue(null)

                }
            })

    }

    suspend fun fetchContents(limit: Int) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("talents")
        databaseReference.limitToFirst(limit)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contents = mutableListOf<Content>()
                    snapshot.children.forEach { talentSnapshot ->
                        talentSnapshot.child("content").getValue(Content::class.java)?.let {
                            contents.add(it)
                        }
                    }
                    contentsLiveData.postValue(contents)
                }

                override fun onCancelled(error: DatabaseError) {
                    contentsLiveData.postValue(null)
                }
            })
    }

    suspend fun fetchFilteredContents(categories: List<String>, minRating: Double, limit: Int) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("talents")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredContents = mutableListOf<Content>()
                snapshot.children.forEach { talentSnapshot ->
                    val content = talentSnapshot.child("content").getValue(Content::class.java)
                    content?.let {
                        if (it.categories.filterKeys { key -> categories.contains(key) }.all { (_, value) -> value == 1 }
                            && it.rating >= minRating) {
                            filteredContents.add(it)
                        }
                    }
                }
                contentsLiveData.postValue(filteredContents.sortedByDescending { it.rating }.take(limit))
            }

            override fun onCancelled(error: DatabaseError) {
                contentsLiveData.postValue(null)
            }
        })
    }
    fun createUserPreference(userEmail: String, preferences: Map<String, Int>) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val userRef = databaseReference.child("users").orderByChild("email").equalTo(userEmail).limitToFirst(1)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userKey = snapshot.children.first().key
                    userKey?.let {
                        databaseReference.child("users").child(it).child("preference").setValue(preferences)
                            .addOnSuccessListener { preferenceCreatedLiveData.postValue(true) }
                            .addOnFailureListener { preferenceCreatedLiveData.postValue(false) }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                preferenceCreatedLiveData.postValue(false)

            }
        })
    }

    fun checkUserPreference(userEmail: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        val userRef = databaseReference.child("users").orderByChild("email").equalTo(userEmail).limitToFirst(1)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val hasPreferences = snapshot.children.first().child("preference").exists()
                    userHasPreferencesLiveData.postValue(hasPreferences)
                } else {
                    userHasPreferencesLiveData.postValue(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
            }
        })
    }

}