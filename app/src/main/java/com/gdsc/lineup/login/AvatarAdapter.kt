package com.gdsc.lineup.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.AvatarLayoutBinding
import java.util.*

internal class AvatarAdapter(private val context: Context, private val images: IntArray): RecyclerView.Adapter<AvatarAdapter.VH>() {


    class VH(val binding: AvatarLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            AvatarLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.avatarImg.setImageResource(images[position])
    }

    override fun getItemCount(): Int = images.size
}