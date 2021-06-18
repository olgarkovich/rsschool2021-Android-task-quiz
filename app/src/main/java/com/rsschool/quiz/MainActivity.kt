package com.rsschool.quiz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class MainActivity : AppCompatActivity(), QuizFragment.OnFragmentSendQuestionNumber,
    ResultFragment.OnFragmentResultQuiz {

    override fun onCreate(savedInstanceState: Bundle?) {
        val currentThemeId = intent.getIntExtra(CURRENT_THEME, R.style.Theme_Quiz_First)
        val current = intent.getIntExtra(CURRENT_QUESTION, 0)
        setTheme(currentThemeId)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openQuizFragment(current)
    }

    private fun openQuizFragment(currentQuestion: Int) {
        val quizFragment: Fragment = QuizFragment.newInstance(currentQuestion)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, quizFragment)
        transaction.commit()
    }

    private fun openResultFragment() {
        val resultFragment: Fragment = ResultFragment.newInstance()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, resultFragment)
        transaction.commit()
    }

    override fun sendQuestionNumber(currentQuestion: Int) {
        changeTheme(currentQuestion)
    }

    override fun showResultFragment() {
        openResultFragment()
    }

    override fun showQuiz() {
        openQuizFragment(0)
    }

    override fun closeApp() {
        this.finish()
    }

    private fun changeTheme(currentQuestion: Int) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(CURRENT_THEME, chooseTheme(currentQuestion))
        intent.putExtra(CURRENT_QUESTION, currentQuestion)

        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun chooseTheme(currentQuestion: Int): Int {
        when (currentQuestion % 5) {
            0 -> return R.style.Theme_Quiz_First
            1 -> return R.style.Theme_Quiz_Second
            2 -> return R.style.Theme_Quiz_Third
            3 -> return R.style.Theme_Quiz_Fourth
            4 -> return R.style.Theme_Quiz_Fifth
        }

        return R.style.Theme_Quiz_First
    }

    companion object {
        const val CURRENT_THEME = "currentTheme"
        const val CURRENT_QUESTION = "currentQuestion"
    }
}