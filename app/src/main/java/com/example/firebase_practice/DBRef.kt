package com.example.firebase_practice

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DBRef {
    val apptCollectionRef = Firebase.firestore.collection("Appointments")
}