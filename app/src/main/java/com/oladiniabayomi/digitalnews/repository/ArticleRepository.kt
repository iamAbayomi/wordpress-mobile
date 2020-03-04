package com.oladiniabayomi.digitalnews.repository

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oladiniabayomi.digitalarticles.articles.Articles
import com.oladiniabayomi.digitalarticles.articles.SavedArticles
import com.oladiniabayomi.digitalnews.articles.ArticlesDao
import com.oladiniabayomi.digitalnews.network.PostsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ArticleRepository( private val articlesDao: ArticlesDao, var context: Application){

    // private val webSer

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //val allArticles : LiveData<List<Articles>> = articlesDao.getAllArticles()
    val allArticles : LiveData<List<Articles>> = getArticles()

    val FRESH_TIMEOUT_IN_MINUTES = 2

    suspend fun insert(articles: Articles){
        articlesDao.insertArticles(articles)
    }

    fun getArticles() : LiveData<List<Articles>>{

        return  refreshArticles()
    }



     fun refreshArticles() : LiveData<List<Articles>> {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.tell.com.ng/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(PostsService::class.java)

        val call = service.getPosts()
        val data = MutableLiveData<List<Articles>>()

        call.enqueue(object : Callback<List<Articles>> {
            override fun onResponse(call: Call<List<Articles>>?, response: Response<List<Articles>>?) {
                if (response!!.code() == 200)
                {
                    data.value = response.body()
                    Toast.makeText(context, response.body()[1].toString(), Toast.LENGTH_LONG).show()

                }
             }
            override fun onFailure(call: Call<List<Articles>>?, t: Throwable?) {

            }
        })
        return  data
    }
}