package com.example.firebase_practice.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.firebase_practice.R
import com.example.firebase_practice.adapter.ItemAdapter
import com.example.firebase_practice.databinding.FragmentMakeAppoinmentBinding
import com.example.firebase_practice.model.ItemData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class MakeAppoinment : Fragment() {

    private lateinit var binding: FragmentMakeAppoinmentBinding
    val apptCollectionRef = Firebase.firestore.collection("Appointments")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeAppoinmentBinding.inflate(layoutInflater)

        binding.addRec.setOnClickListener {
            val dataTime=binding.dataTimeEt.text.toString()
            val name=binding.personName.text.toString()
            val locat=binding.location.text.toString()
            val detail=binding.details.text.toString()

            val apptDetail=ItemData(dataTime,name,locat,detail)

            AddApptToDB(apptDetail)

            this.findNavController().navigate(R.id.action_makeAppoinment_to_apptListFragment)
        }
        return binding.root
    }

    private fun AddApptToDB(appt: ItemData) = CoroutineScope(Dispatchers.IO).launch {
        try {
            apptCollectionRef.add(appt).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Successfully Added....", Toast.LENGTH_LONG)
                    .show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}