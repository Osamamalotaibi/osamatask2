package com.example.taskapi.aop;

import io.micronaut.aop.InterceptorBean;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Singleton
@InterceptorBean(Loggable.class)
public class LoggingInterceptor implements MethodInterceptor<Object, Object> {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        String method = context.getDeclaringType().getSimpleName() + "#" + context.getMethodName();

        LOG.debug("BEFORE {}", method);
        long start = System.nanoTime();

        try {
            Object result = context.proceed();
            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            LOG.info("{} took {} ms", method, ms);
            LOG.debug("AFTER {}", method);
            return result;
        } catch (Exception ex) {
            LOG.error("Method {} threw exception: {}", method, ex.getClass().getSimpleName(), ex);
            throw ex;
        }
    }
}
