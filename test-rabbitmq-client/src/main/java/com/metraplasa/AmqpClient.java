package com.metraplasa;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.remoting.client.AmqpProxyFactoryBean;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class AmqpClient {
    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(AmqpClient.class, args);
        final TestInterface service = run.getBean(TestInterface.class);
        final Test2Interface service2 = run.getBean(Test2Interface.class);

        ExecutorService pool = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 5; i++) {
            final String name = "Shadrach " + i;
            final Integer age = 29;
            final String random = UUID.randomUUID().toString();
            Runnable r = new Runnable() {
                public void run() {
                    System.out.println("this is thread " + " " + name + " " + random);
                    System.out.println(service.makeTest(name) + random);
                }
            };
            pool.execute(r);
        }

        for (int i = 1; i <= 5; i++) {
            final String name = "Shadrach " + i;
            final Integer age = 29;
            final String random = UUID.randomUUID().toString();
            Runnable r = new Runnable() {
                public void run() {
                    System.out.println("this is thread " + " " + name + " " + age + " " + random);
                    System.out.println(service2.buildTest(name, age) + random);
                    System.out.println("this is thread " + " " + name + " " + age + " " + random);
                    System.out.println(service2.doTest(name, age) + random);
                }
            };
            pool.execute(r);
        }
    }

    @Bean
    Queue queue() {
        return new Queue("remotingQueue");
    }

    @Bean
    Queue queue2() {
        return new Queue("remotingQueue2");
    }

    @Bean
    AmqpProxyFactoryBean amqpFactoryBean(AmqpTemplate amqpTemplate) {
        AmqpProxyFactoryBean factoryBean = new AmqpProxyFactoryBean();
        factoryBean.setServiceInterface(TestInterface.class);
        factoryBean.setAmqpTemplate(amqpTemplate);
        return factoryBean;
    }

    @Bean
    AmqpProxyFactoryBean amqpFactory2Bean(AmqpTemplate amqpTemplate2) {
        AmqpProxyFactoryBean factoryBean2 = new AmqpProxyFactoryBean();
        factoryBean2.setServiceInterface(Test2Interface.class);
        factoryBean2.setAmqpTemplate(amqpTemplate2);
        return factoryBean2;
    }

    @Bean
    Exchange directExchange(Queue queue) {
        DirectExchange exchange = new DirectExchange("remoting.exchange");
        BindingBuilder.bind(queue).to(exchange).with("remoting.binding");
        return exchange;
    }

    @Bean
    Exchange directExchange2(Queue queue2) {
        DirectExchange exchange = new DirectExchange("remoting.exchange");
        BindingBuilder.bind(queue2).to(exchange).with("remoting.binding2");
        return exchange;
    }

    @Bean
    RabbitTemplate amqpTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
//        template.setMessageConverter(producerJackson2MessageConverter());
        template.setRoutingKey("remoting.binding");
        template.setExchange("remoting.exchange");
        return template;
    }

//    @Bean
//    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

    @Bean
    RabbitTemplate amqpTemplate2(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
//        template.setMessageConverter(producerJackson2MessageConverter());
        template.setRoutingKey("remoting.binding2");
        template.setExchange("remoting.exchange");
        return template;
    }

}
