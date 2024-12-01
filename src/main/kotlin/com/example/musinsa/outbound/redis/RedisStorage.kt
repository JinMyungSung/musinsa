package com.example.musinsa.outbound.redis

interface RedisStorage {
    fun <T> set(
        key: String,
        value: T,
    )

    fun evict(key: String)

    fun <T> get(
        key: String,
        clazz: Class<T>,
    ): T?
}
