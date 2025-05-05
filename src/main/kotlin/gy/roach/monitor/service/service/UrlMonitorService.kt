package gy.roach.monitor.service.service

import gy.roach.monitor.service.config.MonitorConfig
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Service
class UrlMonitorService(private val monitorConfig: MonitorConfig) {

    private val logger = LoggerFactory.getLogger(UrlMonitorService::class.java)

    @Scheduled(fixedRateString = "#{@monitorConfig.scheduler.intervalSeconds * 1000}", timeUnit = TimeUnit.MILLISECONDS)
    fun pingUrls() {
        logger.info("Starting URL monitoring at {}", LocalDateTime.now())
        
        if (monitorConfig.urls.isEmpty()) {
            logger.warn("No URLs configured for monitoring")
            return
        }
        
        monitorConfig.urls.forEach { url ->
            try {
                logger.info("Pinging URL: {}", url)
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.requestMethod = "HEAD"
                
                val responseCode = connection.responseCode
                if (responseCode >= 200 && responseCode < 400) {
                    logger.info("Successfully pinged URL: {} - Response code: {}", url, responseCode)
                } else {
                    logger.warn("Failed to ping URL: {} - Response code: {}", url, responseCode)
                }
                
                connection.disconnect()
            } catch (e: Exception) {
                logger.error("Error pinging URL: {} - {}", url, e.message, e)
            }
        }
        
        logger.info("Completed URL monitoring at {}", LocalDateTime.now())
    }
}