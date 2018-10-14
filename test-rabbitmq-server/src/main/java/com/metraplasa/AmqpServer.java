package com.metraplasa;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.remoting.service.AmqpInvokerServiceExporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class AmqpServer {
    public static void main(String[] args) {
        SpringApplication.run(AmqpServer.class, args);
    }

    @Bean
    TestInterface testService() {
        return new TestImpl();
    }

    @Bean
    Test2Interface test2Service() {
        return new Test2Impl();
    }

    @Bean
    Queue queue() {
        return new Queue("remotingQueue");
    }
//
//    @Bean
//    public MessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    Queue queue2() {
        return new Queue("remotingQueue2");
    }

    @Bean
    AmqpInvokerServiceExporter exporter(TestInterface testService, AmqpTemplate template) {
        AmqpInvokerServiceExporter exporter = new AmqpInvokerServiceExporter();
        exporter.setServiceInterface(TestInterface.class);
        exporter.setService(testService);
        exporter.setAmqpTemplate(template);
        return exporter;
    }

    @Bean
    AmqpInvokerServiceExporter exporter2(Test2Interface test2Service, AmqpTemplate template2) {
        AmqpInvokerServiceExporter exporter2 = new AmqpInvokerServiceExporter();
        exporter2.setServiceInterface(Test2Interface.class);
        exporter2.setService(test2Service);
        exporter2.setAmqpTemplate(template2);
        return exporter2;
    }

    @Bean
    SimpleMessageListenerContainer listener(ConnectionFactory factory, AmqpInvokerServiceExporter exporter, Queue queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
        container.setMessageListener(exporter);
        container.setQueueNames(queue.getName());
//        container.setMessageConverter(jsonMessageConverter());
        return container;
    }

    @Bean
    SimpleMessageListenerContainer listener2(ConnectionFactory factory, AmqpInvokerServiceExporter exporter2, Queue queue2) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(factory);
        container.setMessageListener(exporter2);
        container.setQueueNames(queue2.getName());
//        container.setMessageConverter(jsonMessageConverter());
        return container;
    }
}
