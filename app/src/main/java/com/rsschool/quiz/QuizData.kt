package com.rsschool.quiz

object QuizData {

    val questions = listOf(
        "1 + 3 = ?",
        "2 + 2 * 2 = ?",
        "4 * 3 = ?",
        "4 * 4 + 1 = ?",
        "665 + 1 = ?"
    )

    val answers = listOf(
        listOf("1", "2", "3", "4", "5"),
        listOf("6", "7", "8", "9", "10"),
        listOf("11", "12", "13", "14", "15"),
        listOf("16", "17", "18", "19", "20"),
        listOf("21", "22", "23", "24", "666")
    )

    val rightAnswers = listOf("4", "6", "12", "17", "666")
}