package com.gdsc.lineup.login

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import com.gdsc.lineup.ActionEventListener
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.FragmentRulesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RulesFragment : Fragment(), ActionEventListener {

    private lateinit var binding: FragmentRulesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRulesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = "Aj"
        val spannableText = SpannableStringBuilder()
            .append("Hey $name\n")
            .bold { append(getString(R.string.need_your_help)) }
            .append(getString(R.string.rules))
        binding.content.text = spannableText
    }

    override fun onAttach(context: Context) {
        setActionEvent()
        super.onAttach(context)
    }

    private fun setActionEvent() {
        if (activity is LoginActivity) {
            (activity as LoginActivity).setActionListener(this, getString(R.string.finish))
        }
    }

    override fun onActionEvent() {

    }
}