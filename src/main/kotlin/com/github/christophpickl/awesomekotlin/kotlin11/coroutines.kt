package com.github.christophpickl.awesomekotlin.kotlin11

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

// http://kotlinlang.org/docs/reference/coroutines.html
// http://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html
// https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#composing-suspending-functions
// http://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html

fun main(args: Array<String>) {
    val msNeeded = measureTimeMillis {
        //        `andreys simple sample`()
//        `work it hard with coroutines`()
        `work it hard with threads`()
    }
    println("$msNeeded ms needed")
}

// =====================================================================================================================
fun `andreys simple sample`() {
    val a = async(CommonPool) {
        42
    }
    val b = async(CommonPool) {
        delay(1000)
        a.await() * 2
    }
    runBlocking {
        println(a.await())
        println(b.await())
    }
}

// =====================================================================================================================
fun `work it hard with coroutines`() = runBlocking {
    println("start ...")
    val jobs = List(100_000) {
        async(CommonPool) {
            delay(1_000L)
            1
        }
    }
    println(jobs.sumBy { it.await() })
}

fun `work it hard with threads`() {
    for (i in 1..100_000) {
        // Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
        thread(start = true) {
            Thread.sleep(1000)
        }
    }
}

// =====================================================================================================================
fun `coroutines samples`() {
//    async {
//        thinkLong()
//    }
    println("Launching ... (thread=${Thread.currentThread().name})") // main
    runBlocking {
        //    async(CommonPool) {
        // launch(CommonPool) { .. does NOT block
        println("Start think long")
        thinkLong()
        println("Stop think long")
    }

    println("Launching ... DONE")
}

suspend fun thinkLong(): Int {
    println("thinkLong() START (thread=${Thread.currentThread().name})") // ForkJoinPool.commonPool-worker-1

    delay(2000)
    println("thinkLong() DONE")
    return 42
}

// =====================================================================================================================
