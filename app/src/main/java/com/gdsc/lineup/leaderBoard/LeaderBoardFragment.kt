package com.gdsc.lineup.leaderBoard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.FragmentLeaderBoardBinding

/**
 * @Created: Karan Verma on 25/04/22
 */
class LeaderBoardFragment : Fragment() {

    private var _binding: FragmentLeaderBoardBinding? = null
    private val binding get() = _binding!!

    private var leaderBoardAdapter: LeaderBoardAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leaderBoardAdapter = LeaderBoardAdapter()
        binding.leaderRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = leaderBoardAdapter
        }
    }
}