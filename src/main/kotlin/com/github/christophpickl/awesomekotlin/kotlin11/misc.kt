package com.github.christophpickl.awesomekotlin.kotlin11

// https://github.com/christophpickl/kotlin11slides
// http://kotlinlang.org/docs/reference/whatsnew11.html

fun main(args: Array<String>) {
    inlinedExtensionProperty()
}

// @JvmOverloads ?

// =====================================================================================================================


// =====================================================================================================================
// property types are now inferred

data class Person2(val name: String, val age: Int) {
    val isAdult get() = age >= 18 // Property type inferred to be 'Boolean'
}


// =====================================================================================================================
// inlined properties - http://kotlinlang.org/docs/reference/inline-functions.html#inline-properties-since-11
// they become inlined just as inlined functions are.
// You can also mark the entire property as inline - then the modifier is applied to both accessors.
//
// KEEP: https://github.com/Kotlin/KEEP/blob/master/proposals/inline-properties.md

val <T> List<T>.lastIndexInlined: Int
    inline get() = this.size - 1
val <T> List<T>.lastIndexNonInlined: Int
    get() = this.size - 1

inline var <T> List<T>.bothInlined: String
    get() = "this is inlined"
    set(value) { }

fun inlinedExtensionProperty() {
    val list = listOf("a", "b")
    println(list.lastIndexInlined)
    println(list.lastIndexNonInlined)
    println(list.bothInlined)
}

// =====================================================================================================================
fun `interception of delegated property binding`() {
    TODO()
}
