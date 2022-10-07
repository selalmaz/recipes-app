package com.ammo.yemek_tarifleri_sqlite

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ammo.yemek_tarifleri_sqlite.databinding.FragmentListeBinding
import com.ammo.yemek_tarifleri_sqlite.databinding.FragmentTarifBinding
import java.lang.Exception

class ListeFragment : Fragment() {

    private var _binding:FragmentListeBinding?=null
    private val binding get()=_binding!!

    var yemek_ismi_listesi=ArrayList<String>()
    var yemek_id_listesi=ArrayList<Int>()
    private  lateinit var  listeAdapter : liste_adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentListeBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listeAdapter= liste_adapter(yemek_ismi_listesi,yemek_id_listesi)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = listeAdapter

        sql_veri_alma()

    }



    fun sql_veri_alma()
    {
        try {

            activity?.let {

                val data_base=it.openOrCreateDatabase("Yemekler",Context.MODE_PRIVATE,null)

                val cursor=data_base.rawQuery("SELECT * FROM yemekler", null)
                val yemekIsmiIndex=cursor.getColumnIndex("yemekismi")
                val yemekIdIndex=cursor.getColumnIndex("id")

                 yemek_ismi_listesi.clear()
                yemek_id_listesi.clear()


                while(cursor.moveToNext()){

                    yemek_ismi_listesi.add(cursor.getString(yemekIsmiIndex))
                    yemek_id_listesi.add(cursor.getInt(yemekIdIndex))

                }

                listeAdapter.notifyDataSetChanged() //veriyi günceller rcyler view guncellenir

                cursor.close()


            }




        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}