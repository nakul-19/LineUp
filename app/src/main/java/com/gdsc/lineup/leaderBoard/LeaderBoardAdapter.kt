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

    private var list: ArrayList<LeaderBoardResponse> = arrayListOf()
    fun setList(list: ArrayList<LeaderBoardResponse>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH = VH(
        ItemLeaderBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.name.text = list[position].name
        holder.binding.score.text = list[position].score.toString()
    }

    override fun getItemCount(): Int = list.size

    class VH(val binding: ItemLeaderBoardBinding) : RecyclerView.ViewHolder(binding.root)

}