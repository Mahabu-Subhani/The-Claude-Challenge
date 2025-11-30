package com.runanywhere.startup_hackathon20

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.runanywhere.sdk.public.RunAnywhere
import com.runanywhere.sdk.public.extensions.listAvailableModels
import com.runanywhere.sdk.models.ModelInfo
import com.runanywhere.startup_hackathon20.data.CrisisReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

/**
 * GridZero Crisis Analysis ViewModel
 *
 * This is the intelligence layer that extracts structured tactical data
 * from chaotic field reports. Instead of just "chatting", we EXTRACT
 * actionable intelligence in strict JSON format.
 */
class CrisisViewModel : ViewModel() {

    // Crisis Reports (The Tactical Feed)
    private val _reports = MutableStateFlow<List<CrisisReport>>(emptyList())
    val reports: StateFlow<List<CrisisReport>> = _reports

    // Analysis State
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing

    // Model Management
    private val _availableModels = MutableStateFlow<List<ModelInfo>>(emptyList())
    val availableModels: StateFlow<List<ModelInfo>> = _availableModels

    private val _downloadProgress = MutableStateFlow<Float?>(null)
    val downloadProgress: StateFlow<Float?> = _downloadProgress

    private val _currentModelId = MutableStateFlow<String?>(null)
    val currentModelId: StateFlow<String?> = _currentModelId

    private val _statusMessage = MutableStateFlow<String>("Initializing GridZero...")
    val statusMessage: StateFlow<String> = _statusMessage

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Last raw input for debugging
    private val _lastRawInput = MutableStateFlow<String>("")
    val lastRawInput: StateFlow<String> = _lastRawInput

    // Bandwidth savings tracker
    private val _totalBytesSaved = MutableStateFlow<Long>(0)
    val totalBytesSaved: StateFlow<Long> = _totalBytesSaved

    init {
        loadAvailableModels()
    }

    private fun loadAvailableModels() {
        viewModelScope.launch {
            try {
                val models = listAvailableModels()
                _availableModels.value = models
                _statusMessage.value = "GRIDZERO READY - Load tactical AI model"
            } catch (e: Exception) {
                _statusMessage.value = "Error loading models: ${e.message}"
                Log.e(TAG, "Failed to load models", e)
            }
        }
    }

