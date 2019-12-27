package dev.vishna.dartfmt

import dev.vishna.kmnd.execute
import dev.vishna.kmnd.shList
import dev.vishna.kmnd.weaveTo
import dev.vishna.kmnd.weaveToBlocking
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.lang.IllegalStateException

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
    fun kmnd() = runBlocking<Unit> {
        val dartfmt = "dartfmt --help".shList()
        val dartOutputStream = ByteArrayOutputStream()
        val result = dartfmt.execute { inputStream ->
            inputStream weaveToBlocking dartOutputStream
        }

        if (result != 0) {
            throw IllegalStateException("dartfmt returned exit code $result")
        }

        val formattedOutput = dartOutputStream.toByteArray().toString(Charsets.UTF_8)
        print(formattedOutput)
        formattedOutput `should contain` "Idiomatically formats Dart source code"
    }

    @Test
    fun formatting() = runBlocking<Unit> {
        unformattedDartCode.dartfmt() `should be equal to` formattedDartCode
    }
}