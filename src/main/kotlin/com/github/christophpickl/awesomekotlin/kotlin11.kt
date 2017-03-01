package com.github.christophpickl.awesomekotlin

// type aliases


data class Person(val name: String)

// typealias OscarWinners = Map<String, String>
typealias PersonEater = (Person, String) -> Unit

fun main(args: Array<String>) {
    actOn { p, round -> println(round + " -> " + p) }
}

fun actOn(personEater: (Person, String) -> Unit) {
    listOf(Person("foo"), Person("bar")).forEach { personEater(it, "round 1") }
}

// bound callable references
// destructuring in lambdas