    fun downloadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Downloading tactical model..."
                RunAnywhere.downloadModel(modelId).collect { progress ->
                    _downloadProgress.value = progress
                    _statusMessage.value = "Downloading: ${(progress * 100).toInt()}%"
                }
                _downloadProgress.value = null
                _statusMessage.value = "Download complete! Load model to begin operations."
                refreshModels()
            } catch (e: Exception) {
                _statusMessage.value = "Download failed: ${e.message}"
                _downloadProgress.value = null
                Log.e(TAG, "Download failed", e)
            }
        }
    }

    fun loadModel(modelId: String) {
        viewModelScope.launch {
            try {
                _statusMessage.value = "Loading tactical AI..."
                val success = RunAnywhere.loadModel(modelId)
                if (success) {
                    _currentModelId.value = modelId
                    _statusMessage.value = "OPERATIONAL - GridZero AI Online"
                } else {
                    _statusMessage.value = "Failed to load model"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Error loading model: ${e.message}"
                Log.e(TAG, "Model load failed", e)
            }
        }
    }

    /**
     * THE WINNING FEATURE
     *
     * Analyzes chaotic field reports and extracts critical tactical data.
     * We force the model to act as a military-grade dispatcher,
     * outputting ONLY structured JSON.
     *
     * Input: "This is unit 4, we have a structural collapse at the library,
     *         severe flooding, three civilians trapped, send heavy lift gear immediately!"
     *
     * Output: Strict JSON with location, type, severity, casualties, resources
     */
    fun analyzeFieldReport(inputText: String) {
        if (_currentModelId.value == null) {
            _error.value = "Tactical AI not loaded. Please download and load a model first."
            _statusMessage.value = "ERROR: No model loaded"
            return
        }

        _lastRawInput.value = inputText

        viewModelScope.launch {
            _isAnalyzing.value = true
            _error.value = null
            _statusMessage.value = "Analyzing field report..."

            try {
                // THE WINNING PROMPT
                // Force the model to act as an emergency dispatcher
                val systemPrompt = """
You are an offline emergency dispatcher AI for GridZero tactical operations.
Analyze the field report and extract critical tactical data into JSON.

Schema:
{
  "location_name": "string",
  "incident_type": "Fire|Collapse|Flood|Medical|CivilUnrest|Hazmat|Explosion",
  "severity": "Low|Moderate|Critical",
  "casualties": int,
  "resources_needed": ["string"],
  "sentiment": "Calm|Panic|Urgent"
}

Rules:
1. Output ONLY valid JSON. No explanations, no markdown, no code blocks.
2. If location is vague, infer from context or use "Unknown".
3. Infer severity based on keywords: "trapped", "blood", "multiple casualties" = Critical
4. Keep resource names short: "Ambulance", "Crane", "Boat", "Firefighters", "Police", "Medics"
5. Extract sentiment from tone: urgent keywords = "Urgent", calm tone = "Calm", distress = "Panic"
6. If casualties not mentioned, use 0.
                """.trimIndent()

                val finalPrompt = "$systemPrompt\n\nField Report: \"$inputText\"\n\nJSON:"

                // Call the One-Shot Generation
                val response = RunAnywhere.generate(finalPrompt)

                Log.d(TAG, "Raw LLM Response: $response")

                // Parse the JSON response
                val report = parseJsonResponse(response)

                if (report != null) {
                    // Add to the live tactical list
                    _reports.value = _reports.value + report
                    _statusMessage.value =
                        "Report processed - ${report.incidentType} at ${report.locationName}"

                    // Calculate bandwidth savings
                    val rawBytes = inputText.toByteArray(Charsets.UTF_8).size
                    val jsonBytes = report.estimatedSizeInBytes()
                    val saved = rawBytes - jsonBytes
                    _totalBytesSaved.value += saved.toLong()

                    Log.i(TAG, "Bandwidth saved: $saved bytes (${rawBytes}B → ${jsonBytes}B)")
                } else {
                    _error.value = "Failed to parse AI response. Retrying with cleaner prompt..."
                    // Retry with simplified input
                    retryWithSimplifiedPrompt(inputText)
                }

            } catch (e: Exception) {
                val errorMsg = when {
                    e.message?.contains("not initialized", ignoreCase = true) == true -> {
                        "LLM not initialized. Please wait a moment and try reloading the model."
                    }

                    e.message?.contains("No model loaded", ignoreCase = true) == true -> {
                        "Model not loaded. Please load a model from the settings."
                    }

                    else -> "Analysis failed: ${e.message}"
                }
                _error.value = errorMsg
                _statusMessage.value = "Analysis failed"
                Log.e(TAG, "Analysis failed: ${e.message}", e)
                e.printStackTrace()
            } finally {
                _isAnalyzing.value = false
            }
        }
    }

    /**
     * Parse the LLM response into a CrisisReport
     * Handles various JSON formats and extracts the data
     */
    private fun parseJsonResponse(response: String): CrisisReport? {
        try {
            // Clean the response - remove markdown code blocks if present
            var cleanedResponse = response.trim()

            // Remove markdown code blocks
            if (cleanedResponse.startsWith("```")) {
                cleanedResponse = cleanedResponse
                    .removePrefix("```json")
                    .removePrefix("```")
                    .removeSuffix("```")
                    .trim()
            }

            // Find JSON object in the response
            val jsonStart = cleanedResponse.indexOf('{')
            val jsonEnd = cleanedResponse.lastIndexOf('}')

            if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
                cleanedResponse = cleanedResponse.substring(jsonStart, jsonEnd + 1)
            }

            val json = JSONObject(cleanedResponse)

            // Extract resources array
            val resourcesArray = json.optJSONArray("resources_needed") ?: json.optJSONArray("res")
            val resourcesList = mutableListOf<String>()
            if (resourcesArray != null) {
                for (i in 0 until resourcesArray.length()) {
                    resourcesList.add(resourcesArray.getString(i))
                }
            }

            // Build the report
            val report = CrisisReport(
                locationName = json.optString("location_name", json.optString("loc", "Unknown")),
                incidentType = json.optString("incident_type", json.optString("type", "General")),
                severity = json.optString("severity", json.optString("sev", "Moderate")),
                casualties = json.optInt("casualties", json.optInt("cas", 0)),
                resourcesNeeded = resourcesList,
                sentiment = json.optString("sentiment", json.optString("sent", "Urgent"))
            )

            Log.d(TAG, "Parsed report: $report")
            return report

        } catch (e: Exception) {
            Log.e(TAG, "JSON parsing failed: ${e.message}\nResponse: $response", e)
            return null
        }
    }

    /**
     * Retry with a simpler, more focused prompt if initial parsing fails
     */
    private suspend fun retryWithSimplifiedPrompt(inputText: String) {
        try {
            val simplePrompt = """
Extract incident info from this report in JSON format:
"$inputText"

Output only this JSON structure:
{"location_name":"[location]","incident_type":"Fire|Collapse|Flood|Medical","severity":"Low|Moderate|Critical","casualties":0,"resources_needed":["resource1","resource2"],"sentiment":"Calm|Urgent|Panic"}
            """.trimIndent()

            val response = RunAnywhere.generate(simplePrompt)
            val report = parseJsonResponse(response)

            if (report != null) {
                _reports.value = _reports.value + report
                _statusMessage.value = "Report processed (retry) - ${report.incidentType}"
                _error.value = null
            } else {
                _error.value = "Could not extract structured data from report"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Retry failed", e)
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearReports() {
        _reports.value = emptyList()
        _totalBytesSaved.value = 0
    }

    fun removeReport(report: CrisisReport) {
        _reports.value = _reports.value - report
    }

    fun refreshModels() {
        viewModelScope.launch {
            try {
                RunAnywhere.scanForDownloadedModels()
                loadAvailableModels()
            } catch (e: Exception) {
                Log.e(TAG, "Model refresh failed", e)
            }
        }
    }

    companion object {
        private const val TAG = "CrisisViewModel"
    }
}
