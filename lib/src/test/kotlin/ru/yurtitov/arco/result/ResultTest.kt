package ru.yurtitov.arco.result

import org.junit.Assert.*
import org.junit.Test
import java.lang.NullPointerException
import java.lang.RuntimeException

class ResultTest {
    @Test
    fun test_invoke_whenThisIsSuccess_thenReturnSuccess() {
        val actual = Result(5)
        val expected = Result.Success(5)
        assertEquals(expected, actual)
    }

    @Test
    fun test_invoke_whenThisIsFailureNPE_thenReturnFailureNPE() {
        val actual = Result<Int>(null)
        val expected = Result.Failure<Int>(NullPointerException())
        assertEquals(expected, actual)
    }

    @Test
    fun test_map_whenThisIsSuccess_thenReturnSuccess() {
        val sut = Result(2)
        val actual = sut.map { it + 2 }
        val expected = Result.Success(4)
        assertEquals(expected, actual)
    }

    @Test
    fun test_map_whenThisIsFailureNPE_thenReturnFailureNPE() {
        val sut = Result<Int>(null)
        val actual = sut.map { it + 2 }
        val expected = Result.Failure<Int>(NullPointerException())
        assertEquals(expected, actual)
    }

    @Test
    fun test_map_whenThisIsSuccessAndParamThrowRTE_thenReturnFailureRTE() {
        val sut = Result(5)
        val actual = sut.map { throw RuntimeException() }
        val expected = Result.Failure<Int>(RuntimeException())
        assertEquals(expected, actual)
    }

    @Test
    fun test_map_whenThisIsEmpty_thenReturnEmpty() {
        val sut = Result<Int>()
        val actual = sut.map { it + 5 }
        val expected = Result.Empty
        assertEquals(expected, actual)
    }

    @Test
    fun test_flatMap_whenThisIsSuccess_thenReturnSuccess() {
        val sut = Result(5)
        val actual = sut.flatMap { Result(it + 2) }
        val expected = Result.Success(7)
        assertEquals(expected, actual)
    }

    @Test
    fun test_flatMap_whenThisIsSuccessAndParamThrowRTE_thenReturnFailureRTE() {
        val sut = Result(5)
        val someFun: (Int) -> Result<Int> = { throw RuntimeException() }
        val actual = sut.flatMap(someFun)
        val expected = Result.failure<Int>(RuntimeException())
        assertEquals(expected, actual)
    }

    @Test
    fun test_flatMap_whenThisIsFailureNPE_thenReturnFailureNPE() {
        val sut = Result<Int>(null)
        val someFun: (Int) -> Result<Int> = { Result.invoke(it) }
        val actual = sut.flatMap(someFun)
        val expected = Result.failure<Int>(NullPointerException())
        assertEquals(expected, actual)
    }

    @Test
    fun test_flatMap_whenThisIsEmpty_thenReturnEmpty() {
        val sut = Result<Int>()
        val actual = sut.flatMap { Result("sdf") }
        val expected = Result.Empty
        assertEquals(expected, actual)
    }

    @Test
    fun test_flatMap_whenThisIsEmptyAndParamThrowRTE_thenReturnEmpty() {
        val sut = Result<Int>()
        val actual = sut.flatMap<String> { throw RuntimeException() }
        val expected = Result.Empty
        assertEquals(expected, actual)
    }

    @Test
    fun test_getOrElse_whenThisIsSuccess_thenReturnTheParam() {
        val sut = Result(5)
        val defaultValue = 10
        val actual = sut.getOrElse(defaultValue)
        val expected = 5
        assertEquals(expected, actual)
    }

    @Test
    fun test_getOrElse_whenThisIsFailure_thenReturnDefault() {
        val sut = Result<Int>(null)
        val defaultValue = 10
        val actual = sut.getOrElse(defaultValue)
        val expected = 10
        assertEquals(expected, actual)
    }

    @Test
    fun test_getOrElse_whenThisIsEmpty_thenReturnDefaultValue() {
        val sut = Result<Int>()
        val actual = sut.getOrElse(5)
        val expected = 5
        assertEquals(expected, actual)
    }

    @Test
    fun test_orElse_whenThisSuccess_thenReturnSameSuccess() {
        val sut = Result(5)
        val actual = sut.orElse { Result(10) }
        val expected = Result(5)
        assertEquals(expected, actual)
    }

    @Test
    fun test_orElse_whenThisFailure_thenReturnDefaultValue() {
        val sut = Result<Int>(null)
        val actual = sut.orElse { Result(10) }
        val expected = Result(10)
        assertEquals(expected, actual)
    }

    @Test
    fun test_orElse_whenThisIsSuccessAndDefaultValueThrowsRTE_thenReturnSuccess() {
        val sut = Result(5)
        val actual = sut.orElse { throw RuntimeException() }
        val expected = Result(5)
        assertEquals(expected, actual)
    }

    @Test
    fun test_orElse_whenThisIsFailureAndDefaultValueThrowsRTE_thenReturnFailureRTE() {
        val sut = Result<Int>(null)
        val actual = sut.orElse { throw RuntimeException() }
        val expected = Result.failure<Int>(RuntimeException())
        assertEquals(expected, actual)
    }

    @Test
    fun test_orElse_whenThisIsEmptyAndDefaultValueThrowsRTE_thenReturnFailureRTE() {
        val sut = Result<Int>()
        val actual = sut.orElse { throw RuntimeException() }
        val expected = Result.failure<Int>(RuntimeException())
        assertEquals(expected, actual)
    }
}