package com.github.christophpickl.awesomekotlin.kotlin11

import java.awt.Color

// data classes can now inherit other classes (and sealed subclasses can be outside of the parent definition)
sealed class Expression
object Operator : Expression()
data class Operand(val symbol: String) : Expression()

// =====================================================================================================================

// https://github.com/Kotlin/KEEP/blob/master/proposals/data-class-inheritance.md
fun `dataclass hierarchy`() {
//    val userEvent = UserEvent("id", "userId")
//    val event = MyEvent("id")
//    println(userEvent)
//    println("event and userEvent are equal: " + (event == (userEvent as MyEvent))) // ==> false
    println(Triangle(1) == ColorTriangle(1, Color.BLACK))
}

// i dont want MyEvent.id to be open, i just want to pass it in UserEvent through to MyEvent!!! :-/
open class MyEvent(open val id: String)

data class UserEvent(override val id: String, val userId: String) : MyEvent(id)

// lombok can do it... :(

open class Triangle(open val x: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Triangle) return false
        return this.x == other.x
    }
}

data class ColorTriangle(override val x: Int, val color: Color) : Triangle(x)
