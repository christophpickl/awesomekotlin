package com.github.christophpickl.awesomekotlin.kotlin11

import kotlin.reflect.KProperty

// http://kotlinlang.org/docs/reference/delegated-properties.html
// https://github.com/Kotlin/KEEP/blob/master/proposals/local-delegated-properties.md

//delegated properties as such:
//- lazy
//- observable
//- by map

fun main(args: Array<String>) {
//    `lazy local variable access`()
    `custom delegate`()
}

// =====================================================================================================================

fun `official sample from doc website`(computeFoo: () -> LazyFoo) {
    val memoizedFoo by lazy(computeFoo)

    if (someCondition() && memoizedFoo.isValid()) { // short circuit evaluation to the rescue (but no lazy evaluation like in haskell ;)
        memoizedFoo.doSomething()
    }
}

class LazyFoo {
    fun isValid() = true
    fun doSomething() {}
}

// =====================================================================================================================

fun `lazy local variable access`() {
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
    println("loooong cat computation")
    return "long cat"
}
fun someCondition() = true


// CUSTOM DELEGATE
// =====================================================================================================================
fun `custom delegate`() {
    DelegatingClass()

    var verboseString: String by VerboseDelegate("init value")
    println("verboseString.length = ${verboseString.length}")
    verboseString = "new value xxxxxx"
    println("verboseString.length = ${verboseString.length}")
}

class DelegatingClass {
    var anotherVerbose: String by VerboseDelegate("init value")
    init {
        anotherVerbose = "haha"
        anotherVerbose.length
    }
}

class VerboseDelegate<V>(initValue: V) {
    private var currentValue = initValue
    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        // thisRef will be null if invoked for local variable delegates
        println("[DELEGATE] $thisRef - get() = $currentValue")
        return currentValue
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        println("[DELEGATE] $thisRef - set($value) = $currentValue")
        currentValue = value
    }
}
