package com.maxbohoniuk.springkotlindemo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
class AuditingConfig {

    @Bean
    fun auditorAware(): AuditorAware<String> = AuditorAware { Optional.of(SecurityContextHolder.getContext().authentication.name) }
}