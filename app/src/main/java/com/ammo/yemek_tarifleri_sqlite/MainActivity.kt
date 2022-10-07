package com.ammo.yemek_tarifleri_sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.Navigation
import com.ammo.yemek_tarifleri_sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.menu_bar,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.item_ekle)
        {
            binding.bilgiText.visibility=View.INVISIBLE
            val action=ListeFragmentDirections.actionListeFragmentToTarifFragment(0,0)
            Navigation.findNavController(this,R.id.fragmentContainerView).navigate(action)


        }


        return super.onOptionsItemSelected(item)
    }

}