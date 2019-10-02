package dev.vishna.dartfmt

import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.junit.Test

val unformattedDartCode =
"""
class Foo{
int bar;
}
"""

val formattedDartCode =
"""class Foo {
  int bar;
}
"""

class DartfmtTest {
    @Test
    fun formatting() = runBlocking<Unit> {
        unformattedDartCode.dartfmt() `should be equal to` formattedDartCode
    }
}