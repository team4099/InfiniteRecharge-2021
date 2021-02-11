package com.team4099.lib.logging

import edu.wpi.first.networktables.EntryListenerFlags
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.RobotBase
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import java.io.File
import java.io.IOException
import java.lang.ClassCastException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.Instant

/**
 * The global logger object.
 *
 * Manages Shuffleboard entries and CSV logging of values.
 */
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

  /** Severity of an event. */
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

  /**
   * Add a source of data for the logger.
   *
   * @param tab The name of the Shuffleboard tab to add this value to. Typically the subsystem name.
   * @param name The name of this value.
   * @param supplier A function which returns the value to be logged.
   * @param setter An optional function which will be called when the value in Shuffleboard is
   * changed.
   */
  fun <T> addSource(tab: String, name: String, supplier: () -> T, setter: ((T) -> Unit)?) {
    dataSources.add(LogSource(tab, name) { supplier().toString() })
    val shuffleboardEntry = Shuffleboard.getTab(tab).add(name, supplier)

    if (setter != null) {
      shuffleboardEntry.entry
          .addListener(
              {
                val newValue = it.getEntry().value

                try {
                  // Unchecked cast since we don't know the type of this
                  // source due to type erasure
                  @Suppress("UNCHECKED_CAST")
                  setter(newValue as T)
                } catch (e: ClassCastException) {
                  addEvent(
                      "Logger",
                      "Could not change value for $tab/$name due to invalid type cast.",
                      Severity.ERROR)
                }
              },
              EntryListenerFlags.kUpdate)
    }
  }

  /**
   * Add a source of data for the logger.
   *
   * @param tab The name of the Shuffleboard tab to add this value to. Typically the subsystem name.
   * @param name The name of this value.
   * @param supplier A function which returns the value to be logged.
   * @param setter An optional function which will be called when the value in Shuffleboard is
   * changed.
   */
  fun <T> addSource(tab: String, name: String, supplier: () -> T) {
    addSource(tab, name, supplier, null)
  }

  /** Write logs to the CSV file. */
  fun saveLogs() {
    try {
      val data = "${Instant.now()},${DriverStation.getInstance().matchTime},$values"
      Files.write(file, listOf(data), StandardOpenOption.APPEND)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  /** Begin logging. Creates CSV files and starts the logging thread. */
  fun startLogging() {
    createFile()
    logNotifier.startPeriodic(1.0)
  }

  @Throws(IOException::class)
  private fun saveTitles() {
    val titles = "Timestamp,match_time,${dataSources.joinToString(",") { "${it.tab}/${it.name}" }}"
    Files.write(file, listOf(titles), StandardOpenOption.APPEND)
  }

  /**
   * Track an event that occurs. Saves the event to a CSV and prints it to the console.
   *
   * @param source The source of the event, typically a subsystem or class name.
   * @param event The text to log.
   * @param severity The severity of the event. Defaults to INFO. Events with severity ERROR will be
   * logged to stderr instead of stdout.
   */
  fun addEvent(source: String, event: String, severity: Severity = Severity.INFO) {
    val log =
        "$severity,${Instant.now()},${DriverStation.getInstance().matchTime}," + "($source),$event"
    events.add(log)
    val consoleString =
        "[$severity][${Instant.now()}][${DriverStation.getInstance().matchTime}] " +
            "($source): $event"
    when (severity) {
      Severity.INFO -> println(consoleString)
      Severity.DEBUG -> println(consoleString)
      Severity.ERROR -> System.err.println(consoleString)
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
