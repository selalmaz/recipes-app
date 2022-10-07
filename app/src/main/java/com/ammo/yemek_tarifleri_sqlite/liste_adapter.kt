package com.ammo.yemek_tarifleri_sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ammo.yemek_tarifleri_sqlite.databinding.RecyclerViewBinding

class liste_adapter(val yemekListesi: ArrayList<String>,val idListesi : ArrayList<Int>) : RecyclerView.Adapter<liste_adapter.YemekHolder>() {


    class YemekHolder(val recyclerViewBinding: RecyclerViewBinding ) :RecyclerView.ViewHolder(recyclerViewBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_view,parent,false)
        val recyclerViewBinding =RecyclerViewBinding.inflate(inflater,parent,false)
        return YemekHolder(recyclerViewBinding)


    }

    override fun onBindViewHolder(holder: YemekHolder, position: Int) {

        holder.recyclerViewBinding.recylerViewText.text=yemekListesi[position]

        holder.recyclerViewBinding.recylerViewText.setOnClickListener{

        val action =ListeFragmentDirections.actionListeFragmentToTarifFragment(idListesi[position],1)
            Navigation.findNavController(it).navigate(action)  // yazıya tıklandıgında detay sayffasına gecr

        }

    }

    override fun getItemCount(): Int {

        return yemekListesi.size
    }
}