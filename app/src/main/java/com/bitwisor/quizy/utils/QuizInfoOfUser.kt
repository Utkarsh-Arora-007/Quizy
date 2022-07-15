package com.bitwisor.quizy.utils

data class QuizInfoOfUser(val quizName:String?="",
                          val quizId:String?="",
                          val currDate:String?="",
                          val currMonth:String?="",
                          val currYear:String?="",
                          val currTimeHr:String?="",
                          val currTimeMin:String?="",
                          val endDate:String?="",
                          val endMonth:String?="",
                          val endYear:String?="",
                          val endTimeHr:String?="",
                          val endTimeMin:String?="",
                          val duration:String?="",
                          val numberOfQuestions:String?="",
                          val isExpired:Boolean = false
)