package com.github.christophpickl.awesomekotlin.kotlin11

// see: https://github.com/christophpickl/kotlin11slides

//import kotlin.experimental.
// http://kotlinlang.org/docs/reference/whatsnew11.html

fun main(args: Array<String>) {
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
