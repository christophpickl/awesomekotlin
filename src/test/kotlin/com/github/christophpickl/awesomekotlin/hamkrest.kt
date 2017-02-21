package com.github.christophpickl.awesomekotlin

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.should.shouldMatch
import org.testng.annotations.Test

// https://github.com/npryce/hamkrest


@Test class MyTest {

    fun `do this with hamkrest`() {
        assertThat("xyzzy", startsWith("x") and endsWith("y") and !containsSubstring("a"))


        val isBlank = Matcher(String::isBlank)
        assertThat("", isBlank)
        assertThat("", String::isBlank)


        fun String.hasLength(n: Int): Boolean = this.length == n
        val isTheRightLength = Matcher(String::hasLength, 8)
        assertThat("12345678", isTheRightLength)


        val isLongEnough = has(String::length, greaterThan(8))
        assertThat("12345678abc", isLongEnough)

        "12345678abc" shouldMatch isLongEnough
    }

}
