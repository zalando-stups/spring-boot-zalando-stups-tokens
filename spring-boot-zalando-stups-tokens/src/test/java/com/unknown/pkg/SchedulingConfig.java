package com.unknown.pkg;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulingConfig.class);

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }

    /**
     * The real {@link ScheduledExecutorService}.
     * 
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }

    /**
     * Springs {@link TaskExecutor}. To be picked up by
     * ScheduledAnnotationBeanPostProcessor.
     * 
     * @return
     */
    @Bean
    public TaskExecutor taskScheduler() {
        LOG.info("create task-scheduler ...");
        return new ConcurrentTaskScheduler(taskExecutor());
    }

}
