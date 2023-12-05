package com.dominate.thirtySecondsChallenge.ui.auctionround

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import butterknife.ButterKnife
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.base.adapters.OnItemClickListener
import com.dominate.thirtySecondsChallenge.base.fragment.BaseBindingFragment
import com.dominate.thirtySecondsChallenge.databinding.FragmentAuctionRoundBinding
import com.dominate.thirtySecondsChallenge.ui.auctionround.sheet.CountAnswerFragment
import com.dominate.thirtySecondsChallenge.ui.endgame.lossgame.LossGameFragment
import com.dominate.thirtySecondsChallenge.ui.endgame.wingame.WinGameFragment
import com.dominate.thirtySecondsChallenge.ui.player.dialog.GiftFriendsFragment
import com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.adapter.QuestionAdapter
import com.dominate.thirtySecondsChallenge.ui.whatdoyouknow.model.QuestionModel
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogAnswer
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogCircular
import com.dominate.thirtySecondsChallenge.utils.dialog.showDialogLottie
import com.dominate.thirtySecondsChallenge.utils.extensions.addPlaySound
import com.dominate.thirtySecondsChallenge.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auction_round.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuctionRoundFragment : BaseBindingFragment<FragmentAuctionRoundBinding>(),
    OnItemClickListener<QuestionModel> {

    override fun getLayoutId(): Int = R.layout.fragment_auction_round

    private val viewModel by activityViewModels<AuctionRoundViewModel>()

    @Inject
    lateinit var adapter: QuestionAdapter

    var handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)

        setUpData()
        setUpViewsListeners()
        setRecycleView()
    }


    private fun setUpData() {
        binding?.viewModel = viewModel

        requireContext().showDialogLottie(
            R.raw.auction_round, "جولة المزاد", isTimer = 1500
        )
//        addPlaySound(requireContext(), R.raw.sound_auction_round)
//        addPlaySound(requireContext(), R.raw.sd_start_round)

    }

    private fun setUpViewsListeners() {

        tv_NumberOfAnswers.onClick {
            requireContext().showDialogCircular(
                "ابدأ المزايدة",
                R.drawable.ic_circuler_blue,
                1500
            )
//            addPlaySound(requireContext(), R.raw.sound_turn_now)
            Handler(Looper.getMainLooper()).postDelayed({

//                val pd = CountAnswer()
//                pd.show(parentFragmentManager, "Count Answer")
                val countAnswerFragment = CountAnswerFragment()
                countAnswerFragment.show(parentFragmentManager, "count Answer Fragment")
            }, 1500)


        }

        btn_TakeTurn.onClick {
            viewModel.startAuctionRound.value = false

            requireContext().showDialogLottie(
                R.raw.time_started,
                "بدأ الوقت!",
                isTimer = 1500
            )
//            addPlaySound(requireContext(), R.raw.sd_start_time)

//            addPlaySound(requireContext(), R.raw.sound_auction_round)

            setRecycleView()
        }

        iv_keyboard.onClick {
            val winGameFragment = WinGameFragment()
//            val winGameFragment = LossGameFragment()
            winGameFragment.show(parentFragmentManager, "win Game Fragment")
        }

    }

    private fun setRecycleView() {

        context?.let {
            adapter = QuestionAdapter()
            rvQuestions.adapter = adapter
//            adapter.onItemClickListener = this
        }

//        adapter.submitItems(
//            arrayListOf(
//                QuestionModel(
//                    "بنجامين ميندي", true
//                ),
//                QuestionModel(
//                    "جابي",
//                ),
//                QuestionModel(
//                    "تيبو كارتو",
//                ),
//                QuestionModel(
//                    "انخيل دي ماريا",
//                ),
//                QuestionModel(
//                    "برناردو سيلفا",
//                ),
//                QuestionModel(
//                    "تيمو باكايوكو",
//                ),
//                QuestionModel(
//                    "جاري كاهيل",
//                ),
//                QuestionModel(
//                    "جابي",
//                ),
//                QuestionModel(
//                    "دافيد اوسبينا",
//                ),
//                QuestionModel(
//                    "خوانفران",
//                ),
//                QuestionModel(
//                    "راؤول جارسيا",
//                ),
//                QuestionModel(
//                    "دييجو كوستا",
//                ),
//            )
//        )

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onItemClicked(view: View?, item: QuestionModel, position: Int) {
        requireContext().showDialogAnswer(
            item.itemName
        )
        addPlaySound(requireContext(), R.raw.sound_shift)

        if (item.itemName == "بنجامين ميندي") {
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                requireContext().showDialogCircular(
                    "اجابة\nخاطئة", R.drawable.ic_circuler_red,1500
                )
                addPlaySound(requireContext(), R.raw.sd_wrong_answer)
            }

        } else if (item.itemName == "جابي") {
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                requireContext().showDialogCircular(
                    "اجابة\nصحيحة!", R.drawable.ic_circuler_green,
                    1500
                )
                addPlaySound(requireContext(), R.raw.sd_right_answer)
            }

        } else {
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                requireContext().showDialogCircular(
                    "دورك الآن", R.drawable.ic_circuler_blue,1500
                )
                addPlaySound(requireContext(), R.raw.sound_turn_now)
            }
        }


    }

}