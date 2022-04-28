package com.gdsc.lineup.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.gdsc.lineup.MainViewModel
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.FragmentRouteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class RouteFragment : Fragment() {

    private lateinit var binding: FragmentRouteBinding
    private val vm by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRouteBinding.inflate(inflater)
        subscribeUI()
        return binding.root
    }

    private fun subscribeUI() {
        vm.first.observe(viewLifecycleOwner) {
            if (it==null) {
                binding.tv1.text = "Nobody nearby"
                return@observe
            }
            binding.tv1.text = "${it.distance.roundToInt()}m"
            try {
                binding.iv1.setImageDrawable(ResourcesCompat.getDrawable(resources,it.avatar.toInt(),null))
            } catch (e: Exception) {}
        }
        vm.second.observe(viewLifecycleOwner) {
            if (it==null) {
                binding.tv2.text = "Nobody nearby"
                return@observe
            }
            binding.tv2.text = "${it.distance.roundToInt()}m"
            try {
                binding.iv2.setImageDrawable(ResourcesCompat.getDrawable(resources,it.avatar.toInt(),null))
            } catch (e: Exception) {}
        }
        vm.first.observe(viewLifecycleOwner) {
            if (it==null) {
                binding.tv3.text = "Nobody nearby"
                return@observe
            }
            binding.tv3.text = "${it.distance.roundToInt()}m"
            try {
                binding.iv3.setImageDrawable(ResourcesCompat.getDrawable(resources,it.avatar.toInt(),null))
            } catch (e: Exception) {}
        }
    }

    override fun onDestroyView() {
        vm.first.removeObservers(viewLifecycleOwner)
        vm.second.removeObservers(viewLifecycleOwner)
        vm.third.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }
}