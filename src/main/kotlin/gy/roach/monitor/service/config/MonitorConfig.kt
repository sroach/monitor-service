package gy.roach.monitor.service.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "monitor")
class MonitorConfig {
    var urls: List<String> = emptyList()
    var scheduler: SchedulerConfig = SchedulerConfig()

    class SchedulerConfig {
        var intervalSeconds: Long = 30
    }
}
