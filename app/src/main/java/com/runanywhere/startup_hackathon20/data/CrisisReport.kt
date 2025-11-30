package com.runanywhere.startup_hackathon20.data

import kotlinx.serialization.Serializable

/**
 * GridZero Crisis Report Schema
 *
 * The strict schema for the Actionable Data Engine.
 * Converts chaotic voice/text reports into tiny, structured JSON packets
 * that can be synced instantly over emergency mesh networks.
 */
@Serializable
data class CrisisReport(
    val locationName: String,          // e.g., "Library", "North Sector"
    val incidentType: String,          // "Fire", "Collapse", "Medical", "Flood", "CivilUnrest"
    val severity: String,              // "Critical", "Moderate", "Low"
    val casualties: Int,               // Estimated count
    val resourcesNeeded: List<String>, // e.g., ["Medic", "Crane", "Boat"]
    val sentiment: String,             // "Panic", "Calm", "Urgent" - useful for triage
    val timestamp: Long = System.currentTimeMillis() // When report was created
) {
    /**
     * Converts the crisis report to a compact JSON string for transmission
     * over low-bandwidth mesh networks
     */
    fun toCompactJson(): String {
        return """{"loc":"$locationName","type":"$incidentType","sev":"$severity","cas":$casualties,"res":[${
            resourcesNeeded.joinToString(
                ","
            ) { "\"$it\"" }
        }],"sent":"$sentiment","ts":$timestamp}"""
    }

    /**
     * Estimates the size in bytes for bandwidth comparison
     */
    fun estimatedSizeInBytes(): Int {
        return toCompactJson().toByteArray(Charsets.UTF_8).size
    }
}
