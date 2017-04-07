package com.github.christophpickl.awesomekotlin.kotlin11

fun main(args: Array<String>) {
    // lazy delegation one of the most used delegates
    // pre kotlin11 not possible for variables (only fields)

    val lazilyFetched by lazy { timeConsumingFetch() }
    if (someCondition()) {
        println("Accessing lazy variable.")
        lazilyFetched.length
    } else {
        println("NOT accessing lazy variable.")
    }
}

fun timeConsumingFetch(): String {
    println("timeConsumingFetch() called")
    return "takes some time"
}
fun someCondition() = false
