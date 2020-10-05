package com.ericdecanini.mockwebtest

import java.io.InputStreamReader

class MockResponseFileReader(path: String) {

    val contents: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(path))
        contents = reader.readText()
        reader.close()
    }

}
