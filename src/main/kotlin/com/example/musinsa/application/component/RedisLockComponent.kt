package com.example.musinsa.application.component

import com.example.musinsa.application.exception.ExceptionType
import com.example.musinsa.application.exception.MusinsaException
import org.springframework.stereotype.Component

@Component
class RedisLockComponent {
    private val fakeRedisStorage = mutableSetOf<String>()

    init {
        fakeRedisStorage.add("LOCK_BRAND_NAME_V1:5")
    }

    fun lockOrThrow(
        lockKey: String,
        failMessage: String,
    ) {
        if (fakeRedisStorage.contains(lockKey)) {
            throw MusinsaException(ExceptionType.LOCK_ACQUISITION_FAIL)
        } else {
            fakeRedisStorage.add(lockKey)
        }
    }

    fun releaseLock(lockKey: String) {
        fakeRedisStorage.remove(lockKey)
    }
}
