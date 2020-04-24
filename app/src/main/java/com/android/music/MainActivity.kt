package com.android.music

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.Gravity
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.music.Adapter.SongAdapter
import com.android.music.ViewModel.SongViewModel
import com.android.music.model.Songs

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var adapter : SongAdapter;
    private  val newWordActivityRequestCode = 1
    val client = OkHttpClient()
    lateinit var songViewModel: SongViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        progress.visibility = View.GONE
        adapter = SongAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        songViewModel  = ViewModelProvider(this).get(SongViewModel::class.java)


        serachView.setQueryHint("Search by Artist Name")
        serachView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                run( "https://itunes.apple.com/search?term="+query)
                Thread.sleep(2000)
                return true
            }
        })
        getUpdateUi();


    }

    fun run(url: String) {
         var flag : Int = 0
        progress.visibility = View.VISIBLE
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {


            override fun onFailure(call: okhttp3.Call, e: IOException) {
                progress.visibility = View.GONE

            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if(response.isSuccessful)
                {var str_response = response.body()!!.string()
                val json_contact:JSONObject = JSONObject(str_response)
                var jsonarray_info: JSONArray = json_contact.getJSONArray("results")
                var i:Int = 0
                var size:Int = jsonarray_info.length()
                Log.e("SIZEOFARRAY",size.toString())
                    if(size != 0)
                {
                    songViewModel.deleteAll()
                    Thread.sleep(4000)
                    for (i in 0.. size-1) {
                    var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)
                    var songs: Songs = Songs(json_objectdetail.getString("trackId"), json_objectdetail.getString("trackName"), json_objectdetail.getString("artworkUrl100"), json_objectdetail.getString("collectionName"),
                            json_objectdetail.getString("primaryGenreName"), json_objectdetail.getString("trackTimeMillis"));
                        songViewModel.insert(songs)
                        Log.e("data", json_objectdetail.getString("trackId"))

                    }
                }
                else
                {
                    flag =1
                  }}
                else
                {
                    flag =1}

                runOnUiThread {
                    if(flag == 1) {
                        val myToast = Toast.makeText(
                            applicationContext,
                            "NO Record Found",
                            Toast.LENGTH_SHORT
                        )
                        myToast.setGravity(Gravity.LEFT, 200, 200)
                        myToast.show()
                    }
                    else {
                        //stuff that updates ui

                        getUpdateUi();

                    }

                    progress.visibility = View.GONE
                }
            }
        })
    }

    private fun getUpdateUi() {

        songViewModel.allSongs.observe(this, Observer { words ->
            words?.let { adapter.setWords(it) }
        })
        Log.e("ADPATER Size",adapter.itemCount.toString())

    }

}
