package com.github.ledoyen.automocker.examples;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.ledoyen.automocker.api.rabbitmq.RabbitMqMock;
import com.github.ledoyen.automocker.base.Automocker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RabbitApplication.class)
@Automocker
public class RabbitApplicationTest {

    @Autowired
    private Sender sender;
    
    @Autowired
    private Receiver receiver;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqMock rabbitMqMock;
    
    @Test
    public void broker_is_available_and_consistent() throws InterruptedException {
        sender.send();
        TimeUnit.MILLISECONDS.sleep(100L);
        assertThat(receiver.getMessages()).hasSize(1);
    }

    @Test
    public void broker_supports_reject() throws InterruptedException {
        rabbitTemplate.convertAndSend("", RabbitApplication.queueName, "reject this message");
        TimeUnit.MILLISECONDS.sleep(100L);
        assertThat(receiver.getMessages()).hasSize(0);
        assertThat(rabbitMqMock.getQueue(RabbitApplication.queueName)).containsOnly("reject this message");
    }
}