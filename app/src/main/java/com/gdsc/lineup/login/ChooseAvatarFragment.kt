package com.gdsc.lineup.login

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.gdsc.lineup.ActionEventListener
import com.gdsc.lineup.LoginViewModel
import com.gdsc.lineup.R
import com.gdsc.lineup.databinding.FragmentChooseAvatarBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.lang.Math.abs

@AndroidEntryPoint
class ChooseAvatarFragment : Fragment(), ActionEventListener {

    private lateinit var binding: FragmentChooseAvatarBinding
    private lateinit var avatarAdapter: AvatarAdapter
    private val viewModel: LoginViewModel by activityViewModels()
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
        binding.avatarVp.offscreenPageLimit = 2
        binding.avatarVp.adapter = avatarAdapter
        binding.avatarVp.clipToPadding = false
        binding.avatarVp.clipChildren = false
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * abs(position))

        }
        val offsetPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin).toInt()
                .dpToPx(resources.displayMetrics)
        binding.avatarVp.setPadding(offsetPx, 0, offsetPx, 0)
        binding.avatarVp.setPageTransformer(pageTransformer)
        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
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

    private fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()

    override fun onAttach(context: Context) {
        setActionEvent()
        super.onAttach(context)
    }

    private fun setActionEvent() {
        if (activity is LoginActivity) {
            (activity as LoginActivity).setActionListener(this, getString(R.string.next))
        }
    }

    override fun onActionEvent() {
        val pos = binding.avatarVp.currentItem
        viewModel.userModel.value?.avatarId = avatars[pos].toString()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.containerSignIn, RulesFragment())?.commit()
    }

}