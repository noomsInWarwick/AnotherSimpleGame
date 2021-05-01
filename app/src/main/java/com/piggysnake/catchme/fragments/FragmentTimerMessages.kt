package com.piggysnake.catchme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.piggysnake.catchme.R

class FragmentTimerMessages : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer_n_messages, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): FragmentTimerMessages {
            return FragmentTimerMessages()
        }
    }

}
