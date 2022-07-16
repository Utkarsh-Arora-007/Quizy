package com.bitwisor.quizy.utils

data class QuizInfoOfUser(val quizName:String?="",
                          val quizId:String?="",
                          val fromDate:Int?=0,
                          val fromMonth:Int?=0,
                          val fromYear:Int?=0,
                          val toMonth:Int?=0,
                          val toDate:Int?=0,
                          val toYear:Int?=0,
                          val fromHr:Int?=0,
                          val fromMin:Int?=0,
                          val toHr:Int?=0,
                          val toMin:Int?=0,
                          val duration:String?="",
                          val numberOfQuestions:String?="",
                          val isExpired:Boolean = false
)