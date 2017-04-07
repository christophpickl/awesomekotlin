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
