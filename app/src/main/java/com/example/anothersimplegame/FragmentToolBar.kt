package com.example.anothersimplegame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class FragmentToolBar : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tool_bar_constrained, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(): FragmentToolBar {
            return FragmentToolBar()
        }
    }

}
