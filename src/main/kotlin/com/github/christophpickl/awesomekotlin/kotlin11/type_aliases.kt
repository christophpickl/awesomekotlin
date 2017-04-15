package com.github.christophpickl.awesomekotlin.kotlin11

data class ButtonEvent(val message: String)

// http://kotlinlang.org/docs/reference/type-aliases.html
// https://github.com/Kotlin/KEEP/blob/master/proposals/type-aliases.md

// typealias must be toplevel thing (no nested or local ones allowed!)
//class Event<T> {
//    typealias EventListener = (T) -> Unit
//    private val observers = ArrayList<EventListener>
//}

class ButtonPre11 {
    val listeners = ArrayList<(ButtonEvent) -> Unit>()
    fun subscribe(listener : (ButtonEvent) -> Unit): ButtonPre11 {
        listeners += listener
        return this
    }
    fun fireEvent() = ButtonEvent("pre 11").apply { listeners.forEach { it.invoke(this) } }
}

typealias ButtonListener = (ButtonEvent) -> Unit
class ButtonWith11 {
    val listeners = mutableListOf<ButtonListener>()
    fun subscribe(listener : ButtonListener): ButtonWith11 {
        listeners += listener
        return this
    }
    fun fireEvent() = ButtonEvent("with 11").apply { listeners.forEach { it.invoke(this) } }
}

fun main(args: Array<String>) {
    val myListener = { event: ButtonEvent -> println("Got event: $event")}
    ButtonPre11().subscribe(myListener).fireEvent()
    ButtonWith11().subscribe(myListener).fireEvent()
}

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
