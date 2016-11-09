package com.github.ledoyen.automocker.internal.jms;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.ledoyen.automocker.Resettable;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockDestination;

public class DestinationManagerResetter implements Resettable {

	@Autowired
	private Set<DestinationManager> destinationManagers;

	@SuppressWarnings("unchecked")
	@Override
	public void reset() {
		for (DestinationManager destinationManager : destinationManagers) {
			Map<String, MockDestination> queues = (Map<String, MockDestination>) ReflectionTestUtils
					.getField(destinationManager, "queues");
			Map<String, MockDestination> topics = (Map<String, MockDestination>) ReflectionTestUtils
					.getField(destinationManager, "topics");
			queues.values()
					.forEach(d -> d.reset());
			topics.values()
					.forEach(d -> d.reset());
		}
	}
}
