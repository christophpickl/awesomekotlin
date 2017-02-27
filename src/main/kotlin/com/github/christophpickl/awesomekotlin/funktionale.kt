package com.github.christophpickl.awesomekotlin

import org.funktionale.collections.destructured
import org.funktionale.collections.prependTo
import org.funktionale.collections.tail
import org.funktionale.composition.compose
import org.funktionale.composition.forwardCompose
import org.funktionale.currying.curried
import org.funktionale.currying.uncurried
import org.funktionale.either.Disjunction
import org.funktionale.either.Either
import org.funktionale.either.eitherTry
import org.funktionale.memoization.memoize
import org.funktionale.option.*
import org.funktionale.option.Option.Some
import org.funktionale.partials.invoke
import org.funktionale.reverse.reverse
import org.funktionale.utils.constant
import org.funktionale.utils.identity
import org.funktionale.validation.Validation

// https://github.com/MarioAriasC/funKTionale

fun main(args: Array<String>) {
    memoizee()
}


fun functionComposition() {
    val add5 = { i: Int -> i + 5 }
    val multiplyBy2 = { i: Int -> i * 2 }

    val add5andMultiplyBy2 = add5 forwardCompose multiplyBy2
    println("add5andMultiplyBy2(2): " + add5andMultiplyBy2(2)) // (2 + 5) * 2 = 14

    val multiplyBy2andAdd5 = add5 compose multiplyBy2
    println("multiplyBy2andAdd5(2): " + multiplyBy2andAdd5(2)) // (2 * 2) + 5 = 9
}

fun partialApplication() {
    val prefixAndPostfix: (String, String, String) -> String = { prefix: String, x: String, postfix: String -> "$prefix$x$postfix" }

    val prefixAndBang: (String, String) -> String = prefixAndPostfix(p3 = "!")
//    val prefixAndBang: (String, String) -> String = prefixAndPostfix.partially3("!")

    val hello: (String) -> String = prefixAndBang(p1 = "Hello, ")
//    val hello: (String) -> String = prefixAndBang.partially1("Hello, ")

    println("hello(\"awesome\"): " + hello("awesome")) // "Hello, awesome!"
}

fun currying() {
    val sum2ints = { x: Int, y: Int -> x + y }
    val curried: (Int) -> (Int) -> Int = sum2ints.curried()
    println("curried(2)(4): " + curried(2)(4)) // 6

    val add5 = curried(5)
    println("add5(7): " + add5(7)) // 12

    println("curried.uncurried()(2, 4): " + curried.uncurried()(2, 4)) // 6
}

fun reverse() {
    val f = { s: String, i: Int, b: Boolean -> /* */ }
    f("a", 1, true)

    val r: (Boolean, Int, String) -> Unit = f.reverse()
    r(true, 1, "a")
}

fun option() {
    val nullableString: String? = null
    val optionString: Option<String> = nullableString.toOption()

    val aString = when (optionString) {
        is Some<String> -> optionString.get()
        else -> "Default String"
    }

    val bString = if (optionString.isEmpty()) {
        optionString.get()
    } else {
        "Default String"
    }

    val cString = optionString.fold(ifEmpty = { "Default String" }, f = { it })

    val dString = optionString.getOrElse { "Default String" }


    val map = mapOf(1 to "uno", 2 to "dos")
    map.option[1] // => Some("uno")
    map.option[3] // => None

    val l = listOf(1, 2, 3, 4, 5, 6)
    l.firstOption() // => Some(1)
    l.firstOption { it > 2 } // => Some(3)
}

fun either() {
    fun problematicEitherFunction(): Either<Exception, String> {
        return Either.right("result")
    }

    val result = problematicEitherFunction()
    when (result) {
        is Either.Right<Exception, String> -> result.right().get()
        is Either.Left<Exception, String> -> result.left().get().printStackTrace()
    }

    if (result.isRight()) {
        result.right().get()
    } else {
        //manage the error
    }

    result.fold({ e -> /*Manage the error*/ }) { string ->
        // process string
    }


    val (e, string) = problematicEitherFunction()

    fun problematicFunction() = "gladly no exception"

    val v: Either<Throwable, String> = eitherTry {
        problematicFunction()
    }
}

fun validation() {
    val e1 = Disjunction.right(1)
    val e2 = Disjunction.left("Not a number")
    val e3 = Disjunction.right(3)
    val e4 = Disjunction.left("Division by zero")

    val validation = Validation(e1, e2, e3, e4)
    validation.hasFailures //true
    validation.failures // listOf("Not a number", "Division by zero")
}

fun identityy() {
    val add5 = { i: Int -> i + 5 }
    val multiplyBy2 = { i: Int -> i * 2 }

    fun applyTwoFunctions(i: Int, firstFunction: (Int) -> Int, secondFunction: (Int) -> Int): Int {
        val x = firstFunction(i)
        return secondFunction(x)
    }

    applyTwoFunctions(2, add5, multiplyBy2) // => 14
    applyTwoFunctions(2, add5, identity()) // => 7
    applyTwoFunctions(2, identity(), identity()) // 2
}

fun constantt() {
    val list = arrayListOf("foo", "bar", "baz")
    val to7: (String) -> Int = constant(7) // Transform any String to an int of value 7

    list.map(to7) // 7, 7, 7
}

fun memoizee() {
    fun timeElapsed(body: () -> Unit): String {
        val start = System.currentTimeMillis()
        body()
        val end = System.currentTimeMillis()
        return (end - start).toString() + "ms"
    }

    var fib: (Long) -> Long = { it } // Declared ahead to be used inside recursively
    fib = { n: Long ->
        if (n < 2) n else fib(n - 1) + fib(n - 2)
    }
    var memFib: (Long) -> Long = { it }
    memFib = { n: Long ->
        if (n < 2) n else memFib(n - 1) + memFib(n - 2)
    }.memoize()

    println(timeElapsed { fib(40) }) // 2613ms
    println(timeElapsed { memFib(40) })  // 2ms
}

fun collections() {
    listOf(1, 2, 3).tail() // => listOf(2, 3)
    1 prependTo listOf(2, 3) // => listOf(1, 2, 3)

    val (head, tail) = listOf(1, 2, 3).destructured()
    head // => 1
    tail // => listOf(2, 3)
}
