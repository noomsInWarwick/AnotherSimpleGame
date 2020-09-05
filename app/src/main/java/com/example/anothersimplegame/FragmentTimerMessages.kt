package com.example.anothersimplegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

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
