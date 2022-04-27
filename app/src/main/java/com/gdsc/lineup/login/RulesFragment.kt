package com.gdsc.lineup.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.bold
import androidx.fragment.app.activityViewModels
import com.gdsc.lineup.*
import com.gdsc.lineup.databinding.FragmentRulesBinding
import com.gdsc.lineup.models.ResultHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RulesFragment : Fragment(), ActionEventListener {

    private lateinit var binding: FragmentRulesBinding
    private val viewModel: LoginViewModel by activityViewModels()
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

        val name = viewModel.userModel.value?.name
        val spannableText = SpannableStringBuilder()
            .append("Hey $name\n")
            .bold { append(getString(R.string.need_your_help)) }
            .append(getString(R.string.rules))
        binding.content.text = spannableText
        setObserver()
    }

    private fun setObserver() {
        viewModel.registerRes.observe(viewLifecycleOwner){
            when(it){
                is ResultHandler.Loading -> {
                    binding.pb.visibility = View.VISIBLE
                }
                is ResultHandler.Success -> {
                    binding.pb.visibility = View.INVISIBLE
                    val intent = Intent(requireContext().applicationContext, LoginActivity::class.java)
                    intent.putExtra(WelcomeActivity.TARGET_FRAGMENT, "LOGIN")
                    startActivity(intent)
                    activity?.finish()
                    Toast.makeText(requireContext(), "Registered Successfully", Toast.LENGTH_SHORT).show()
                }
                is ResultHandler.Failure -> {
                    binding.pb.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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
        viewModel.registerUser()
    }
}