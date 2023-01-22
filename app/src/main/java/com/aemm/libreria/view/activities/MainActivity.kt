package com.aemm.libreria.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.aemm.libreria.R
import com.aemm.libreria.databinding.ActivityMainBinding
import com.aemm.libreria.model.Book
import com.aemm.libreria.network.BooksApi
import com.aemm.libreria.network.RetrofitService
import com.aemm.libreria.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {



            val call =
                RetrofitService.getRetrofit().create(BooksApi::class.java)
                    .getBooks("books") //En el servidor serverbpw y en localhost
            //Constants.getRetrofit().create(GamesApi::class.java).getGames("games/games_list")  //Con Apiary

            call.enqueue(object : Callback<ArrayList<Book>> {
                override fun onResponse(
                    call: Call<ArrayList<Book>>,
                    response: Response<ArrayList<Book>>
                ) {
                    Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.toString()}")
                    Log.d(Constants.LOGTAG, "Datos: ${response.body().toString()}")

                    val gameTmp: Book
                    for (book in response.body()!!) {
                        Toast.makeText(
                            this@MainActivity,
                            "Juego: ${book.title}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    Log.d(Constants.LOGTAG, "Hilo en el onResponse: ${Thread.currentThread().name}")

                    /*
                    binding.pbConexion.visibility = View.GONE
                    binding.rvMenu.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvMenu.adapter = GamesAdapter(this@MainActivity, response.body()!!)

                     */

                }

                override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "No hay conexión. Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    //binding.pbConexion.visibility = View.GONE
                }
            })

        }
    }
}