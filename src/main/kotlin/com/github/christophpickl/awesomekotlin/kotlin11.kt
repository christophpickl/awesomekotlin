package com.github.christophpickl.awesomekotlin

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import java.awt.Color


//import kotlin.experimental.
// http://kotlinlang.org/docs/reference/whatsnew11.html

fun main(args: Array<String>) {
//    `coroutines samples`()
//    `type aliases types`()
//    `destructuring in lambdas`()
//    `bound callable references`()
    `dataclass hierarchy`()
//    `unused params underscore`()
//    `generic enum value access`()
//    `array like List instantiation functions`()
}


// =====================================================================================================================

// https://github.com/Kotlin/kotlinx.coroutines/blob/master/coroutines-guide.md#composing-suspending-functions
// http://kotlinlang.org/docs/tutorials/coroutines-basic-jvm.html

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
fun `type aliases functions`() {
    actOn { p, round -> println(round + " -> " + p) }
}

typealias PersonEater = (Person, String) -> Unit
data class Person(val name: String)

fun actOn(personEater: PersonEater) {
// same as: fun actOn(personEater: (Person, String) -> Unit) {
    listOf(Person("foo"), Person("bar")).forEach { personEater(it, "round 1") }
}

// =====================================================================================================================
fun `type aliases types`() {
    printPrices(mapOf(Person("foo") to 42))
}

typealias Euro = Int
typealias PersonPrices = Map<Person, Euro>
fun printPrices(prices: PersonPrices) {
    prices.mapValues { (key, value) -> println("$key -> $value") }
}


// =====================================================================================================================
fun `bound callable references`() {
    val numberRegex = "\\d+".toRegex()
    listOf("a", "1").filter(numberRegex::matches).forEach { println(it) }
}


// =====================================================================================================================
fun `destructuring in lambdas`() {
    val map = mapOf(1 to "one", 2 to "two")
    // before
    println(map.mapValues { entry ->
        val (key, value) = entry
        "$key -> $value!"
    })
    // now
    println(map.mapValues { (key, value) -> "$key -> $value!" })

}

// =====================================================================================================================
// https://github.com/Kotlin/KEEP/blob/master/proposals/data-class-inheritance.md
fun `dataclass hierarchy`() {
//    val userEvent = UserEvent("id", "userId")
//    val event = MyEvent("id")
//    println(userEvent)
//    println("event and userEvent are equal: " + (event == (userEvent as MyEvent))) // ==> false
    println(Triangle(1) == ColorTriangle(1, Color.BLACK))
}

// TODO i dont want MyEvent.id to be open, i just want to pass it in UserEvent through to MyEvent
open class MyEvent(open val id: String)

data class UserEvent(override val id: String, val userId: String) : MyEvent(id)

open class Triangle(open val x: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Triangle) return false
        return this.x == other.x
    }
}

data class ColorTriangle(override val x: Int, val color: Color) : Triangle(x)

// =====================================================================================================================
fun `unused params underscore`() {
    gimmeSomeMore { _, i, _ -> println("i just want ints: $i") }
}

fun gimmeSomeMore(gimme: (String, Int, Boolean) -> Unit) {
    // and underscores in ints, as known from java already ;)
    gimme("a", 1_000, true)
}

// =====================================================================================================================
// property types are now inferred
data class Person2(val name: String, val age: Int) {
    val isAdult get() = age >= 18 // Property type inferred to be 'Boolean'
}
/*
inlined properties:

public val <T> List<T>.lastIndex: Int
inline get() = this.size - 1
 */

// =====================================================================================================================
fun `local delegated properties`() {
    TODO()
}

// =====================================================================================================================
fun `interception of delegated property binding`() {
    TODO()
}
// =====================================================================================================================

enum class RGB { RED, GREEN, BLUE;

    companion object
}

inline fun <reified T : Enum<T>> printAllValues() {
    println(enumValues<T>().joinToString { it.name })
}

fun `generic enum value access`() {
    printAllValues<RGB>()
}


// Scope control for implicit receivers in DSLs
// rem instead of mod operator

// Standard library
// - String to number conversions
// - onEach
// - also(), takeIf() and takeUnless()
// - groupingBy()
// - Map.toMap() and Map.toMutableMap()
// - Map.minus(key)
// - minOf() and maxOf()

fun `array like List instantiation functions`() {
    val squares = List(10) { index -> index * index }
    println(squares)
    val mutable = MutableList(10) { 0 }
    println(mutable)
}
// - Map.getValue()
// - Abstract collections
// - Array manipulation functions
// -
// -  javax.script support
