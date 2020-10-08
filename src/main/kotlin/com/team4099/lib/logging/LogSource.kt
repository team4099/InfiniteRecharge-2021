package com.team4099.lib.logging

data class LogSource(val tab: String, val name: String, val supplier: () -> String)
