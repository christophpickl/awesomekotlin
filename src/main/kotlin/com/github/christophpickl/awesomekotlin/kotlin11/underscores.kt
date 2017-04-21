package com.github.christophpickl.awesomekotlin.kotlin11


// =====================================================================================================================

fun `unused sample`() {
    mapOf(1 to "one").map { (k, _) -> k }
}


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
