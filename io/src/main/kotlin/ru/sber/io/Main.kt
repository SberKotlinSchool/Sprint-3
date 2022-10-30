package ru.sber.io

import kotlin.io.path.Path
import kotlin.io.path.absolute

fun main() {
    val archiver = Archiver()
    archiver.zipLogfile()
    val targetZip = Path("io/logfile.zip").normalize().absolute()
    assert(targetZip.toFile().isFile)

    archiver.unzipLogfile()
    val targetUnzip = Path("io/unzippedLogfile.log").normalize().absolute()
    assert(targetUnzip.toFile().isFile)
}