package ru.yurtitov.arco.result

import java.io.Serializable
import java.lang.IllegalStateException
import java.lang.NullPointerException
import java.lang.RuntimeException

sealed class Result<out A> : Serializable {
    /**
     * Failure - Used when result is an error
     */
    internal data class Failure<out A>(internal val exception: RuntimeException) : Result<A>() {
        override fun toString(): String = "Failure(${exception.message})"

        override fun equals(other: Any?): Boolean =
            other is Failure<*> &&
                    exception.javaClass == other.exception.javaClass

        override fun hashCode(): Int = exception.hashCode()
    }

    /**
     * Success - Used when result is valid value
     */
    internal data class Success<out A>(internal val value: A) : Result<A>() {
        override fun toString(): String = "Success($value)"
    }

    /**
     * Empty - Used when result is an empty value and this is not an error
     */
    internal object Empty : Result<Nothing>() {
        override fun toString(): String = "Empty"
    }

    fun <B> map(f: (A) -> B): Result<B> =
        when (this) {
            is Failure -> Failure(exception)
            is Success -> {
                try {
                    invoke(f(value))
                } catch (e: RuntimeException) {
                    failure(e)
                } catch (e: Exception) {
                    failure(e)
                }
            }
            is Empty -> Empty
        }

    fun <B> flatMap(f: (A) -> Result<B>): Result<B> =
        when (this) {
            is Failure -> failure(exception)
            is Success -> {
                try {
                    f(value)
                } catch (e: RuntimeException) {
                    failure(e)
                } catch (e: Exception) {
                    failure(e)
                }
            }
            Empty -> Empty
        }

    fun getOrElse(defaultValue: @UnsafeVariance A): A =
        when (this) {
            is Failure -> defaultValue
            is Success -> value
            Empty -> defaultValue
        }

    fun orElse(defaultValue: () -> Result<@UnsafeVariance A>): Result<A> =
        when (this) {
            is Success -> this
            else -> {
                try {
                    defaultValue()
                } catch (e: RuntimeException) {
                    failure(e)
                } catch (e: Exception) {
                    failure(e)
                }
            }
        }

    companion object {
        operator fun <A> invoke(a: A? = null): Result<A> =
            when (a) {
                null -> Failure(NullPointerException())
                else -> Success(a)
            }

        operator fun <A> invoke(): Result<A> = Empty

        fun <A> failure(message: String): Result<A> =
            Failure(IllegalStateException(message))

        fun <A> failure(exception: RuntimeException): Result<A> =
            Failure(exception)

        fun <A> failure(exception: Exception): Result<A> =
            Failure(IllegalStateException(exception))
    }
}