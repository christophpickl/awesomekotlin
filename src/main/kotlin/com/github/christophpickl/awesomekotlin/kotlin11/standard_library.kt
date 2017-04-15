package com.github.christophpickl.awesomekotlin.kotlin11


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

// - Map.getValue()
// - Abstract collections
// - Array manipulation functions
// -
// -  javax.script support
/*

* string to number conversion: val port = System.getenv("PORT")?.toIntOrNull() ?: 80
* onEach()
* also(), takeIf(), takeUnless()
* groupingBy()
* toMap(), toMutableMap() // i already implemented that myself, didn't i?! ;)
* minus operator for map
* minOf(), maxOf()
* list "comprehension": val squares = List(10) { index -> index * index }; val mutable = MutableList(10) { 0 }
* Map.getValue(), withDefault
* Array manipulation functions: contentEquals, contentHashCode, contentToString  (or deep)

 */
// ENUM
// =====================================================================================================================


enum class RGB { RED, GREEN, BLUE }

inline fun <reified T : Enum<T>> printAllValues() {
    println(enumValues<T>().joinToString { it.name })
}

fun `generic enum value access`() {
    printAllValues<RGB>()
    enumValueOf<RGB>("RED")
}
