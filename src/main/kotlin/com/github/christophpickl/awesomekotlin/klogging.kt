package com.github.christophpickl.awesomekotlin

import mu.KotlinLogging

private val fileLog = KotlinLogging.logger {}

fun main(args: Array<String>) {
    fileLog.info("file log infooos")
    MyLogeee.writeLogs()
    LogViaLambda.writeLogs()
    LogViaJavaclass.writeLogs()
}

object MyLogeee {
    private val log = KotlinLogging.logger {}

    fun writeLogs() {
        log.trace { "trace says: " + longRunning() }
        log.debug("debug message")
        log.info("good old: {}", "template")
        log.warn(RuntimeException("my exception")) { "some message for exception " }
    }

    private fun longRunning() = "looong cat"
}

object LogViaLambda {
    private val log = LOG {}
    fun writeLogs() {
        log.info("by LOG {}")
    }
}

object LogViaJavaclass {
    private val log = LOG(javaClass)
    fun writeLogs() {
        log.info("by LOG(javaClass)")
    }
}

fun LOG(func: () -> Unit) = KotlinLogging.logger(func)
fun LOG(clazz: Class<out Any>) = KotlinLogging.logger(clazz.simpleName)

