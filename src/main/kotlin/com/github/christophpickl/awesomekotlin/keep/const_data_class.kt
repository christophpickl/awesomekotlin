package com.github.christophpickl.awesomekotlin.keep

import kotlin.system.measureTimeMillis

// https://github.com/SalomonBrys/KEEP/blob/const-data-class/proposals/const-data-class.md

const val ITERATIONS = 10_000_000

//const val ITERATIONS = 10_000_000
//Standard:  746, 641, 503
//Optimized: 374, 310, 257

//const val ITERATIONS = 1_000_000
//Standard:  122, 83, 75, 83, 77, 109
//Optimized:  82, 64, 80, 97, 89,  72

//const val ITERATIONS = 100_000
//Standard:  17, 16, 24, 50, 18
//Optimized: 17, 26, 21, 18, 31

fun main(args: Array<String>) {
    val person = Person("Salomon", "BRYS")
    val s = HashSet<Standard>()
    val o = HashSet<Optimized>()
    println("Building standard map ...")
    (0..ITERATIONS).mapTo(s) { Standard(it, person) }
    println("Building optimized map ...")
    (0..ITERATIONS).mapTo(o) { Optimized(it, person) }

    println("Measuring ...")
    measureTimeMillis {
        s.forEach {
            s.contains(it)
        }
    }.let { println("Standard: ${it}ms") }

    measureTimeMillis {
        o.forEach {
            o.contains(it)
        }
    }.let { println("Optimized: ${it}ms") }
}

data class Person(val firstName: String, val lastName: String)

data class Optimized(val id: Int, val person: Person) {
    @Transient @Volatile
    private var _hashcode = 0;
    override fun hashCode(): Int{
        if (_hashcode == 0)
            _hashcode = 31 * id + person.hashCode()
        return _hashcode
    }
    override fun equals(other: Any?): Boolean{
        if (other !is Optimized) return false
        if (this === other) return true

        if (hashCode() != other.hashCode()) return false
        if (id != other.id) return false
        if (person != other.person) return false

        return true
    }
}

data class Standard(val id: Int, val person : Person)


inline fun time(name: String, f: () -> Unit) {
    val start = System.currentTimeMillis()
    f()
    val time = System.currentTimeMillis() - start
    println("$name: $time")
}
