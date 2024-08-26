package com.batman.configuration;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAutoConfiguration
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

	
	private final ApplicationContext context;
	
	@Bean
    SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        log.info("Configuring Job factory ...");

        jobFactory.setApplicationContext(context);
        return jobFactory;
    }
	
	@Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource quartzDataSource() {
        return DataSourceBuilder.create().build();
    }
	
	@Bean
	SchedulerFactoryBean scheduler(DataSource quartzDataSource) {
		

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
        
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;

	}

}
