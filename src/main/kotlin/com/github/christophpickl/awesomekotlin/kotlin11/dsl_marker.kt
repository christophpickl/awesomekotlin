package com.github.christophpickl.awesomekotlin.kotlin11

// https://kotlinlang.org/docs/reference/type-safe-builders.html#scope-control-dslmarker-since-11
// Scope control for implicit receivers in DSLs

/*
html {
    head {
        head {} // should be forbidden
    }
    // ...
}
 */

fun main(args: Array<String>) {
    html {
        onlyMakesSenseForHtml()
        head {
//            this
//            this@html ... should be forbidden

            // compiler forbids when both contexts have @MyDslMarker
//            onlyMakesSenseForHtml()
//            head {
//            }
            this@head.onlyForHead()
            this@html.onlyMakesSenseForHtml() // we can still bypass it but must do it explicitly now!
        }
    }
}

fun html(code: HtmlContext.() -> Unit) {
    code(HtmlContext())
}

@DslMarker
annotation class MyDslMarker

@MyDslMarker
class HtmlContext {
    fun head(code: HeadContext.() -> Unit) {
        code(HeadContext())
    }

    fun onlyMakesSenseForHtml() {
        println("onlyMakesSenseForTop() invoked")
    }
}

@MyDslMarker
class HeadContext {
    fun onlyForHead() {
        println("onlyForHead() inovked")
    }
}
