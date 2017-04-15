package com.github.christophpickl.awesomekotlin.kotlin11

import java.util.Arrays


fun main(args: Array<String>) {
    `array manipulation`()
}

// list "comprehension"
// =====================================================================================================================
fun `list comprehension like`() {

    // array comprehension was already existing before K11, but not for lists
    println(IntArray(4) { it * 2 }.toList()) // [0, 2, 4, 6]

    // public inline fun <T> List(size: Int, init: (index: Int) -> T): List<T>
    println(List(4) { it * 2 })
    // public inline fun <T> MutableList(size: Int, init: (index: Int) -> T): MutableList<T>
    println(MutableList(4) { it * 2 })
}


// ON EACH
// =====================================================================================================================
// public inline fun <T, C : Iterable<T>> C.onEach(action: (T) -> Unit): C
fun `onEach is like forEach but returns object again`() {
    val list: Iterable<String> = listOf("foobar", "foobaz", "bar")

    list.forEach {} // returns Unit

    list
            .filter { it.endsWith("bar") }
            .onEach { println("Found item: $it") } // returns Iterable<String>, therefor usable in chain
            .forEach { /* finally operate on them */ }
}

// - groupingBy()
// =====================================================================================================================

// public inline fun <T, K> Iterable<T>.groupingBy(crossinline keySelector: (T) -> K): Grouping<T, K>
fun `groupingBy group collection by key and fold`() {
    val words = "one two three four five six seven eight nine ten".split(' ')

    // public fun <T, K> Grouping<T, K>.eachCount(): Map<K, Int> ... does a fold internally
    val frequenciesWithGroupingBy = words.groupingBy(String::first).eachCount()
    println("Frequencies with groupingBy: $frequenciesWithGroupingBy") // o=1, t=3, f=2, s=2, e=1, n=1

    // old way: groupBy and mapValues (create intermediate map which groupingBy does not)
    val frequenciesWIthGroupBy = words.groupBy { it.first() }.mapValues { (_, list) -> list.size }
    println("Frequencies with groupBy: $frequenciesWIthGroupBy")
}

// - Map.toMap() and Map.toMutableMap() // i already implemented that myself, didn't i?! ;)
// =====================================================================================================================
fun `toMap toMutableMap`() {
    val map = mapOf(1 to "1")
//    map += 2 to "2" ... nope, immutable

    val map2 = map.toMutableMap().apply { put(2, "2") }
}

fun `toMap do nasty stuff`() {
    val map: MutableMap<String, String> = mutableMapOf("a" to "b")

    `toMap i believe you`(map, { map.put("c", "d") })

}

fun `toMap i believe you`(seemsToBeImmutable: Map<String, String>, someoneElse: () -> Unit) {
    println("map before: $seemsToBeImmutable") // {a=b}
    someoneElse()
    println("map after: $seemsToBeImmutable") // {a=b, c=d}

    val betterToUseCopy = seemsToBeImmutable.toMap() // results in: LinkedHashMap(this)

    // kotlin's default toMap() is for immutable ones (optimizeReadOnlyMap), but nothing for mutable ones?!
    listOf(1 to "einz").toMap() == mapOf(1 to "einz")
}

// my implementation :)
fun <K, V> Iterable<Pair<K, V>>.toMutableMap(): MutableMap<K, V> {
    val immutableMap = toMap()
    val map = HashMap<K, V>(immutableMap.size)
    map.putAll(immutableMap)
    return map
}

// - Map.minus(key)
// =====================================================================================================================
fun `map minus`() {
    val map = mapOf("key1" to "value1")
    println("map: $map") // {key1=value1}

    val map2 = map + Pair("key2", "value2")
//    var map2 = map
//    map2 += Pair("key2", "value2")
    println("map2: $map2") // {key1=value1, key2=value2}

    // until K11 there was no "-" support
    val minussed = map2 - "key1" // minus uses minusAssign which looses the previous value associated/null which the underlying remove() returns :(
    println("minussed: $minussed") // {key2=value2}
}

// - Map.getValue(), withDefault
// =====================================================================================================================
fun `map getValue, withDefault`() {
    val map = mapOf("key1" to 1)
    println(map["key1"]) // 1
    println(map["key2"]) // null

    map.getValue("key1") // 1
//    map.getValue("two") // throws NoSuchElementException: Key two is missing in the map.
    // would be nice, if there was a custom exception type which got the key as an accessible field (string parsing, really?!)

    val mapWithDefault = map.withDefault { key -> "!$key!" }
    println(mapWithDefault.getValue("key2")) // !key2!
}


// - Array manipulation functions
// =====================================================================================================================
fun `array manipulation`() {
    // new method: content[Deep](Equals|HashCode|ToString)
    // actually just delegation to: java.util.Arrays.*

    println("TO STRING")
    println("flat:")
    val array = arrayOf("a", "b")
    println(array.toString()) // [Ljava.lang.String;@1be6f5c3
    println(array.contentToString()) // [a, b]
    println(array.contentDeepToString()) // [a, b]

    println("deep:")
    val arrayDeep = arrayOf(arrayOf("a"), arrayOf("b", "c"))
    println(arrayDeep.toString()) // [Ljava.lang.String;@1be6f5c3
    println(arrayDeep.contentToString()) // [[Ljava.lang.String;@38af3868, [Ljava.lang.String;@77459877]
    println(arrayDeep.contentDeepToString()) // [[a], [b, c]]

    println("EQUALS")
    println("flat:")
    val array1 = arrayOf("a")
    val array2 = arrayOf("a")
    @Suppress("ReplaceArrayEqualityOpWithArraysEquals")
    println(array1 == array2) // false
    println(Arrays.equals(array1, array2)) // true
    println(array1.contentEquals(array2)) // true
    println(array1.contentDeepEquals(array2)) // true

    println("deep:")
    val array1Deep = arrayOf(arrayOf("a"))
    val array2Deep = arrayOf(arrayOf("a"))
    println(array1Deep.contentEquals(array2Deep)) // false
    println(array1Deep.contentDeepEquals(array2Deep)) // true

    println("HASH CODE")
    println(array.hashCode()) // 468121027
    println(array.contentHashCode()) // 4066
    println(array.contentDeepHashCode()) // 4066
    println(arrayDeep.contentHashCode()) // 1417507024
    println(arrayDeep.contentDeepHashCode()) // 9027
}


// - Abstract collections
// =====================================================================================================================
fun `abstract collection handy base classes`() {
    // Abstract[Mutable](Collection|List|Set|Map)

    val listWithOneElement = object : AbstractList<String>() { // skeletal implementation of the [List] interface
        override val size: Int
            get() = 1
        override fun get(index: Int): String {
            return "always foo"
        }
    }
}
