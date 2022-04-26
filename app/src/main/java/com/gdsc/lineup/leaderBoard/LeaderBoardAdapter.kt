package com.gdsc.lineup.leaderBoard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdsc.lineup.databinding.ItemLeaderBoardBinding

/**
 * @Author: Karan Verma
 * @Date: 25/04/22
 */
class LeaderBoardAdapter : RecyclerView.Adapter<LeaderBoardAdapter.VH>() {

    private var list: ArrayList<LeaderModel> = arrayListOf()
    fun setList(list: ArrayList<LeaderModel>) {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemLeaderBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
    }

    override fun getItemCount(): Int = list.size

    class VH(val binding: ItemLeaderBoardBinding) : RecyclerView.ViewHolder(binding.root)

}