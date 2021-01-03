package com.example.anothersimplegame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.anothersimplegame.R

class FragmentStartAgain : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start_again, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): FragmentStartAgain {
            return FragmentStartAgain()
        }
    }
}
