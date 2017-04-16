package com.github.christophpickl.awesomekotlin.kotlin11

import kotlin.reflect.jvm.javaMethod


// You can now use the :: operator to get a member reference pointing to a method or property of a specific object instance.
// Previously this could only be expressed with a lambda.
//
// see: http://kotlinlang.org/docs/reference/reflection.html#bound-function-and-property-references-since-11
// see: https://github.com/Kotlin/KEEP/blob/master/proposals/bound-callable-references.md
// https://github.com/Kotlin/KEEP/issues/5

fun main(args: Array<String>) {
    `different types but should still reference same method`()
}

// * List<String>>::new ??
// * inlining is supported
// * Even now a function reference has an imaginary (non-existing at runtime) KFunctionN<P1, ..., PN, R> type

// * different semantics in here:
//  caller.call { computeExpensiveFoo().bar }
//  caller.call(computeExpensiveFoo()::bar)
// * nicer code:
//  strings.compareBy { s, t -> myComparator.compareTo(s, t) }
//  strings.compareBy(myComparator::compareTo)

// (One might think that "callable reference" means a reference that is itself callable, instead of a reference to something callable.
// This is a slightly unfortunate ambiguity that we're aware of but do not consider it a big problem, at least because both definitions are technically correct.)

class Foo {
    fun bar() {
        println("Foo.bar() invoked")
    }

    fun barWithParam(param: Int) {
        println("Foo.barWithParam(param=$param) invoked")
    }
}

fun `bound callable references`() {
    val numberRegex = "\\d+".toRegex()

    // most verbosest ;)
    listOf("a", "1").filter { numberRegex.matches(it) }.forEach { println(it) }

    // pre kotlin11
    listOf("a", "1").filter { numberRegex.matches(it) }.forEach(::println)

    // with kotlin11
    listOf("a", "1").filter(numberRegex::matches).forEach(::println)
}

fun `invoke refs`() {
    val foo = Foo()
    val barRef = Foo::bar
    val barWithParamRef = Foo::barWithParam

    println(barRef.javaMethod) // public final void com.github.christophpickl.awesomekotlin.kotlin11.Foo.bar()
    println(barRef) // fun com.github.christophpickl.awesomekotlin.kotlin11.Foo.bar(): kotlin.Unit
    barRef.invoke(foo)

    println(barWithParamRef) // fun com.github.christophpickl.awesomekotlin.kotlin11.Foo.barWithParam(kotlin.Int): kotlin.Unit
    barWithParamRef.invoke(foo, 42)

    // partial application is not covered by kotlin, but funKtionale to the win ;)
}

fun `references are equal but not same`() {
    val barRef1 = Foo::bar
    val barRef2 = Foo::bar
    println(barRef1 == barRef2) // true
    println(barRef1 === barRef2) // false
}

fun `bind nullable callable references not yet supported`() {
    val foo: Foo = Foo()
    foo::bar
    foo::class

    val maybeFoo: Foo? = null
    // maybeFoo?::bar ... compile error: "LHS of callable reference matches expression syntax reserved for future releases" :(
    val maybeRef = maybeFoo?.let { it::bar } // we need to do it this way...
    // It is an error if the LHS expression has nullable type and the resolved member is not an extension to nullable type.

}

fun `inferr bound types`() {
    // println(listOf("a", "").filter { it.isNotEmpty() }) // we didnt do this in any kotlin version ;)
    println(listOf("a", "").filter(String::isNotEmpty))
    // println(listOf("a", "").filter(::isNotEmpty)) // inference here is not yet supported :(
    // String::isEmpty would be inferred for arguments of type String.() -> Boolean or (String) -> Boolean
}

// =====================================================================================================================

class Event<T> {

    private val observers = ArrayList<(T) -> Unit>()

    operator fun plusAssign(observer: (T) -> Unit) {
        println("add: $observer")
        observers += observer
    }

    operator fun minusAssign(observer: (T) -> Unit) {
        println("remove: $observer")
        observers -= observer
    }

    operator fun invoke(value: T) {
        observers.forEach { it(value) }
    }
}

class PrintListener {
    fun onEvent(value: String) {
        println("onEvent($value)")
    }
}

fun `event references`() {
    val event = Event<String>()
    val listener = PrintListener()
    event("invoke 1 NO")
    event += listener::onEvent
    event("invoke 2 YES") // the only one which gets printed by listener
    event -= listener::onEvent
    event("invoke 3 NO")
}

// =====================================================================================================================

interface SuperFoo {
    fun foo(): CharSequence
}
class SubFoo: SuperFoo {
    override fun foo(): String = ""
}

fun `different types but should still reference same method`() {
    val subFoo: SubFoo = SubFoo()
    val superFoo: SuperFoo = subFoo

    println(subFoo::foo == superFoo::foo) // prints out false
}

// =====================================================================================================================
class GetsExt {
    fun ordinaryMethod() {}
}
class DoesExt {
    fun GetsExt.extendedMethod() {}

    fun main() {
        GetsExt().apply(GetsExt::ordinaryMethod)
        // GetsExt().apply(GetsExt::extendedMethod)
        // compile error! "'extendedMethod' is a member and an extension at the same time. References to such elements are not allowed"
    }
}
/*
POSSIBLE:
class Foo {
    fun doStuff(bar: Bar) {
    }
}

NOT POSSIBLE:
class Foo {
    fun Bar.doStuff() {
    }
}
 */
