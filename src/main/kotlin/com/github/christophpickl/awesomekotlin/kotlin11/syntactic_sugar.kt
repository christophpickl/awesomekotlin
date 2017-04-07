package com.github.christophpickl.awesomekotlin.kotlin11


// UNUSED LAMBDA VARIABLES
// =====================================================================================================================

fun `unused params in lambdas underscore`() {
    numberWithUnderscore { _, i, _ -> println("i just want ints: $i") }
}

// UNDERSCORE IN NUMBERS
// =====================================================================================================================

fun numberWithUnderscore(gimme: (String, Int, Boolean) -> Unit) {
    // and underscores in ints, as known from java already ;)
    gimme("a", 1_000, true)
}

// =====================================================================================================================


fun `array like List instantiation functions`() {
    val squares = List(10) { index -> index * index }
    println(squares)

    val mutable = MutableList(10) { 0 }
    println(mutable)
}
