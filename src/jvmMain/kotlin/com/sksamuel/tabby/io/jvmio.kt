@file:JvmName("iojvm.kt")

package com.sksamuel.tabby.io

/**
 * Returns an effect that acquires an [AutoCloseable] resource using the given,
 * possibly, effectful [acquire] function, and then applies that resource to the
 * given effect [f].
 *
 * The resource is guaranteed to be closed before the effect completes.
 * Any error in acquisition is ignored.
 */
fun <A : AutoCloseable, B> IO.Companion.useWith(
   acquire: suspend () -> A,
   f: (A) -> Task<B>,
): Task<B> {
   return effect(acquire).flatMap { a ->
      val close = effect { a.close() }
      f(a).tap { close }.tapError { close }
   }
}

/**
 * Returns an effect that acquires an [AutoCloseable] resource using the given
 * [acquire] effect, and then applies that resource to the given [apply] effect.
 *
 * Both the acquire and action functions can be effectful, and will be handled as such.
 *
 * The resource is guaranteed to be closed before the effect completes.
 * Any error in acquisition is ignored.
 */
fun <A : AutoCloseable, B> IO.Companion.use(
   acquire: Task<A>,
   apply: (A) -> Task<B>,
): Task<B> {
   return acquire.flatMap { a ->
      val close = effect { a.close() }
      apply(a).tap { close }.tapError { close }
   }
}
