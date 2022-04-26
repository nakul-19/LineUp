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
//    var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//
//    override fun getCount(): Int = images.size
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view === `object` as LinearLayout
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val itemView: View = mLayoutInflater.inflate(R.layout.avatar_layout, container, false)
//
//        val imageView: ImageView = itemView.findViewById(R.id.avatarImg)
//
//        imageView.setImageResource(images[position])
//
//        Objects.requireNonNull(container).addView(itemView)
//        return itemView
//
//    }
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        container.removeView(`object` as LinearLayout)
//    }
}