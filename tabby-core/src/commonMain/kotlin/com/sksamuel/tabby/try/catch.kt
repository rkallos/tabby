package com.sksamuel.tabby.`try`

interface Catch {
   fun <A> Try<A>.get(): A = fold({ throw it }, { it })
}

private object CatchImpl : Catch

/**
 * Invokes the given function [f] wrapping the result into a [Try.Success], or, if an exception
 * is thrown, will wrap the throwable into an [Try.Failure].
 *
 * Exposes [get] to extract values from Try monads, handling the Failure case by early
 * returning that failure.
 */
inline fun <A> catch(f: Catch.() -> A): Try<A> = try {
   CatchImpl.f().success()
} catch (t: Throwable) {
   t.failure()
}
