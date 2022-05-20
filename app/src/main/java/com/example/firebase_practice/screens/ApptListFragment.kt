package com.example.firebase_practice.screens

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
    val apptRef = MakeAppoinment().apptCollectionRef

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApptListBinding.inflate(layoutInflater)
        setUpRecyclerView()

        try {
            RetriveAppDataFromDB()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            Log.d("fireBaseException",e.message.toString())
        }



        binding.addItem.setOnClickListener {
            this.findNavController().navigate(R.id.action_apptListFragment_to_makeAppoinment)
        }
        return binding.root
    }


    private fun RetriveAppDataFromDB() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = apptRef.get().await()
            val tempList = mutableListOf<ItemData>()

            for (doc in querySnapshot.documents) {
                val appt = doc.toObject<ItemData>()
                tempList.add(appt!!)
            }

            myAdapter.getDataChanges(tempList)

        } catch (e: Exception) {
            withContext(Dispatchers.Main){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setUpRecyclerView() {
        binding.revView.adapter = myAdapter
        binding.revView.layoutManager = LinearLayoutManager(requireContext())
    }


}