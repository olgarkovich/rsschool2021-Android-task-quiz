package com.rsschool.quiz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var currentQuestion = 0
    private var options: ArrayList<RadioButton> = arrayListOf()
    private lateinit var fragmentSendQuestionNumberListener: OnFragmentSendQuestionNumber
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        sharedPreferences =
            requireActivity().getSharedPreferences(USER_ANSWERS, Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options.add(binding.optionOne)
        options.add(binding.optionTwo)
        options.add(binding.optionThree)
        options.add(binding.optionFour)
        options.add(binding.optionFive)

        currentQuestion = arguments?.getInt(CURRENT_QUESTION) ?: 0

        renderData()

        binding.toolbar.setNavigationOnClickListener {

            if (currentQuestion > 0) {
                currentQuestion--
                fragmentSendQuestionNumberListener.sendQuestionNumber(currentQuestion)
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            var userAnswer = -1
            for (i in 0 until ANSWER_COUNT) {
                if (options[i].isChecked) {
                    userAnswer = i
                }
            }
            binding.nextButton.isEnabled = true
            saveAnswer(options[userAnswer].text.toString())
        }

        binding.nextButton.setOnClickListener {

            if (currentQuestion < 4) {
                currentQuestion++
                fragmentSendQuestionNumberListener.sendQuestionNumber(currentQuestion)
            } else {
                fragmentSendQuestionNumberListener.showResultFragment()
            }
        }

        binding.previousButton.setOnClickListener {

            if (currentQuestion > 0) {
                currentQuestion--
                fragmentSendQuestionNumberListener.sendQuestionNumber(currentQuestion)
            }
        }
    }

    private fun renderData() {

        if (currentQuestion == 0) {
            binding.previousButton.visibility = View.INVISIBLE
            binding.toolbar.navigationIcon = null
        }

        if (currentQuestion == QuizData.questions.lastIndex) {
            binding.nextButton.text = resources.getString(R.string.submit)
        }

        binding.toolbar.title = "Question ${currentQuestion + 1}"
        binding.question.text = QuizData.questions[currentQuestion]

        val answers = QuizData.answers[currentQuestion]

        val answerBefore: String? = getAnswer()
        for (i in 0..options.lastIndex) {
            options[i].text = answers[i]

            if (answerBefore == answers[i]) {
                options[i].isChecked = true
                binding.nextButton.isEnabled = true
            }
        }
    }

    private fun saveAnswer(text: String) {
        val editor = sharedPreferences.edit()
        editor.putString("question$currentQuestion", text)
        editor.apply()
    }

    private fun getAnswer(): String? {
        return sharedPreferences.getString("question$currentQuestion", null)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSendQuestionNumberListener = context as OnFragmentSendQuestionNumber
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    interface OnFragmentSendQuestionNumber {
        fun sendQuestionNumber(currentQuestion: Int)
        fun showResultFragment()
    }

    companion object {
        @JvmStatic
        fun newInstance(currentQuestion: Int): QuizFragment {
            val fragment = QuizFragment()
            val args = Bundle()
            args.putInt(CURRENT_QUESTION, currentQuestion)
            fragment.arguments = args
            return fragment
        }

        private const val CURRENT_QUESTION = "currentQuestion"
        private const val ANSWER_COUNT = 5
        private const val USER_ANSWERS = "userAnswers"
    }
}