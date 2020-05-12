package com.ericdecanini.mockwebtest

import io.reactivex.Single
import retrofit2.http.GET

interface PlaceholderApi {

    @GET("posts")
    fun getPosts(): Single<List<Post>>

}