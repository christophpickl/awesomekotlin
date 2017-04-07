package com.github.christophpickl.awesomekotlin.kotlin11

// https://github.com/christophpickl/kotlin11slides


fun main(args: Array<String>) {
}

// @JvmOverloads ?

// =====================================================================================================================



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
fun `interception of delegated property binding`() {
    TODO()
}
