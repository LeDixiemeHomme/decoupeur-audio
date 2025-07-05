package org.example

import scala.io.Source
import sys.process._
import java.io.File

object AppTimeCodes {
  def main(args: Array[String]): Unit = {
    val inputFile = "/home/valle/Travail/Dev/java/decoupeur-audio/data/input.mp3"
    val timecodesFile = "timecodes.txt"
    val outputDir = new File("output")
    outputDir.mkdirs()

    val lines = Source.fromFile(timecodesFile)
    .getLines().filter(_.contains("-->")).toList

    lines.zipWithIndex.foreach { case (line, index) =>
      val Array(start, end) = line
      .split("-->").map(_.trim.replace(",", "."))
      val outputFile = s"output/part_%02d.mp3".format(index + 1)
      val command = Seq(
        "ffmpeg", "-y",
        "-i", inputFile,
        "-ss", start,
        "-to", end,
        "-c", "copy",
        outputFile
      )
      println(s"Running: ${command.mkString(" ")}")
      val exitCode = command.!
      if (exitCode == 0) println(s"✅ Created $outputFile")
      else println(s"❌ Failed to create $outputFile")
    }
  }
}
