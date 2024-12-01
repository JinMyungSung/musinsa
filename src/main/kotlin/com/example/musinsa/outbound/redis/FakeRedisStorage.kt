package com.example.musinsa.outbound.redis

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component

@Component
class FakeRedisStorage : RedisStorage {
    private val objectMapper: ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    private val cache: MutableMap<String, String> = mutableMapOf()

    override fun <T> set(
        key: String,
        value: T,
    ) {
        cache[key] = objectMapper.writeValueAsString(value)
    }

    override fun evict(key: String) {
        cache.remove(key)
    }

    override fun <T> get(
        key: String,
        clazz: Class<T>,
    ): T? {
        return cache[key]?.let {
            return objectMapper.readValue(it, clazz)
        }
    }
}
