package com.github.christophpickl.awesomekotlin.kotlin11


data class MyPerson(val name: String, val age: Int)
fun `destructuring in lambdas for data class`() {
    val p = MyPerson("foo", 21)

    // via componentX() in pre kotlin11
    val (myName, myage) = p

    // no with kotlin11:
    p.let { ( lamName, lamAge) -> println("$lamName is $lamAge") }
}

// =====================================================================================================================

fun `destructuring in lambdas for MAP`() {
    val map = mapOf(1 to "one", 2 to "two")

    // before
    println(map.mapValues { entry ->
        val (key, value) = entry
        "$key -> $value!"
    })

    // now
    println(map.mapValues { (key, value) -> "$key -> $value!" })

}


