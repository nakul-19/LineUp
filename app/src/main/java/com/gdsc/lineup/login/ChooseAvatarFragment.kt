package com.gdsc.lineup.login

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.gdsc.lineup.ActionEventListener
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.FragmentChooseAvatarBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.abs

@AndroidEntryPoint
class ChooseAvatarFragment : Fragment(), ActionEventListener {

    private lateinit var binding: FragmentChooseAvatarBinding
    private lateinit var avatarAdapter: AvatarAdapter
    private var avatars: IntArray = intArrayOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChooseAvatarBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAvatars()
        avatarAdapter = AvatarAdapter(requireContext(), avatars)
        binding.avatarVp.offscreenPageLimit = 1
        binding.avatarVp.adapter = avatarAdapter
// Add a PageTransformer that translates the next and previous items horizontally
// towards the center of the screen, which makes them visible
        val nextItemVisiblePx = 20.toDp
        val currentItemHorizontalMarginPx = 40.toDp
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        binding.avatarVp.setPageTransformer(pageTransformer)

// The ItemDecoration gives the current (centered) item horizontal margin so that
// it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            40
        )
        binding.avatarVp.addItemDecoration(itemDecoration)
    }

    private fun setAvatars() {
        avatars = intArrayOf(
            R.drawable.ic_avatar_1,
            R.drawable.ic_avatar_2,
            R.drawable.ic_avatar_3,
            R.drawable.ic_avatar_4,
            R.drawable.ic_avatar_5,
            R.drawable.ic_avatar_6,
            R.drawable.ic_avatar_7,
            R.drawable.ic_avatar_8,
        )
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

    val Int.toDp get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    override fun onActionEvent() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.containerSignIn, RulesFragment())?.commit()
    }

}