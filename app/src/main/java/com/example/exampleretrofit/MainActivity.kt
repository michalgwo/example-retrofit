package com.example.exampleretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var retrofitService: AlbumService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        retrofitService = RetrofitInstance.getRetrofitInstance().create(AlbumService::class.java)

        requestForSingleAlbum()
        requestForMultipleAlbums()
        uploadAlbum()
    }

    private fun requestForSingleAlbum() {
        val albumLiveData: LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.getAlbum(12)
            emit(response)
        }

        albumLiveData.observe(this) {
            val album = it.body()

            if (album != null) {
                val result = "Chosen album: ${album.id}. ${album.title}, userId: ${album.userId} \n\n"
                textView.append(result)
            }
        }
    }

    private fun requestForMultipleAlbums() {
        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retrofitService.getFilteredAlbums(3)
            emit(response)
        }

        responseLiveData.observe(this) {
            val albumsList = it.body()?.listIterator()
            if (albumsList != null) {
                while (albumsList.hasNext()) {
                    val album = albumsList.next()
                    val result = "${album.id}. ${album.title}, userId: ${album.userId} \n"
                    textView.append(result)
                }
            }
        }
    }

    private fun uploadAlbum() {
        val newAlbum = AlbumsItem(0, "album name", 51)

        val postResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.uploadAlbum(newAlbum)
            emit(response)
        }

        postResponse.observe(this) {
            val album = it.body()

            if (album != null) {
                val result = "\nAlbum uploaded: ${album.id}. ${album.title}, userId: ${album.userId} \n\n"
                textView.append(result)
            }
        }
    }
}