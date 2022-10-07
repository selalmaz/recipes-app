package com.ammo.yemek_tarifleri_sqlite

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.ammo.yemek_tarifleri_sqlite.databinding.FragmentTarifBinding
import java.io.ByteArrayOutputStream


class TarifFragment : Fragment() {

   var secilen_gorsel : Uri? = null
    var secilen_bitmap : Bitmap? = null

    private var _binding:FragmentTarifBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentTarifBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kaydetButton.setOnClickListener {
            kaydet(it)
        }

        binding.gorselSecImage.setOnClickListener {
        gorselSec(it)
    }


        arguments?.let {
            // arg lar geldimi diye bakıyoruz
            // bu argument icidekiler tamamen yazıya tıklandıgında tarif hakkında bilgi vermek icin yapıldı

            var gelen_bilgi = TarifFragmentArgs.fromBundle(it).bilgi

            if(gelen_bilgi == 0)
            {
                // yeni bir yemek eklemeye geldi
                binding.yemekIsmiEdit.setText("")
                binding.tarifListesiEdit.setText("")
                binding.kaydetButton.visibility=View.VISIBLE
                binding.gorselSecImage.isClickable=true
                binding.yemekIsmiEdit.isEnabled=true
                binding.tarifListesiEdit.isEnabled=true
                binding.switch1.visibility=View.INVISIBLE
                binding.silButton.visibility=View.INVISIBLE



                val gorsel_secme_arka_plani = BitmapFactory.decodeResource(context?.resources,R.drawable.gorsesec)
                binding.gorselSecImage.setImageBitmap(gorsel_secme_arka_plani)

            }
            else{

                binding.switch1.visibility=View.VISIBLE
                binding.kaydetButton.visibility=View.INVISIBLE
                binding.gorselSecImage.isClickable=false
                binding.yemekIsmiEdit.isEnabled=false
                binding.tarifListesiEdit.isEnabled=false

                binding.switch1.setOnCheckedChangeListener{ compoundButton: CompoundButton, isChecked: Boolean ->

                    if(isChecked)
                    {
                        //switch on
                        binding.silButton.visibility=View.VISIBLE
                        binding.kaydetButton.visibility=View.VISIBLE
                        binding.kaydetButton.setText("Güncelle")
                        binding.gorselSecImage.isClickable=true
                        binding.yemekIsmiEdit.isEnabled=true
                        binding.tarifListesiEdit.isEnabled=true

                        val secilenID=TarifFragmentArgs.fromBundle(it).id

                        context?.let{
                            try{
                                val db=it.openOrCreateDatabase("Yemekler",Context.MODE_PRIVATE,null)
                                val cursor = db.rawQuery("SELECT * FROM yemekler WHERE id = ?", arrayOf(secilenID.toString()))
                                val yemek_ismi_index = cursor.getColumnIndex("yemekismi")
                                val malzeme_listesi=cursor.getColumnIndex("yemekmalzemesi")
                                val yemek_gorseli=cursor.getColumnIndex("gorsel")

                                var yeni_yemek_ismi=binding.yemekIsmiEdit.text.toString()
                                var yeni_yemek_malzemesi=binding.tarifListesiEdit.text.toString()

                                binding.kaydetButton.setOnClickListener{   //veri güncelleme




                                    yeni_yemek_ismi=binding.yemekIsmiEdit.text.toString()
                                    yeni_yemek_malzemesi=binding.tarifListesiEdit.text.toString()
                                  //  yeni_yemek_resmi=binding.
                                    println(yeni_yemek_ismi)
                                    db.execSQL("UPDATE Yemekler SET yemekismi = '${yeni_yemek_ismi}'  WHERE id = ?",arrayOf(secilenID.toString()))
                                    db.execSQL("UPDATE Yemekler SET yemekmalzemesi = '${yeni_yemek_malzemesi}'  WHERE id = ?",arrayOf(secilenID.toString()))
                                    cursor.close() // bütün işlem bittiikden sonra kapat

                                    val action=TarifFragmentDirections.actionTarifFragmentToListeFragment() // bu resım falan ekledıkden sonra butona bastıgımız da listeleri göruntulememizi saglar
                                    Navigation.findNavController(view).navigate(action)

                                }

                                binding.silButton.setOnClickListener{

                                    db.execSQL("DELETE FROM Yemekler WHERE id = ?",arrayOf(secilenID.toString())) //id si 5 olanı siler
                                    val action=TarifFragmentDirections.actionTarifFragmentToListeFragment() // bu resım falan ekledıkden sonra butona bastıgımız da listeleri göruntulememizi saglar
                                    Navigation.findNavController(view).navigate(action)


                                }



                            }catch (e: Exception){
                                e.printStackTrace()
                            }



                        }


                    }
                    else{
                        //switch close
                        binding.silButton.visibility=View.INVISIBLE
                        binding.kaydetButton.visibility=View.INVISIBLE
                        binding.gorselSecImage.isClickable=false
                        binding.yemekIsmiEdit.isEnabled=false
                        binding.tarifListesiEdit.isEnabled=false
                    }


                }



                val secilenID=TarifFragmentArgs.fromBundle(it).id

                context?.let{
                    try{
                        val db=it.openOrCreateDatabase("Yemekler",Context.MODE_PRIVATE,null)
                        val cursor = db.rawQuery("SELECT * FROM yemekler WHERE id = ?", arrayOf(secilenID.toString()))
                val yemek_ismi_index = cursor.getColumnIndex("yemekismi")
                        val malzeme_listesi=cursor.getColumnIndex("yemekmalzemesi")
                        val yemek_gorseli=cursor.getColumnIndex("gorsel")


                        while (cursor.moveToNext()){

                            binding.yemekIsmiEdit.setText(cursor.getString(yemek_ismi_index))
                            binding.tarifListesiEdit.setText(cursor.getString(malzeme_listesi))

                            val bytedizi=cursor.getBlob(yemek_gorseli)
                            val bitmap=BitmapFactory.decodeByteArray(bytedizi,0,bytedizi.size)

                            binding.gorselSecImage.setImageBitmap(bitmap)
                        }


                    }catch (e: Exception){
                        e.printStackTrace()
                    }



                }


            }


        }


    }

    fun kaydet(view: View)
    {
        //SQLite 'a kaydetme
        val yemekIsmi=binding.yemekIsmiEdit.text.toString()
        val yemekMalzemesi=binding.tarifListesiEdit.text.toString()

        if(secilen_bitmap != null)
        {
            val kucuk_bitmap=bitmapiKucult(secilen_bitmap!!,600) // resmi kuculktuk

            // bu 3 satır kod bitmapı veriye cevirdi data-baseye eklemek icin
            val outputStream = ByteArrayOutputStream()
            kucuk_bitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byte_dizisi=outputStream.toByteArray()

            try {
                // data base olusturuorz verileri eklenme kısımı
                context?.let {

                    val data_base = it.openOrCreateDatabase("Yemekler",Context.MODE_PRIVATE,null)
                    data_base.execSQL("CREATE TABLE IF NOT EXISTS yemekler (id INTEGER PRIMARY KEY,yemekismi VARCHAR, yemekmalzemesi VARCHAR,gorsel BLOB) ")

                    val sqlString= "INSERT INTO yemekler (yemekismi,yemekmalzemesi,gorsel) VALUES (?,?,?)"
                    val statement= data_base.compileStatement(sqlString)
                    statement.bindString(1,yemekIsmi)
                    statement.bindString(2,yemekMalzemesi)
                    statement.bindBlob(3,byte_dizisi)
                    statement.execute()




                }



            }catch (e:Exception){

                e.printStackTrace()
            }
            val action=TarifFragmentDirections.actionTarifFragmentToListeFragment() // bu resım falan ekledıkden sonra butona bastıgımız da listeleri göruntulememizi saglar
            Navigation.findNavController(view).navigate(action)

        }
    }

    fun gorselSec(view: View)
    {
       //kullanıcan resim almak icin izin almak gerekir
        //izin sadece bir defa sorulacak onceden izin verildiyse bi daha izin istemeye gerek yok



        activity?.let {
            //bu activity varsa var yoksa yok manasında gelir

            if(ContextCompat.checkSelfPermission(it.applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                // izin verilmedi izin istememiz gerekiyor

               requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)


            }

            else{
               // izin zaten verildi,tekrar istemeden galeriye git

                val galeriIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)   // haleriye boyle gidiyoruz
                // action pick almak mesale resim almak icin kullanılır
                // uri bir yerin konumunu belirmek icin gorselin nerde durdugunu belirten yapı

                startActivityForResult(galeriIntent,2)

            }


        }
    }

    // kontrol kısımı

    override fun onRequestPermissionsResult( // istenilen izinlerin sonuclarını degerlendiregimiz fonksiyon
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(requestCode == 1)
        {
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // bu if izin aldıgmız da calısır ve galeriye gideriz
                val galeriIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)   // haleriye boyle gidiyoruz
                startActivityForResult(galeriIntent,2)
            }
        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // galeriye gidilince ne olcagını ele alıyoruz

        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) //  result_ok kullanıcı bişey sectimi secmedimi iptal mi etti diye kontrol ediyoruz data!=null veri dondumu onu kontrol ediyor
        {
            secilen_gorsel = data.data

            try { // app in cokmemesi icin

                context?.let {
                    // context null oldugu icin contexi nulldan cıkarıyoruz


                    if(secilen_gorsel != null)
                    {
                        if(Build.VERSION.SDK_INT >=28) // andorid 10 ve üzerinde ki telefonlar
                        {
                            val source = ImageDecoder.createSource(it.contentResolver,secilen_gorsel!!)
                        secilen_bitmap = ImageDecoder.decodeBitmap(source)
                        binding.gorselSecImage.setImageBitmap(secilen_bitmap)

                        }

                        else
                        {
                            secilen_bitmap = MediaStore.Images.Media.getBitmap(it.contentResolver,secilen_gorsel)
                            binding.gorselSecImage.setImageBitmap(secilen_bitmap)
                        }


                    }
                }


            }catch (e:Exception){
                e.printStackTrace()
            }

        }


        super.onActivityResult(requestCode, resultCode, data)



    }

        fun bitmapiKucult(kullanici_bitmap : Bitmap,maximum_boyut : Int):Bitmap  // resim boyutunu ufaltır buyuk resımler koyuldugunda çökme oluşmasın diye
        {
            var width = kullanici_bitmap.width
            var height = kullanici_bitmap.height

            val bitmap_orani : Double = width.toDouble() / height.toDouble()

            if(bitmap_orani > 1 )
            {
                // gorsel yatay
                width=maximum_boyut
                val kisaltilmis_height=width/bitmap_orani
                height = kisaltilmis_height.toInt()


            }
            else{
                // gorsel dikey
                height=maximum_boyut
                val kisaltimis_width = height*bitmap_orani
                width = kisaltimis_width.toInt()


            }

            return Bitmap.createScaledBitmap(kullanici_bitmap,width,height,true)
        }


    override fun onDestroyView() {
        super.onDestroyView()
    }


}