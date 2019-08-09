package akwarski.pcss

import akwarski.pcss.adapter.ExpandableListViewAdapter
import akwarski.pcss.model.Picture
import akwarski.pcss.service.ApiService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

private const val BASE_URL = "http://jsonplaceholder.typicode.com"

class MainActivity : AppCompatActivity() {

    var identity: MutableList<String> = ArrayList()
    val pics: MutableList<MutableList<String>> = ArrayList()
    var thumbnails: MutableList<String> = ArrayList()
    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable
    private val wait = 5000.toLong()
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        identity.add("1")
        mHandler = Handler()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        load()

        //swipe to refresh when we want to reload data or when our data were not load because of lack internet
        swipe.setOnRefreshListener {
            mRunnable = Runnable {
                load()
                swipe.isRefreshing = false
            }
            mHandler.postDelayed(
                mRunnable,
                wait
            )
        }
    }

    //load data from specific API using retrofit
    private fun load(){
        val api = retrofit.create(ApiService::class.java)
        api.fetchPhotos().enqueue(object: Callback<List<Picture>> {
            override fun onFailure(call: Call<List<Picture>>, t: Throwable) {
                d("error", "Do not have connection")
                //visible text or list depend on internet connection
                noInternet.isVisible = true
                expandedListView.isVisible = false
            }

            override fun onResponse(call: Call<List<Picture>>, response: Response<List<Picture>>) {
                //visible text or list depend on internet connection
                noInternet.isVisible = false
                expandedListView.isVisible = true
                //foreach to search all object
                for(item in response.body()!!){
                    // -1 because empty list return -1
                    if(item.albumId.toInt()-1 != identity.lastIndex){
                        //add specific albumId to list
                        identity.add(item.albumId)
                        //add group of thumbnail to list
                        pics.add(thumbnails)
                        //create new list to add next group of thumbnailsUrl
                        thumbnails = ArrayList()
                    }
                    if(item.albumId.toInt()-1 == identity.lastIndex){
                        //add thumbnailUrl to group
                        thumbnails.add(item.thumbnailUrl)
                    }
                }
                //add thumbnail which albumId equals 100
                pics.add(thumbnails)
                expandedListView.setAdapter(ExpandableListViewAdapter(this@MainActivity, identity as ArrayList<String>, pics))
            }
        })
    }
}