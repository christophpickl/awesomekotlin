package com.github.christophpickl.awesomekotlin.kotlin11


fun main(args: Array<String>) {
    val rgbs: Array<RGB> = enumValues()
    println(enumValues<RGB>().joinToString { it.name }) // RED, GREEN, BLUE

    val red: RGB = enumValueOf("RED")
    println(enumValueOf<RGB>("RED")) // RED
//    println(enumValueOf<RGB>("red")) // fails with an IllegalArgumentException
}

enum class RGB { RED, GREEN, BLUE }

