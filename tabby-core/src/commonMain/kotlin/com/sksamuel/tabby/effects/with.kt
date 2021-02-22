package com.sksamuel.tabby.effects

import com.sksamuel.tabby.`try`.MonadControlException
import com.sksamuel.tabby.`try`.Try
import com.sksamuel.tabby.`try`.failure
import com.sksamuel.tabby.`try`.success

@Deprecated("with is a scope function in kotlin; use comprehension {}")
fun <A> with(f: suspend () -> A): IO<A> = comprehension(f)
fun <A> comprehension(f: suspend () -> A): IO<A> = object : IO<A>() {
   override suspend fun apply(): Try<A> {
      return try {
         f().success()
      } catch (t: MonadControlException) {
         t.t.failure()
      } catch (t: Throwable) {
         t.failure()
      }
   }
}
