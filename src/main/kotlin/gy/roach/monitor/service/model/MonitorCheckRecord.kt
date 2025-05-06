package gy.roach.monitor.service.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

/**
 * Represents a record of a URL monitoring check.
 */
data class MonitorCheckRecord(
    @JsonProperty("name") val name: String,
    @JsonProperty("url") val url: String,
    @JsonProperty("statusCode") val statusCode: Int?,
    @JsonProperty("timestamp") val timestamp: LocalDateTime,
    @JsonProperty("responseTimeMs") val responseTimeMs: Long?,
    @JsonProperty("errorMessage") val errorMessage: String?
)
