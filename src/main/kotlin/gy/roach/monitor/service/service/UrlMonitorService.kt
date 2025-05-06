package gy.roach.monitor.service.service

import gy.roach.monitor.service.config.MonitorConfig
import gy.roach.monitor.service.model.MonitorCheckRecord
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class UrlMonitorService(
    private val monitorConfig: MonitorConfig,
    private val monitorRecordService: MonitorRecordService
) {

    private val logger = LoggerFactory.getLogger(UrlMonitorService::class.java)

    @Scheduled(fixedRateString = "#{@monitorConfig.scheduler.intervalSeconds * 1000}", timeUnit = TimeUnit.MILLISECONDS)
    fun pingUrls() {
        logger.info("Starting URL monitoring at {}", LocalDateTime.now())

        if (monitorConfig.urls.isEmpty()) {
            logger.warn("No URLs configured for monitoring")
            return
        }

        monitorConfig.urls.forEach { urlEntry ->
            try {
                logger.info("Pinging URL: {}", urlEntry.url)
                val startTime = System.currentTimeMillis()
                val connection = URL(urlEntry.url).openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.requestMethod = "HEAD"

                val statusCode = connection.responseCode
                val responseTimeMs = System.currentTimeMillis() - startTime
                val success = statusCode >= 200 && statusCode < 400

                if (success) {
                    logger.info("Successfully pinged URL: {} - Response code: {} - Response time: {}ms", urlEntry.url, statusCode, responseTimeMs)
                } else {
                    logger.warn("Failed to ping URL: {} - Response code: {} - Response time: {}ms", urlEntry.url, statusCode, responseTimeMs)
                }

                // Create a monitor check record
                val record = MonitorCheckRecord(
                    name = urlEntry.name,
                    url = urlEntry.url,
                    statusCode = statusCode,
                    timestamp = LocalDateTime.now(),
                    responseTimeMs = responseTimeMs,
                    errorMessage = null
                )

                // Post the record to the API
                monitorRecordService.postRecord(record)

                connection.disconnect()
            } catch (e: Exception) {
                logger.error("Error pinging URL: {} - {}", urlEntry.url, e.message, e)

                // Create a monitor check record for the error
                val record = MonitorCheckRecord(
                    name = urlEntry.name,
                    url = urlEntry.url,
                    statusCode = null,
                    timestamp = LocalDateTime.now(),
                    responseTimeMs = null,
                    errorMessage = e.message
                )

                // Post the record to the API
                monitorRecordService.postRecord(record)
            }
        }

        logger.info("Completed URL monitoring at {}", LocalDateTime.now())
    }
}
