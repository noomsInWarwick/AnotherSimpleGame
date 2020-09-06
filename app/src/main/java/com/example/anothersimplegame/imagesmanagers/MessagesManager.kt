package com.example.anothersimplegame.imagesmanagers

import android.content.res.Resources
import com.example.anothersimplegame.R

object MessagesManager {

    var messagesList = ArrayList<String>()
    var randomIdx = 0

    fun loadMessages(resources: Resources) {
        // load messages - refactor to be in separate class
        messagesList.add(resources.getString(R.string.niceMsg1))
        messagesList.add(resources.getString(R.string.niceMsg2))
        messagesList.add(resources.getString(R.string.niceMsg3))
        messagesList.add(resources.getString(R.string.niceMsg4))
    }

    fun getMessage(): String {

        messagesList.shuffle()
        return messagesList.get(0)
    }

    fun cleanUp() {
        messagesList.clear()
    }
}