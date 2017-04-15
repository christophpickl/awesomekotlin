package com.github.christophpickl.awesomekotlin.kotlin11

import javax.script.ScriptEngineManager


/*

javax.script API (JSR-223)

add JSR impl: https://github.com/JetBrains/kotlin/tree/master/libraries/examples/kotlin-jsr223-local-example

repositories {
    ...
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots"
    }
}
runtime "org.jetbrains.kotlin:kotlin-jsr223-local-example:1.0" // script engine
 */
fun main(args: Array<String>) {
    val engine = ScriptEngineManager().getEngineByExtension("kts") ?: throw Exception("kts not supported by script engine :(")
    engine.eval("val x = 3")

    println(engine.eval("x + 2"))  // Prints out 5
}
