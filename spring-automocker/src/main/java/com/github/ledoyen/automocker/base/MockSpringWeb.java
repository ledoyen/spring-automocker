package com.github.ledoyen.automocker.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.ledoyen.automocker.api.AfterBeanRegistration;
import com.github.ledoyen.automocker.api.AfterBeanRegistrationExecutable;
import com.github.ledoyen.automocker.api.ExtendedBeanDefinitionRegistry;
import com.github.ledoyen.automocker.tools.Classes;

/**
 * Setup {@link MockMvc} for available {@link Controller Controllers}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@AfterBeanRegistration(MockSpringWeb.MockSpringWebExecutable.class)
public @interface MockSpringWeb {

    class MockSpringWebExecutable implements AfterBeanRegistrationExecutable<MockSpringWeb> {

        @Override
        public void execute(MockSpringWeb annotation, ExtendedBeanDefinitionRegistry extendedBeanDefinitionRegistry) {
            if (Classes.isPresent("org.springframework.test.web.servlet.MockMvc")) {
                extendedBeanDefinitionRegistry.registerBeanDefinition("mockMvc", new RootBeanDefinition(MockMvcFactory.class));
            }
        }
    }

    class MockMvcFactory implements FactoryBean<MockMvc>, ApplicationContextAware, InitializingBean {

        private static final Logger LOGGER = LoggerFactory.getLogger(MockMvcFactory.class);

        private ApplicationContext applicationContext;
        private MockMvc singleton;

        @Override
        public MockMvc getObject() throws Exception {
            return singleton;
        }

        @Override
        public Class<?> getObjectType() {
            return MockMvc.class;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            if(Classes.isPresent("org.springframework.web.context.WebApplicationContext") && isWebContext()) {
                this.singleton = MockMvcBuilders.webAppContextSetup((WebApplicationContext) applicationContext).build();
                LOGGER.debug("Setting up " + MockMvc.class.getSimpleName() + " for WebApplicationContext");
            } else {
                Map<String, Object> controllersByName = applicationContext.getBeansWithAnnotation(Controller.class);
                if(controllersByName.size() > 0) {
                    this.singleton = MockMvcBuilders.standaloneSetup(controllersByName.values()
                            .toArray())
                            .build();
                    LOGGER.debug("Setting up " + MockMvc.class.getSimpleName() + " for controllers "
                            + controllersByName.keySet());
                } else {
                    this.singleton = null;
                }
            }
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        public boolean isWebContext() {
            return applicationContext instanceof WebApplicationContext;
        }
    }
}
