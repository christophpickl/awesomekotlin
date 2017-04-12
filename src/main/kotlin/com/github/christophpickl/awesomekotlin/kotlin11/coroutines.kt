package com.github.christophpickl.awesomekotlin.kotlin11

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking

// http://kotlinlang.org/docs/reference/coroutines.html
// http://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html
// https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#composing-suspending-functions
// http://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html

fun main(args: Array<String>) {

}

fun `work it ten thousand times`() {
    async(CommonPool) {

    }
}


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
