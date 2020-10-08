package com.team4099.lib.logging

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.RobotBase
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.Instant

object Logger {
  private val dataSources = mutableListOf<LogSource>()
  private val logNotifier = Notifier(this::saveEvents)
  private lateinit var file: Path
  private lateinit var eventsFile: Path
  private var loggingLocation: String =
    when {
        RobotBase.isSimulation() -> "./logs"
        File("/media/sda1/").exists() -> "/media/sda1/logs/"
        else -> "/home/lvuser/logs/"
    }

  private var values: String = ""
    get() {
      field = dataSources.joinToString(",") { it.supplier() }
      return field
    }
  private val events = mutableListOf<String>()

  enum class Severity {
    INFO,
    DEBUG,
    ERROR
  }

  @Throws(IOException::class)
  private fun createLogDirectory() {
    val logDirectory = File(loggingLocation)
    if (!logDirectory.exists()) {
      Files.createDirectory(Paths.get(loggingLocation))
    }
  }

  private fun createFile() {
    try {
      createLogDirectory()

      file =
          if (DriverStation.getInstance().isFMSAttached) {
            Paths.get(
                "$loggingLocation${DriverStation.getInstance().eventName}_" +
                    "${DriverStation.getInstance().matchType}" +
                    "${DriverStation.getInstance().matchNumber}.csv")
          } else {
            Paths.get("${loggingLocation}test.csv")
          }
      eventsFile =
        if (DriverStation.getInstance().isFMSAttached) {
          Paths.get(
            "$loggingLocation${DriverStation.getInstance().eventName}_" +
              "${DriverStation.getInstance().matchType}" +
              "${DriverStation.getInstance().matchNumber}Events.csv")
        } else {
          Paths.get("${loggingLocation}testEvents.csv")
        }

      if (Files.exists(file)) Files.delete(file)
      Files.createFile(file)

      if (Files.exists(eventsFile)) Files.delete(eventsFile)
      Files.createFile(eventsFile)

      saveTitles()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun <T> addSource(tab: String, name: String, supplier: () -> T) {
    dataSources.add(LogSource(tab, name) { supplier().toString() })
  }

  fun saveLogs() {
    try {
      val data = "${Instant.now()},${DriverStation.getInstance().matchTime},$values"
      Files.write(file, listOf(data), StandardOpenOption.APPEND)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun startLogging() {
    createFile()
    logNotifier.startPeriodic(1.0)
  }

  @Throws(IOException::class)
  private fun saveTitles() {
    val titles = "Timestamp,match_time,${dataSources.joinToString(",") { "${it.tab}/${it.name}" }}"
    Files.write(file, listOf(titles), StandardOpenOption.APPEND)
  }

  fun addEvent(source: String, event: String, severity: Severity = Severity.INFO) {
    val log = "$severity,${Instant.now()},${DriverStation.getInstance().matchTime}," +
      "($source),$event"
    events.add(log)
    val consoleString = "[$severity][${Instant.now()}][${DriverStation.getInstance().matchTime}] " +
      "($source): $event"
    when (severity) {
      Severity.INFO -> println(consoleString)
      Severity.DEBUG -> println(consoleString)
      Severity.ERROR -> error(consoleString)
    }
  }

  private fun saveEvents() {
    while (events.isNotEmpty()) {
      try {
        val event = events.removeAt(0)
        Files.write(eventsFile, listOf(event), StandardOpenOption.APPEND)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }
}
