package com.gdsc.lineup.leaderBoard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdsc.lineup.MainViewModel
import com.gdsc.lineup.databinding.FragmentLeaderBoardBinding
import com.gdsc.lineup.models.ResultHandler
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Created: Karan Verma on 25/04/22
 */
@AndroidEntryPoint
class LeaderBoardFragment : Fragment() {

    private var _binding: FragmentLeaderBoardBinding? = null
    private val binding get() = _binding!!

    private var leaderBoardAdapter: LeaderBoardAdapter? = null
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLeaderBoardData()
        observer()
        leaderBoardAdapter = LeaderBoardAdapter()
        binding.leaderRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = leaderBoardAdapter
        }
    }

    private fun observer() {
        viewModel.leaders.observe(viewLifecycleOwner){
            when(it){
                is ResultHandler.Loading -> {

                }
                is ResultHandler.Success -> {
                    if (!it.result.isNullOrEmpty()){
                        leaderBoardAdapter?.setList(it.result)
                    }
                }
                is ResultHandler.Failure -> {

                }
            }
        }
    }

    private fun getLeaderBoardData() {
        viewModel.getLeaderBoard()
    }
}