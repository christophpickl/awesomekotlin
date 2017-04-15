package com.github.christophpickl.awesomekotlin.kotlin11


// UNUSED LAMBDA VARIABLES
// =====================================================================================================================

fun `unused params in lambdas underscore`() {
    gimmeSome { _, i, _ -> println("i just want ints: $i") }
}

fun gimmeSome(gimme: (String, Int, Boolean) -> Unit) {}

// UNDERSCORES IN NUMBERS
// =====================================================================================================================

fun `underscores in numeric literals`() {
    // add underscores in ints, as known from java already ;)
    val decimal = 1_000_000
    val hexaDecimal= 0x11_FF_22_BB
    val bytes = 0b00000000_11111111
}

// LIST INSTANTIATION
// =====================================================================================================================


fun `array like List instantiation functions`() {
    val squares = List(10) { index -> index * index }
    println(squares)

    val mutable = MutableList(10) { 0 }
    println(mutable)
}
