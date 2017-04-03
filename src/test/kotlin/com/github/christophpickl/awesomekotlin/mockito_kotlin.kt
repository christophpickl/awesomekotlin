package com.github.christophpickl.awesomekotlin

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argForWhich
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.argWhere
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import org.testng.annotations.Test

data class ThreeValues(
        val one: String,
        val two: String,
        val three: String
)

interface Service {
    fun yesThreeParamsYesReturn(three: ThreeValues): String
    fun yesParamNoReturn(param: String)
    fun noParamYesReturn(): String
    fun yesParamYesReturn(param: String): String
}

class Testee(
        private val service: Service
) {
    fun passThroughNoParamYesReturn() = service.noParamYesReturn()
}

@Test class MockitoKotlinTest {

    // private lateinit var sharedMock: Service

    fun `simplest noParamYesReturn`() {
        val mockService = mock<Service>()
        whenever(mockService.noParamYesReturn()).thenReturn("mockReturn")

        assertThat(mockService.noParamYesReturn(), equalTo("mockReturn"))
    }

    fun `simplest yesParamYesReturn`() {
        val mockService = mock<Service>()
        whenever(mockService.yesParamYesReturn(any())).thenReturn("mockReturnWithParam")

        assertThat(mockService.yesParamYesReturn("will be ignored"), equalTo("mockReturnWithParam"))
    }

//    fun `more complex mock params`() {

    fun `use lambdas via KStubbing, noParamYesReturn`() {
        val mockService = mock<Service> {
            // KStubbing<T>.() -> Unit
            on { noParamYesReturn() } doReturn "mockReturn" // doReturn is an infix function
        }

        println("mockService.noParamYesReturn() => ${mockService.noParamYesReturn()}")
        // mockService.noParamNoReturn() // ... will make the verifyNoMoreInteractions fail

        verify(mockService).noParamYesReturn()
        verifyNoMoreInteractions(mockService)
    }

    fun `simplest yesParamNoReturn`() {
        val mock: Service = mock()
        val givenParam = "testParam"

        mock.yesParamNoReturn(givenParam)

        verify(mock).yesParamNoReturn(any()) // for arrays use: anyArray()
        verify(mock).yesParamNoReturn(givenParam)
        verify(mock).yesParamNoReturn(eq(givenParam))
        verify(mock).yesParamNoReturn(argWhere { it.startsWith("test") })
    }

    fun `autoinfer type, and method not mocked returns impossible null-value!`() {
        val testee = Testee(mock())
        val actual: String = testee.passThroughNoParamYesReturn()
        @Suppress("SENSELESS_COMPARISON")
        if (actual == null) {
            println("actual is null!")
        } else {
            println("actual: $actual")
        }
    }

    fun `auto conversion fails, if not using instance creators`() {
        // Instance creators only work for usages in the same file. This is to avoid global state and arbitrary test order from messing up your tests.
//        MockitoKotlin.registerInstanceCreator { WantingPositiveNumber(42) }

        val mock = mock<PositiveNumberReceiver>()
        mock.withPositiveNumber(WantingPositiveNumber(12))

        // the `any` will create a `WantingPositiveNumber` instance reflectively, with default values, which will fail (?!?) for int=0
        verify(mock).withPositiveNumber(any())
    }

    private class WantingPositiveNumber(val number: Int) {
        init {
            println("new WantingPositiveNumber")
            if (number < 1) throw IllegalArgumentException("Expected a positive number, but was: $number")
        }
    }

    private interface PositiveNumberReceiver {
        fun withPositiveNumber(positiveNumber: WantingPositiveNumber)
    }


    interface WithSetterProperty {
        var bar: String
    }

    fun `verify for set property`() {
        val withSetter = mock<WithSetterProperty>()
        withSetter.bar = "foo"
        verify(withSetter).bar = "foo"
    }

    fun `argument matchers`() {
        val mockService = mock<Service>()
//        mockService.yesThreeParamsYesReturn(ThreeValues("x", "x", "x"))
        mockService.yesThreeParamsYesReturn(ThreeValues("1", "2", "3"))

        verify(mockService).yesThreeParamsYesReturn(argThat { one == "1" })
        verify(mockService).yesThreeParamsYesReturn(argForWhich { two == "2" })
        verify(mockService).yesThreeParamsYesReturn(com.nhaarman.mockito_kotlin.check {
            assertThat(it.one, equalTo("1"))
            assertThat(it.two, equalTo("2"))
        })
    }

    fun `argument captors`() {
        val mockService = mock<Service>()

        mockService.yesThreeParamsYesReturn(ThreeValues("1", "2", "3"))
        mockService.yesThreeParamsYesReturn(ThreeValues("A", "B", "C"))

        argumentCaptor<ThreeValues>().apply {
            verify(mockService, times(2)).yesThreeParamsYesReturn(capture())

            assertThat(firstValue.one, equalTo("1"))
            assertThat(secondValue.two, equalTo("B"))
        }
    }

    fun `in order`() {
//        inOrder(a,b) {
//            verify(a).doSomething()
//            verify(b).doSomething()
//        }

    }

    private class KotlinFinalClassByDefault {
        fun returnValue() = "implementation value should be mocked"
    }

    // works with addition of: http://hadihariri.com/2016/10/04/Mocking-Kotlin-With-Mockito/
//    @Test(expectedExceptions = arrayOf(MockitoException::class))
    fun `mockito can not mock finalByDefault type`() {
        val finalMock = mock<KotlinFinalClassByDefault>()
        whenever(finalMock.returnValue()).thenReturn("mockValue")

        assertThat(finalMock.returnValue(), equalTo("mockValue"))
    }
}
