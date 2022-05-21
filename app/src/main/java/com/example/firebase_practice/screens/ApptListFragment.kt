package com.example.firebase_practice.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase_practice.DBRef
import com.example.firebase_practice.R
import com.example.firebase_practice.adapter.ItemAdapter
import com.example.firebase_practice.databinding.FragmentApptListBinding
import com.example.firebase_practice.model.ItemData
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ApptListFragment : Fragment() {

    private lateinit var binding: FragmentApptListBinding
    val myAdapter by lazy { ItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApptListBinding.inflate(layoutInflater)
        setUpRecyclerView()

        RetriveAppDataFromDB()

        binding.addItem.setOnClickListener {
            this.findNavController().navigate(R.id.action_apptListFragment_to_makeAppoinment)
        }
        return binding.root
    }


    private fun RetriveAppDataFromDB() = CoroutineScope(Dispatchers.Main).launch {
        try {

            val querySnapshot = DBRef.apptCollectionRef.get().await()
            if (querySnapshot.documents != null) {
                val tempList = mutableListOf<ItemData>()

                for (doc in querySnapshot.documents) {
                    val appt = doc.toObject<ItemData>()
                    tempList.add(appt!!)
                }

                myAdapter.getDataChanges(tempList)
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "List is Empty...", Toast.LENGTH_LONG).show()
                }
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "${e.message} checking....", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setUpRecyclerView() {
        binding.revView.adapter = myAdapter
        binding.revView.layoutManager = LinearLayoutManager(requireContext())
    }


}