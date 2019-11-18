package com.example.pogoda_volerki

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_city.*

class editCity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_city)

        edit.setOnClickListener{
         if (!editText.text.isEmpty()) {
             val myPreferences = getSharedPreferences("settings", MODE_PRIVATE)

// запрашиваем из хранилища список городов (можно задать значение по-умолчанию)
// андроид не позволяет хранить массивы, поэтому список хранится как строка с разделителями
             val oldCityListString = myPreferences.getString("cityList", "Moscow|Kazan|Yoshkar-Ola")
             val editor = myPreferences.edit()
             try {
                 editor.putString("cityList", oldCityListString+"|"+editText.text.toString() )
             }finally {
                 editor.commit()
             }
                 startActivity( Intent(this, CityListActivity::class.java) )

         }

        }
    }
}
