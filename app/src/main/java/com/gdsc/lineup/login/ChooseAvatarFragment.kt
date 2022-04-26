package com.gdsc.lineup.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gdsc.lineup.ActionEventListener
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.FragmentChooseAvatarBinding

class ChooseAvatarFragment : Fragment(), ActionEventListener {

    private lateinit var binding: FragmentChooseAvatarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChooseAvatarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        setActionEvent()
        super.onAttach(context)
    }

    private fun setActionEvent() {
        if (activity is LoginActivity) {
            (activity as LoginActivity).setActionListener(this, getString(R.string.next))
        }
    }

    override fun onActionEvent() {
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.containerSignIn, RulesFragment())?.commit()
    }

}