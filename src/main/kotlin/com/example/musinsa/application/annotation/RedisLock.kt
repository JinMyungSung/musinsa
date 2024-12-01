package com.example.musinsa.application.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisLock(
    val lockKey: String,
    val lockAcquisitionFailMessage: String = "잠시후 다시 시도해주세요.",
)
