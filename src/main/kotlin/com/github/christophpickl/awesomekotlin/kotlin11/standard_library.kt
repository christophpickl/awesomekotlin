package com.github.christophpickl.awesomekotlin.kotlin11

import java.io.File
import java.math.BigInteger


fun main(args: Array<String>) {
    `takeIf highlighter`()
}



// NUMBER CONVERSION
// =====================================================================================================================

// public fun String.toIntOrNull(): Int? = toIntOrNull(radix = 10)
fun `number conversion with *OrNull() instead throwing exception`() {
    val someInputString = "foobar"

    val someInputInt = someInputString.toIntOrNull() ?: 42
    // same available for: Byte, Short, Long, Double

    someInputString.toInt() // will throw usual NumberFormatException
    // delegates to java.lang.Integer.parseInt

}

// public inline fun Int.toString(radix: Int): String = java.lang.Integer.toString(this, checkRadix(radix))
fun `conversion supports radix`() {
    val radix = 2 // 2..36

    println(42.toString(radix)) // radix2 = 101010, radix16 = 2a
    println("11".toIntOrNull(radix)) // radix2 = 3, radix16 = 17
}

// - also(), takeIf() and takeUnless()
// =====================================================================================================================

// public inline fun <T> T.also(block: (T) -> Unit): T { block(this); return this }
// public inline fun <T> T.apply(block: T.() -> Unit): T { block(); return this }
fun `also is like apply but`() {
    val aWithApply = "a".apply {
        "b".apply {
            println("APPLY - this: " + this) // "b"

            @Suppress("LABEL_NAME_CLASH")
            println("APPLY - this@apply: " + this@apply) // "b", label name clash :-/
        }
    }

    println()

    val aWithAlso = "a".also { outer ->
        "b".also {
            println("ALSO - it: $it") // "b"
            println("ALSO - outer: $outer") // "a"
        }
    }
}

// public inline fun <T> T.takeIf(predicate: (T) -> Boolean): T? = if (predicate(this)) this else null
fun `takeIf - is like filter but acts on single value and returns null if not matched`(): Boolean {
    val path = "somePath"

    val file = File(path)
    if (!file.exists()) {
        return false
    }

    // takeIf works well with elvis operator
    val file2 = File(path).takeIf(File::exists) ?: return false

    println("$file2 exists")
    return true
}

fun `takeIf highlighter`() {
    val input = "Kotlin rules"
    val keyword = "in"

    val index = input.indexOf(keyword).takeIf { it >= 0 } ?: error("keyword not found")
    //  index = input.indexOf(keyword).takeUnless { it < 0 } ?: error("keyword not found")

    // do something with index of keyword in input string, given that it's found
    println("'$keyword' was found in '$input'")
    println(input)
    println(" ".repeat(index) + "^")
}

// public inline fun <T> T.takeUnless(predicate: (T) -> Boolean): T? = if (!predicate(this)) this else null
fun `takeUnless is same as takeIf but inverted`() {
    val file = File("")
    if (file.exists()) {
        return
    }
    // process non existing file


    val file2 = File("").takeUnless(File::exists) ?: return
    // val file2 = File("").takeIf(File::notExists) ... notExists does not exist in std lib :-/

//    val result = string.takeUnless(String::isEmpty)

}

// - minOf() and maxOf()
// =====================================================================================================================
fun `minOf and maxOf find lowest or greatest of 2 or 3 values - primitive numbers, Comparable, or pass a comparator`() {
    val list1 = listOf("a", "b")
    val list2 = listOf("x", "y", "z")
    println(minOf(list1.size, list2.size))
    // this looks pretty similar
    println(Math.min(list1.size, list2.size))

    val list3 = listOf("one")
    println(minOf(list1.size, list2.size, list3.size))
    // now here it becomes messy
    println(Math.min(list1.size, Math.min(list2.size, list3.size)))

    // but minOf/maxOf do not support 4+ values :(  hey, i could implement this easily! =)

    // public fun <T> maxOf(a: T, b: T, comparator: Comparator<in T>): T
    val longestList = maxOf(list1, list2, compareBy { it.size })
}

// mod => rem
// =====================================================================================================================
fun `mod renamed to rem`() {
    // https://youtrack.jetbrains.com/issue/KT-14650 ... mod function on integral types is inconsistent with BigInteger.mod

    // there is even a paper about it, euclidean rings FTW ;)
    // https://www.microsoft.com/en-us/research/wp-content/uploads/2016/02/divmodnote-letter.pdf

    val minus3 = BigInteger.valueOf(-3)
    val plus5 = BigInteger.valueOf(5)

    println("mod BigInt: " + minus3.mod(plus5)) // prints "2" which is mathematically "correct"
    println("mod Int: " + minus3.toInt().mod(plus5.toInt())) // prints "-3" which is mathematically "incorrect", therefor deprecated
    println()
    println("rem BigInt: " + minus3.rem(plus5)) // -3
    println("rem Int: " + minus3.toInt().rem(plus5.toInt())) // -3
}
