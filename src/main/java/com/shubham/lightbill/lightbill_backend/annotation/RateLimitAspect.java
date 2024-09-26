package com.shubham.lightbill.lightbill_backend.annotation;

import com.shubham.lightbill.lightbill_backend.custumExceptions.RateLimitExceededException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Controller
public class RateLimitAspect {
    public static final String ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!";
    private final ConcurrentHashMap<String, List<Long>> requestCounts = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(RateLimitAspect.class);

    @Before("@annotation(com.shubham.lightbill.lightbill_backend.annotation.WithRateLimitProtection)")
    public void rateLimit(JoinPoint joinPoint) throws Exception {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final String ip = requestAttributes.getRequest().getRemoteAddr();
        final String endpoint = requestAttributes.getRequest().getRequestURI();
        final long currentTime = System.currentTimeMillis();

        String compositeKey = endpoint + ":" + ip;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        WithRateLimitProtection rateLimitAnnotation = method.getAnnotation(WithRateLimitProtection.class);

        final int rateLimit = rateLimitAnnotation.rateLimit();
        final long rateDuration = rateLimitAnnotation.rateDuration();

        requestCounts.putIfAbsent(compositeKey, new ArrayList<>());
        requestCounts.get(compositeKey).add(currentTime);
        cleanUpRequestCounts(currentTime, rateDuration);

        if (requestCounts.get(compositeKey).size() > rateLimit) {
            logger.info(String.format(ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), compositeKey, rateDuration));
            throw new RateLimitExceededException(String.format(ERROR_MESSAGE, requestAttributes.getRequest().getRequestURI(), compositeKey, rateDuration));
        }
    }

    private void cleanUpRequestCounts(final long currentTime, final long rateDuration) {
        requestCounts.values().forEach(l -> {
            l.removeIf(t -> timeIsTooOld(currentTime, t, rateDuration));
        });
    }

    private boolean timeIsTooOld(final long currentTime, final long timeToCheck, final long rateDuration) {
        return currentTime - timeToCheck > rateDuration;
    }
}
