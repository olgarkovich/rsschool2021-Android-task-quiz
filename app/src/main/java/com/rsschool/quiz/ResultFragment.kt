package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val userAnswers = ArrayList<String?>()
    private lateinit var fragmentResultQuiz: OnFragmentResultQuiz
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireActivity().getSharedPreferences(USER_ANSWERS, Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.result.text = resources.getString(R.string.your_result, countResult())

        binding.share.setOnClickListener {
            shareResult()
        }

        binding.back.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            fragmentResultQuiz.showQuiz()
        }

        binding.close.setOnClickListener {
            fragmentResultQuiz.closeApp()
        }
    }

    private fun shareResult() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, makeResultDescription())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun countResult(): String {
        var correctCount = 0

        for (i in 0..QuizData.questions.lastIndex) {
            userAnswers.add(sharedPreferences.getString("question$i", null))

            if (QuizData.rightAnswers[i] == userAnswers[i]) {
                correctCount++
            }
        }

        return "${correctCount * 100 / QuizData.questions.size} %"

    }

    private fun makeResultDescription(): String {
        var result = "Quiz results\n\nYour result: ${countResult()}\n\n"

        for (i in 1..QuizData.questions.size) {
            result += "$i) ${QuizData.questions[i-1]}\nYour answer: ${userAnswers[i-1]}\n\n"
        }

        return result
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentResultQuiz = context as OnFragmentResultQuiz
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    interface OnFragmentResultQuiz {
        fun showQuiz()
        fun closeApp()
    }

    companion object {
        @JvmStatic
        fun newInstance(): ResultFragment {
            return ResultFragment()
        }

        private const val USER_ANSWERS = "userAnswers"
    }
}