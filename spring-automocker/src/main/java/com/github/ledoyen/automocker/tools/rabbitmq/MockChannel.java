package com.github.ledoyen.automocker.tools.rabbitmq;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Command;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.FlowListener;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.Method;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class MockChannel implements Channel {
    private final MockNode node;

    public MockChannel(MockNode node) {
        this.node = node;
    }

    @Override
    public int getChannelNumber() {
        return 0;
    }

    @Override
    public Connection getConnection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException, TimeoutException {

    }

    @Override
    public void close(int closeCode, String closeMessage) throws IOException, TimeoutException {

    }

    @Override
    public boolean flowBlocked() {
        return false;
    }

    @Override
    public void abort() throws IOException {

    }

    @Override
    public void abort(int closeCode, String closeMessage) throws IOException {

    }

    @Override
    public void addReturnListener(ReturnListener listener) {

    }

    @Override
    public boolean removeReturnListener(ReturnListener listener) {
        return false;
    }

    @Override
    public void clearReturnListeners() {

    }

    @Override
    public void addFlowListener(FlowListener listener) {

    }

    @Override
    public boolean removeFlowListener(FlowListener listener) {
        return false;
    }

    @Override
    public void clearFlowListeners() {

    }

    @Override
    public void addConfirmListener(ConfirmListener listener) {

    }

    @Override
    public boolean removeConfirmListener(ConfirmListener listener) {
        return false;
    }

    @Override
    public void clearConfirmListeners() {

    }

    @Override
    public Consumer getDefaultConsumer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultConsumer(Consumer consumer) {

    }

    @Override
    public void basicQos(int prefetchSize, int prefetchCount, boolean global) throws IOException {
        
    }

    @Override
    public void basicQos(int prefetchCount, boolean global) throws IOException {
        
    }

    @Override
    public void basicQos(int prefetchCount) throws IOException {
        
    }

    @Override
    public void basicPublish(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body) throws IOException {
        basicPublish(exchange, routingKey, false, props, body);
    }

    @Override
    public void basicPublish(String exchange, String routingKey, boolean mandatory, AMQP.BasicProperties props, byte[] body) throws IOException {
        basicPublish(exchange, routingKey, false, false, props, body);
    }

    @Override
    public void basicPublish(String exchange, String routingKey, boolean mandatory, boolean immediate, AMQP.BasicProperties props, byte[] body) throws IOException {
        node.basicPublish(exchange, routingKey, mandatory, immediate, props, body);
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, String type) throws IOException {
        return exchangeDeclare(exchange, type, false, true, false, Collections.emptyMap());
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, BuiltinExchangeType type) throws IOException {
        return exchangeDeclare(exchange, type, false, true, false, Collections.emptyMap());
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable) throws IOException {
        return exchangeDeclare(exchange, type, durable, true, false, Collections.emptyMap());
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable) throws IOException {
        return exchangeDeclare(exchange, type, durable, true, false, Collections.emptyMap());
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments) throws IOException {
        return exchangeDeclare(exchange, type, durable, autoDelete, false, arguments);
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, Map<String, Object> arguments) throws IOException {
        return exchangeDeclare(exchange, type, durable, autoDelete, false, arguments);
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments) throws IOException {
        return node.exchangeDeclare(exchange, type, durable, autoDelete, internal, arguments);
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments) throws IOException {
        return exchangeDeclare(exchange, type.getType(), durable, autoDelete, internal, arguments);
    }

    @Override
    public void exchangeDeclareNoWait(String exchange, String type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void exchangeDeclareNoWait(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Exchange.DeclareOk exchangeDeclarePassive(String name) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Exchange.DeleteOk exchangeDelete(String exchange, boolean ifUnused) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void exchangeDeleteNoWait(String exchange, boolean ifUnused) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Exchange.DeleteOk exchangeDelete(String exchange) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Exchange.BindOk exchangeBind(String destination, String source, String routingKey) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Exchange.BindOk exchangeBind(String destination, String source, String routingKey, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void exchangeBindNoWait(String destination, String source, String routingKey, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Exchange.UnbindOk exchangeUnbind(String destination, String source, String routingKey) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Exchange.UnbindOk exchangeUnbind(String destination, String source, String routingKey, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void exchangeUnbindNoWait(String destination, String source, String routingKey, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.DeclareOk queueDeclare() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws IOException {
        return node.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
    }

    @Override
    public void queueDeclareNoWait(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.DeclareOk queueDeclarePassive(String queue) throws IOException {
        if(!node.getQueue(queue).isPresent()) {
            throw new IOException("No queue named " + queue);
        }
        return null;
    }

    @Override
    public AMQP.Queue.DeleteOk queueDelete(String queue) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.DeleteOk queueDelete(String queue, boolean ifUnused, boolean ifEmpty) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void queueDeleteNoWait(String queue, boolean ifUnused, boolean ifEmpty) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.BindOk queueBind(String queue, String exchange, String routingKey) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.BindOk queueBind(String queue, String exchange, String routingKey, Map<String, Object> arguments) throws IOException {
        return node.queueBind(queue, exchange, routingKey, arguments);
    }

    @Override
    public void queueBindNoWait(String queue, String exchange, String routingKey, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.UnbindOk queueUnbind(String queue, String exchange, String routingKey) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.UnbindOk queueUnbind(String queue, String exchange, String routingKey, Map<String, Object> arguments) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Queue.PurgeOk queuePurge(String queue) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public GetResponse basicGet(String queue, boolean autoAck) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void basicAck(long deliveryTag, boolean multiple) throws IOException {
//        throw new UnsupportedOperationException();
    }

    @Override
    public void basicNack(long deliveryTag, boolean multiple, boolean requeue) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void basicReject(long deliveryTag, boolean requeue) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String basicConsume(String queue, Consumer callback) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String basicConsume(String queue, boolean autoAck, Consumer callback) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String basicConsume(String queue, boolean autoAck, Map<String, Object> arguments, Consumer callback) throws IOException {
        return basicConsume(queue, autoAck, "", false, false, arguments, callback);
    }

    @Override
    public String basicConsume(String queue, boolean autoAck, String consumerTag, Consumer callback) throws IOException {
        return basicConsume(queue, autoAck, consumerTag, false, false, Collections.emptyMap(), callback);
    }

    @Override
    public String basicConsume(String queue, boolean autoAck, String consumerTag, boolean noLocal, boolean exclusive, Map<String, Object> arguments, Consumer callback) throws IOException {
        return node.basicConsume(queue, autoAck, consumerTag, noLocal, exclusive, arguments, callback);
    }

    @Override
    public void basicCancel(String consumerTag) throws IOException {
//        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Basic.RecoverOk basicRecover() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Basic.RecoverOk basicRecover(boolean requeue) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Tx.SelectOk txSelect() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Tx.CommitOk txCommit() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Tx.RollbackOk txRollback() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AMQP.Confirm.SelectOk confirmSelect() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getNextPublishSeqNo() {
        return 0;
    }

    @Override
    public boolean waitForConfirms() throws InterruptedException {
        return false;
    }

    @Override
    public boolean waitForConfirms(long timeout) throws InterruptedException, TimeoutException {
        return false;
    }

    @Override
    public void waitForConfirmsOrDie() throws IOException, InterruptedException {

    }

    @Override
    public void waitForConfirmsOrDie(long timeout) throws IOException, InterruptedException, TimeoutException {

    }

    @Override
    public void asyncRpc(Method method) throws IOException {

    }

    @Override
    public Command rpc(Method method) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long messageCount(String queue) throws IOException {
        return 0;
    }

    @Override
    public long consumerCount(String queue) throws IOException {
        return 0;
    }

    @Override
    public void addShutdownListener(ShutdownListener listener) {

    }

    @Override
    public void removeShutdownListener(ShutdownListener listener) {

    }

    @Override
    public ShutdownSignalException getCloseReason() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void notifyListeners() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
