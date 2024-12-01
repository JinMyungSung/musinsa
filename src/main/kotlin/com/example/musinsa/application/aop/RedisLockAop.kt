package com.example.musinsa.application.aop

import com.example.musinsa.application.annotation.RedisLock
import com.example.musinsa.application.component.RedisLockComponent
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component

@Aspect
@Component
class RedisLockAop(
    private val redisLockComponent: RedisLockComponent,
) {
    @Around("@annotation(com.example.musinsa.application.annotation.RedisLock))")
    fun redisLock(joinPoint: ProceedingJoinPoint): Any {
        val redisLock = (joinPoint.signature as MethodSignature).method.getAnnotation(RedisLock::class.java)!!

        val parameterNames = (joinPoint.signature as MethodSignature).parameterNames
        val args = joinPoint.args
        val parser = SpelExpressionParser()
        val context = StandardEvaluationContext()

        parameterNames.forEachIndexed { index, parameterName -> context.setVariable(parameterName, args[index]) }
        val lockKey =
            redisLock.lockKey
                .split(":")
                .map {
                    if (it.contains("#")) {
                        parser.parseExpression(it).getValue(context)
                    } else {
                        it
                    }
                }.joinToString(separator = ":")

        try {
            redisLockComponent.lockOrThrow(lockKey, redisLock.lockAcquisitionFailMessage)
            return joinPoint.proceed()
        } finally {
            redisLockComponent.releaseLock(lockKey)
        }
    }
}
