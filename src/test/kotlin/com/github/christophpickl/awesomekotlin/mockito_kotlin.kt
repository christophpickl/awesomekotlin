package com.github.christophpickl.awesomekotlin

import com.nhaarman.mockito_kotlin.*
import org.testng.annotations.Test

interface Service {
    fun noParamNoReturn()
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

    private lateinit var sharedMock: Service

    fun `simplest noParamYesReturn`() {
        val mockService = mock<Service> {
            on { noParamYesReturn() } doReturn "mockReturn"
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

        verify(mock).yesParamNoReturn(any())
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
//        mock.withPositiveNumber(WantingPositiveNumber(12))

        // the `any` will create a `WantingPositiveNumber` instance reflectively, with default values, which will fail for int=0
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

}
