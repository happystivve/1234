package com.example.pogoda_volerki

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.birjuvachhani.locus.Locus
import com.bumptech.glide.Glide
import com.example.pogoda_volerki.CityListActivity
import kotlinx.android.synthetic.main.activity_edit_city.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.io.StringReader
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private var counter =0
    private var ready =false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        selectCity.setOnClickListener {
            // при клике переходим на форму выбора города
            startActivity( Intent(this, CityListActivity::class.java) )
        }



        val cityName = intent.getStringExtra("city_name")
        if (cityName==null){

        object : CountDownTimer(5000,3500){
            override fun onTick(millisUntilFinished: Long) {
                // если данные получены и прошло 3 сек, то скрываем splash screen и останавливаем счетчик
                counter++
                if(counter>1 && ready){
                    splash_screen.isVisible = false
                    this.cancel()
                }
            }

            override fun onFinish(){
                splash_screen.isVisible = false
            }
        }.start()

            Locus.getCurrentLocation(this) { result ->
                result.location?.let {
                    //tv.text = "${it.latitude}, ${it.longitude}"

                    getWheather(it.longitude, it.latitude)

                } ?: run {
                    tv.text = result.error?.message
                }
            }}
        else{
            splash_screen.isVisible=false
            getWheather(cityName)
        }
    }

fun getWheather(city: String){
    val token = "d4c9eea0d00fb43230b479793d6aa78f"
    val url = "https://api.openweathermap.org/data/2.5/forecast?q=$city&units=metric&appid=${token}"
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {

        override fun onFailure(call: Call, e: IOException) {
            //setText( e.toString() )
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // так можно достать заголовки http-ответа
                //for ((name, value) in response.headers) {
                //  println("$name: $value")
                //}

                //строку преобразуем в JSON-объект
                var jsonObj = JSONObject(response.body!!.string())


                // обращение к визуальному объекту из потока может вызвать исключение
                // нужно присвоение делать в UI-потоке
                setText( jsonObj )
            }
        }
    })

}

    fun getWheather(lon: Double, lat: Double) {
        val token = "d4c9eea0d00fb43230b479793d6aa78f"
        val url = "https://api.openweathermap.org/data/2.5/forecast?lat=${lat}&lon=${lon}&units=metric&appid=${token}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                //setText( e.toString() )
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    // так можно достать заголовки http-ответа
                    //for ((name, value) in response.headers) {
                    //  println("$name: $value")
                    //}

                    //строку преобразуем в JSON-объект
                    var jsonObj = JSONObject(response.body!!.string())


                    // обращение к визуальному объекту из потока может вызвать исключение
                    // нужно присвоение делать в UI-потоке
                    setText( jsonObj )
                }
            }
        })
    }

    fun setText(t: JSONObject){
        runOnUiThread {

            val city1= t.getJSONObject("city").getString("name")
            city2.text=city1+" City"

            val list = t.getJSONArray("list")


            var firstItem = true

            for(i in 0..list.length()-1){

                var main = list.getJSONObject(i).getJSONObject("main").getDouble("temp").roundToInt().toString()
                val list2 = list.getJSONObject(i).getJSONArray("weather")
                val data = list.getJSONObject(i).getString("dt_txt")

                val icoName = list2.getJSONObject(0).getString("icon")
                val icoUrl = "https://openweathermap.org/img/w/${icoName}.png"
                val weather1 = list2.getJSONObject(0).getString("main")

                if(firstItem){
                    firstItem=false
                    main.toFloat()
                    main1.text=main+" ℃"
                    weather2.text=weather1
                    data1.text=data.substring(8,10)+'.'+data.substring(5,7)+'.'+data.substring(0,4)
                    Glide.with(this).load( icoUrl ).into( icon )

                }

                val vl = LinearLayout(this)
                vl.orientation = LinearLayout.VERTICAL
                vl.minimumWidth = 170
                scroll.addView(vl)

                val weather = TextView(this)
                weather.text=weather1
                weather.textAlignment= View.TEXT_ALIGNMENT_CENTER
                vl.addView(weather)

                val ico = ImageView(this)
                vl.addView(ico)
                Glide.with(this).load( icoUrl ).into( ico )

                val data1 = TextView(this)
                data1.text=data.substring(8,10)+'.'+data.substring(5,7)
                data1.textAlignment= View.TEXT_ALIGNMENT_CENTER
                vl.addView(data1)

                val temp = TextView(this)
                temp.text = main+" ℃"
                temp.textAlignment= View.TEXT_ALIGNMENT_CENTER
                vl.addView(temp)

            }
        }
        ready = true
    }
}



