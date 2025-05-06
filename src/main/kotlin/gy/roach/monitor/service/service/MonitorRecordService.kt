package gy.roach.monitor.service.service

import gy.roach.monitor.service.model.MonitorCheckRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.RestClientException

/**
 * Service for posting monitor check records to the API.
 */
@Service
class MonitorRecordService(private val restTemplate: RestTemplate) {

    private val logger = LoggerFactory.getLogger(MonitorRecordService::class.java)

    @Value("\${monitor.api.url:http://localhost:7201/monitor-persistence/api/records}")
    private lateinit var apiUrl: String

    /**
     * Posts a monitor check record to the API.
     *
     * @param record The monitor check record to post
     * @return true if the record was successfully posted, false otherwise
     */
    fun postRecord(record: MonitorCheckRecord): Boolean {
        try {

            logger.info("Posting monitor check record for URL: {}, name {}", record.url, record.name)
            val response: ResponseEntity<Void> = restTemplate.postForEntity(apiUrl, record, Void::class.java)

            if (response.statusCode.is2xxSuccessful) {
                logger.info("Successfully posted monitor check record for URL: {}", record.url)
                return true
            } else {
                logger.warn("Failed to post monitor check record for URL: {} - Status code: {}",
                    record.url, response.statusCode)
                return false
            }
        } catch (e: RestClientException) {
            // Log the serialized object to diagnose serialization issues in native image
            try {
                val objectMapper = org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json().build<com.fasterxml.jackson.databind.ObjectMapper>()
                val serializedRecord = objectMapper.writeValueAsString(record)
                logger.error("Error posting monitor check record for URL: {} - {}. Serialized object: {}", 
                    record.url, e.message, serializedRecord, e)
            } catch (ex: Exception) {
                logger.error("Error serializing record for logging: {}", ex.message, ex)
                logger.error("Error posting monitor check record for URL: {} - {}", record.url, e.message, e)
            }
            return false
        }
    }
}
