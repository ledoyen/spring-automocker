package com.github.ledoyen.automocker.internal.jms;

import java.util.Optional;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ErrorHandler;

import com.github.ledoyen.automocker.Resettable;
import com.github.ledoyen.automocker.jms.JmsMock;

public class ErrorHandlerMock implements ApplicationContextAware, InitializingBean, ErrorHandler, Resettable {

	private ApplicationContext applicationContext;
	private Optional<ErrorHandler> delegate;

	private Throwable lastCatched = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		AbstractJmsListenerContainerFactory<?> jmsListenerContainerFactory = applicationContext
				.getBean(AbstractJmsListenerContainerFactory.class);
		delegate = Optional.ofNullable(
				(ErrorHandler) ReflectionTestUtils.getField(jmsListenerContainerFactory, "errorHandler"));
		jmsListenerContainerFactory.setErrorHandler(this);
		ReflectionTestUtils.setField(applicationContext.getBean(JmsMock.class), "errorHandlerMock", this);
	}

	@Override
	public void handleError(Throwable t) {
		lastCatched = t;
		delegate.ifPresent(e -> e.handleError(t));
	}

	public Optional<Throwable> getLastCatched() {
		return Optional.ofNullable(lastCatched);
	}

	@Override
	public void reset() {
		this.lastCatched = null;
	}
}
