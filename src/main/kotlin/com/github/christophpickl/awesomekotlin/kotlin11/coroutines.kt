package com.github.christophpickl.awesomekotlin.kotlin11

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlin.concurrent.thread

// http://kotlinlang.org/docs/reference/coroutines.html
// http://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html
// https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#composing-suspending-functions
// http://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html

/*
* enable async operations; even more lightweight than threads
	- they scale way better than threads, very cheap
* also named: async/await/yield, fibers, [stackless] continuation
* very low level, designed so that frameworks can build upon it
    - create your own async/await, yield
* offer a much nicer syntax, look like regular function invocations, not leading to "nested ladder pattern"
 */

fun main(args: Array<String>) {
    imageMain()
}

// run 100.000 times with coroutines VS threads
// =====================================================================================================================

fun `run 100_000 thread`() {
    for (i in 1..100_000) {
        // OutOfMemoryError: unable to create new native thread
        thread(start = true) {
            Thread.sleep(1000)
        }
    }
}

fun `run 100_000 coroutines`() {
    val jobs = List(100_000) {
        async(CommonPool) {
            delay(1000L)
            1
        }
    }
    runBlocking {
        // bridge async world
        println(jobs.sumBy { it.await() })
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
    return 1
}

// =====================================================================================================================

fun `greet blocking`() = runBlocking {
    println("start")
//    greet("foo")
//    greet("bar")
//    greet("World")
    listOf("foo", "bar", "World").map { name ->
        async(CommonPool) {
            greet(name)
        }
    }.map { it.await() }.joinToString().println()
    println("end")
}

fun `greet blocking2`() = runBlocking {
    listOf("foo", "bar", "World").map { name ->
        async(CommonPool) {
            greet(name)
        }
    }.map { it.await() }.joinToString()
}

fun Any.println() {
    println(this)
}

suspend fun greet(name: String): String {
    val ms = (Math.random() * 3000.0).toLong() + 2000
    println("greet($name) waits for ${ms}ms")
    delay(ms)
    println("greeting $name DONE.")
    return "Hello $name!"
}

// =====================================================================================================================

var imageSeq = 0
fun imageMain() {
    // OLD_imageMain()
    NEW_imageMain()
}

fun OLD_imageMain() {
    OLD_loadImage { image ->
        println("load done: $image")
        OLD_processImage(image) { imageX ->
            println("processing done: $imageX")
        }
    }
}
fun OLD_loadImage(callback: (String) -> Unit) {
    val id = ++imageSeq
    sleepRand()
    callback("image$id")
}
fun OLD_processImage(id: String, callback: (String)->Unit) {
    sleepRand()
    callback("X$id")
}
// ---------------------------------------------------------------

fun NEW_imageMain() = runBlocking {
    println("load image ...")
    val image = NEW_loadImage()
    println("loaded: $image; processing ...")
    val imageX = NEW_processImage(image)
    println("processing done: $imageX")
}

suspend fun NEW_loadImage(): String {
    val id = ++imageSeq
    delayRand()
    return "image$id"
}
suspend fun NEW_processImage(id: String): String {
    delayRand()
    return "X$id"
}

private suspend fun delayRand() = delay((Math.random() * 500.0).toLong() + 500)
private fun sleepRand() = Thread.sleep((Math.random() * 500.0).toLong() + 500)

