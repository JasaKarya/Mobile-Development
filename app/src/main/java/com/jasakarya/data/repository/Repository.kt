package com.jasakarya.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.jasakarya.data.model.Cart
import com.jasakarya.data.model.Content
import com.jasakarya.data.model.Talent
import com.jasakarya.data.model.Transaction
import com.jasakarya.data.model.User
import com.jasakarya.data.source.remote.ApiServices

class Repository (private val apiServices: ApiServices, private val context: Context) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    val firebaseUserLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()

    val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val biodataLiveData: MutableLiveData<Biodata?> = MutableLiveData()

    val talentsLiveData = MutableLiveData<List<Content>?>()

    val contentsLiveData = MutableLiveData<List<Content>?>()

    val preferenceCreatedLiveData = MutableLiveData<Boolean>()

    val userHasPreferencesLiveData = MutableLiveData<Boolean>()

    val talentLiveData = MutableLiveData<Talent?>()

    val cartPushSuccesss = MutableLiveData<Boolean>()

    val cartsLiveData = MutableLiveData<List<Cart>>()

    val cartDeleteSuccessLiveData = MutableLiveData<Boolean>()

    val userLiveData = MutableLiveData<User?>()

    val updatePreferredCategoriesStatus = MutableLiveData<Boolean>()

    val addTransactionSuccessLiveData = MutableLiveData<Boolean>()

    val transactionsLiveData = MutableLiveData<List<Transaction>>()

    val transactionLiveData = MutableLiveData<Transaction?>()




    init{
        database = Firebase.database.reference
        if(firebaseAuth.currentUser != null){
            firebaseUserLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
        }
    }

    fun register(user: User, textPassword: String) {
        database = Firebase.database.reference
        firebaseAuth.createUserWithEmailAndPassword(user.email, textPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserLiveData.postValue(firebaseAuth.currentUser)
                    Log.d("FirebaseRegister", "register: ${firebaseAuth.currentUser?.uid}")
                } else {
                    //post value error
                    firebaseUserLiveData.postValue(null)
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()

                }
            }
        database.child("users").push().setValue(user)
            .addOnSuccessListener { firebaseUserLiveData.postValue(firebaseAuth.currentUser)  }
            .addOnFailureListener {
                Log.d("FirebaseRegister", "register: ${it.message}")
                firebaseUserLiveData.postValue(null) }
    }


    suspend fun login (email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseUserLiveData.postValue(firebaseAuth.currentUser)
                } else {
                    firebaseUserLiveData.postValue(null)
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
                        if (it.categories.filterKeys { key -> categories.contains(key) }.any { (_, value) -> value == 1 }
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

    suspend fun fetchAllContentsSortedByRating(limit: Int) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("talents")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allContents = mutableListOf<Content>()
                snapshot.children.forEach { talentSnapshot ->
                    val content = talentSnapshot.child("content").getValue(Content::class.java)
                    content?.let {
                        allContents.add(it)
                    }
                }
                // Sort the contents by rating in descending order
                val sortedContents = allContents.sortedByDescending { it.rating }
                // Take the first 'limit' number of contents
                val limitedContents = sortedContents.take(limit)
                contentsLiveData.postValue(limitedContents)
            }

            override fun onCancelled(error: DatabaseError) {
                contentsLiveData.postValue(null)
            }
        })
    }

    suspend fun searchContentsByQuery(query: String, limit: Int) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("talents")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matchingContents = mutableListOf<Content>()
                snapshot.children.forEach { talentSnapshot ->
                    val content = talentSnapshot.child("content").getValue(Content::class.java)
                    content?.let {
                        // Check if the 'content_name' contains the search query (case-insensitive)
                        if (it.content_name.contains(query, ignoreCase = true)) {
                            matchingContents.add(it)
                        }
                    }
                }
                // Take the first 'limit' number of matching contents
                val limitedContents = matchingContents.take(limit)
                contentsLiveData.postValue(limitedContents)
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

    suspend fun fetchTalentByContentId(contentId: Int) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("talents")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var fetchedTalent: Talent? = null
                for (talentSnapshot in snapshot.children) {
                    val content = talentSnapshot.child("content").getValue(Content::class.java)
                    if (content?.content_id == contentId) {
                        fetchedTalent = talentSnapshot.getValue(Talent::class.java)
                        break // Found the talent, no need to continue loop
                    }
                }
                talentLiveData.postValue(fetchedTalent)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Error", "onCancelled: ${databaseError.message}")
                talentLiveData.postValue(null) // Post null in case of error
            }
        })
    }

    suspend fun addCart(cart: Cart){
        database.child("carts").push().setValue(cart)
            .addOnSuccessListener { cartPushSuccesss.postValue(true) }
            .addOnFailureListener { cartPushSuccesss.postValue(false) }
    }

    suspend fun fetchCartsByUserEmail(userEmail: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("carts")
        databaseReference.orderByChild("userEmail").equalTo(userEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val carts = dataSnapshot.children.mapNotNull { it.getValue(Cart::class.java) }
                    cartsLiveData.postValue(carts)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    cartsLiveData.postValue(emptyList())
                }
            })
    }

    suspend fun deleteCartByCartId(cartId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("carts")

        databaseReference.orderByChild("cartId").equalTo(cartId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (cartSnapshot in snapshot.children) {
                            cartSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    cartDeleteSuccessLiveData.postValue(true)
                                }
                                .addOnFailureListener {
                                    cartDeleteSuccessLiveData.postValue(false)
                                }
                        }
                    } else {
                        // No matching cart found
                        cartDeleteSuccessLiveData.postValue(false)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    cartDeleteSuccessLiveData.postValue(false)
                }
            })
    }

    fun fetchUserByEmail(userEmail: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.orderByChild("email").equalTo(userEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.children.first().getValue(User::class.java)
                        userLiveData.postValue(user)
                    } else {
                        userLiveData.postValue(null)

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    userLiveData.postValue(null)
                }
            })
    }
    fun updatePreferredCategories(userEmail: String, categories: List<String>) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.orderByChild("email").equalTo(userEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userKey = snapshot.children.first().key
                        userKey?.let {
                            databaseReference.child(it).child("preferredCategories").setValue(categories)
                                .addOnSuccessListener { updatePreferredCategoriesStatus.postValue(true) }
                                .addOnFailureListener { updatePreferredCategoriesStatus.postValue(false) }
                        }
                    } else {
                        updatePreferredCategoriesStatus.postValue(false)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    updatePreferredCategoriesStatus.postValue(false)
                }
            })
    }



    fun checkIfUserPreferred(userEmail: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.orderByChild("email").equalTo(userEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.children.first().getValue(User::class.java)
                        userHasPreferencesLiveData.postValue(user?.preferredCategories?.isNotEmpty() == true)
                    } else {
                        userHasPreferencesLiveData.postValue(false)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    userHasPreferencesLiveData.postValue(false)
                }
            })
    }

    suspend fun createTransaction(transaction: Transaction) {
        database.child("transactions").push().setValue(transaction)
            .addOnSuccessListener { addTransactionSuccessLiveData.postValue(true) }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                addTransactionSuccessLiveData.postValue(false) }
    }

    suspend fun fetchTransactionsByUserEmail(userEmail: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("transactions")
        databaseReference.orderByChild("useremail").equalTo(userEmail)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val transactions = dataSnapshot.children.mapNotNull { it.getValue(Transaction::class.java) }
                    transactionsLiveData.postValue(transactions)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    transactionsLiveData.postValue(emptyList())
                }
            })
    }

    suspend fun fetchTransactionByTransactionId(transactionId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("transactions")
        databaseReference.orderByChild("transactionId").equalTo(transactionId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {

                        val transaction = dataSnapshot.children.first().getValue(Transaction::class.java)
                        transactionLiveData.postValue(transaction)
                    } else {

                        transactionLiveData.postValue(null)
                        Toast.makeText(context, "Transaksi tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    transactionLiveData.postValue(null)
                }
            })
    }




}