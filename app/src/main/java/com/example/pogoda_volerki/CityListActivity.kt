package com.example.pogoda_volerki

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.pogoda_volerki.MainActivity
import com.example.pogoda_volerki.R
import kotlinx.android.synthetic.main.activity_city_list.*

class CityListActivity : AppCompatActivity() {


    private var names1 = emptyArray<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list)

        val myPreferences = getSharedPreferences("settings", MODE_PRIVATE)
        val oldCityListString = myPreferences.getString("cityList", "Moscow|Kazan|Yoshkar-Ola")
        names1 = oldCityListString!!.split("|").toTypedArray()

        cityList.adapter = ArrayAdapter(
            this,
            R.layout.city_list_item, names1
        )

        cityList.setOnItemClickListener { parent, view, position, id ->
            val mainIntent = Intent(this, MainActivity::class.java)
            val cityName = names1[id.toInt()]

            // запоминаем выбранное название города
            mainIntent.putExtra("city_name", cityName)

            // возвращаемся на основной экран (Activity)
            startActivity( mainIntent )
        }

        button2.setOnClickListener{
            startActivity( Intent(this, editCity::class.java) )
        }
    }
}
