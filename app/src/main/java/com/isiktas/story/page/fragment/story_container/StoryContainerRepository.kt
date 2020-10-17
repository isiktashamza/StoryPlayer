package com.isiktas.story.page.fragment.story_container

import org.json.JSONObject
import java.io.InputStream

class StoryContainerRepository (private val inputStream: InputStream) {

    fun readStories() : JSONObject {
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return JSONObject(String(buffer))
    }
}