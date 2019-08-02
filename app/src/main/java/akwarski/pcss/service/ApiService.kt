package akwarski.pcss.service

import akwarski.pcss.model.Picture
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/photos")
    fun fetchPhotos(): Call<List<Picture>>
}