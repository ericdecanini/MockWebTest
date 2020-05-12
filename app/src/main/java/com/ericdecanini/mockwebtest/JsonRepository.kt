package com.ericdecanini.mockwebtest

import io.reactivex.Single

class JsonRepository(private val api: PlaceholderApi) {

    fun observePosts(): Single<List<Post>> = api.getPosts()

}