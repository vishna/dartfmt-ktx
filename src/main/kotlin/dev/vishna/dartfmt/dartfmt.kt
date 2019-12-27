package dev.vishna.dartfmt

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.IllegalStateException
import dev.vishna.emojilog.std.*
import dev.vishna.emojilog.android.info
import dev.vishna.emojilog.android.warn
import dev.vishna.kmnd.execute
import dev.vishna.kmnd.shList
import dev.vishna.kmnd.weaveToBlocking
import java.io.File

internal val log by lazy { defaultLogger() }

// this tries to figure out where dartfmt command is
private val dartfmt by lazy {
    val paths = System.getenv("PATH").split(":")
    val homePath = System.getProperty("user.home")
    val dartPath = paths.map { path -> "$path/cache/dart-sdk/bin".replaceFirst(Regex("^~"), homePath) }.firstOrNull {
        File(it).exists()
    }
    val dartFmt = if (dartPath.isNullOrBlank()) "dartfmt" else "$dartPath/dartfmt"
    var command: List<String> = listOf(dartFmt, "--help")
    runBlocking {
        try {
            val outputStream = ByteArrayOutputStream()
            command.execute { inuputStream ->
                inuputStream weaveToBlocking outputStream
            }
            command = listOf(dartFmt)
        } catch (t: IOException) {
            command = dartFmt.shList()
        }
    }
    command
}

suspend fun String.dartfmt(): String = coroutineScope {
    // TODO add some sort of LRU cache for this
    try {
        val dartOutputStream = ByteArrayOutputStream()
        val result = dartfmt.execute { outputStream, inputStream, errorStream ->

            outputStream.use {
                this@dartfmt weaveToBlocking outputStream
            }

            inputStream weaveToBlocking dartOutputStream
            errorStream weaveToBlocking System.err
        }

        if (result != 0) {
            throw IllegalStateException("dartfmt returned exit code $result")
        }

        val formattedOutput = dartOutputStream.toByteArray().toString(Charsets.UTF_8)
        if (formattedOutput.startsWith("Idiomatically formats Dart source code")) {
            log.warn.."dartfmt present but failed to execute properly"
            this@dartfmt
        } else {
            formattedOutput
        }
    } catch (e: IOException) {
        log.warn..e
        log.info.."If you see message about dartfmt, you might need to export PATH to dart-sdk"
        log.info.."""e.g. export PATH="${'$'}PATH:/path/to/flutter/bin/cache/dart-sdk/bin""""
        this@dartfmt
    } catch (ise: IllegalStateException) {
        log.warn..ise
        log.info.."Failed to apply dartfmt formatting to the generated code"
        this@dartfmt
    }
}