package com.example.anothersimplegame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.anothersimplegame.R

class FragmentSnowman : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_snowman, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): FragmentSnowman {
            return FragmentSnowman()
        }
    }

}
