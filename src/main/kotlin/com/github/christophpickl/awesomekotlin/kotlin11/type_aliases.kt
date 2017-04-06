package com.github.christophpickl.awesomekotlin.kotlin11


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
