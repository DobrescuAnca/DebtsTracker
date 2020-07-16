package com.debts.debtstracker.ui.main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.debts.debtstracker.R
import com.debts.debtstracker.databinding.FragmentProfileBinding
import com.debts.debtstracker.ui.base.BaseFragment
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs
import kotlin.math.roundToInt

class ProfileFragment: BaseFragment() {

    private lateinit var dataBinding: FragmentProfileBinding

    private var expandedAvatarSize: Float  = 0f
    private var collapseImageSize: Float = 0f
    private var horizontalToolbarAvatarMargin: Float = 0f
    private var cashCollapseState: Pair<Int, Int>? = null

    private var avatarAnimateStartPointY: Float = 0F
    private var avatarCollapseAnimationChangeWeight: Float = 0F
    private var isCalculated = false
    private var verticalToolbarAvatarMargin =0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )

        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCollapseAppBar()

    }

    private fun setCollapseAppBar(){
        expandedAvatarSize = resources.getDimension(R.dimen.default_expanded_image_size)
        collapseImageSize = resources.getDimension(R.dimen.default_collapsed_image_size)
        horizontalToolbarAvatarMargin = resources.getDimension(R.dimen.margin)

        dataBinding.appBar.addOnOffsetChangedListener( AppBarLayout.OnOffsetChangedListener{
                appBarLayout, verticalOffset ->
            if(isCalculated.not()){
                avatarAnimateStartPointY =
                    abs((appBarLayout.height - (expandedAvatarSize + horizontalToolbarAvatarMargin)) / appBarLayout.totalScrollRange)
                avatarCollapseAnimationChangeWeight = 1 / (1 - avatarAnimateStartPointY)
                verticalToolbarAvatarMargin = (dataBinding.toolbar.height - collapseImageSize)
                isCalculated = true
            }

            updateViews(abs(verticalOffset / appBarLayout.totalScrollRange.toFloat()))
        })
    }

    private fun updateViews(offset: Float) {

        /* apply levels changes*/
        when(offset) {
            in 0F..0.15F -> {
                dataBinding.tvProfileName.alpha = 1F
                dataBinding.tvUsername.alpha = 1F
                dataBinding.friendshipStatusView.alpha = 1F
                dataBinding.ivProfilePic.alpha = 1f
            }
            in 0.15F..1F -> {
                dataBinding.tvProfileName.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
                dataBinding.tvUsername.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
                dataBinding.friendshipStatusView.apply {
                    if (visibility != View.VISIBLE) visibility = View.VISIBLE
                    alpha = (1 - offset) * 0.35F
                }
            }
        }

        /** collapse - expand switch*/
        when {
            offset < SWITCH_BOUND -> Pair(TO_EXPANDED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
            else -> Pair(TO_COLLAPSED, cashCollapseState?.second ?: WAIT_FOR_SWITCH)
        }.apply {
            when {
                cashCollapseState != null && cashCollapseState != this -> {
                    when (first) {
                        TO_EXPANDED ->  {
                            /* set avatar on start position (center of parent frame layout)*/
                            dataBinding.ivProfilePic.translationX = 0F
                            /* hide top titles on toolbar*/
                            dataBinding.tvToolbarText.visibility = View.INVISIBLE
                        }

                        TO_COLLAPSED ->
                            /* show titles on toolbar with animation*/
                            dataBinding.tvToolbarText.apply {
                                visibility = View.VISIBLE
                                alpha = 0F
                                animate().setDuration(500).alpha(1.0f)
                        }
                    }
                    cashCollapseState = Pair(first, SWITCHED)
                }
                else -> cashCollapseState = Pair(first, WAIT_FOR_SWITCH)
            }
        }

        collapseProfilePic(offset)
    }

    private fun collapseProfilePic(offset: Float){
        dataBinding.ivProfilePic.apply {
            Log.i(LOG_ANIM, "img translationX $translationX")
            Log.i(LOG_ANIM, "offset $offset")
            Log.i(LOG_ANIM, "avatarAnimateStartPointY $avatarAnimateStartPointY")
            when {
                offset > avatarAnimateStartPointY -> {
                    val avatarCollapseAnimateOffset =
                        (offset - avatarAnimateStartPointY) * avatarCollapseAnimationChangeWeight
                    Log.i(LOG_ANIM, "avatarCollapseAnimateOffset $avatarCollapseAnimateOffset")
                    val avatarSize =
                        expandedAvatarSize - (expandedAvatarSize - collapseImageSize) * avatarCollapseAnimateOffset

                    Log.i(LOG_ANIM, "avatarSize $avatarSize")

                    val layoutParams = this.layoutParams
                    layoutParams.height = avatarSize.roundToInt()
                    layoutParams.width = avatarSize.roundToInt()
                    this.layoutParams = layoutParams

                    this.translationX = 0 -
                        ((dataBinding.appBar.width - horizontalToolbarAvatarMargin - avatarSize) / 2) * avatarCollapseAnimateOffset

                    Log.i(LOG_ANIM, "translationX $translationX")
                    this.translationY =
                        ((dataBinding.toolbar.height - verticalToolbarAvatarMargin - avatarSize) / 2) * avatarCollapseAnimateOffset
                }
                else -> this.layoutParams.also {
                    if (it.height != expandedAvatarSize.toInt()) {
                        it.height = expandedAvatarSize.toInt()
                        it.width = expandedAvatarSize.toInt()
                        this.layoutParams = it
                    }
                    translationX = 0f
                }
            }
        }

        Log.i(LOG_ANIM, "-----------------")
    }

    companion object {
        const val SWITCH_BOUND = 0.8f
        const val TO_EXPANDED = 0
        const val TO_COLLAPSED = 1
        const val WAIT_FOR_SWITCH = 0
        const val SWITCHED = 1
        const val LOG_ANIM = "log anim"
    }
}