Simulation-Framework
======================
Java Framework for Multi-Threading Test

[![Maven Central](https://img.shields.io/maven-central/v/com.github.salvatore-lanzini/simulation-framework.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.salvatore-lanzini%22%20AND%20a:%22simulation-framework%22)

Description
=======
A collection of interfaces to make very easy write Multi-Threading and stress tests.
When we have to test a component like WebService, KafkaConsumer, FtpReceiver, MqttSubscriber
we are worry about write a significant stress tests in order to verify if our components
does their job.
With Simulation-Framework, write massive stress tests is very easy.
We have to worry only define a creator of the message (Factory of a simple java Pojo), the way when we want
publish the message (a clientHttp, a producer Kafka, a Publisher Mqtt etc) and finally what is the
configuration about test (number of threads, number of messages or time-duration, delay intra-message etc)

In the framework we have three simple functional interfaces that describe these concepts:

1) A MessageFactory
2) A MessagePublisher
3) A ConfigurationExecutor

With framework we can make intensive stress test, writing really few lines of code

Write less, do more!

Require
=======
Jdk 1.8 or +

Usage
=======
Framework provides a concrete class in order to create a simulation flow very easy.
SimulationFlowBuilder provides three methods to define the messageFactory (tipically random way), the publisher and
the configurationExecutor of the simulation.
Furthemore, the framework provides a useful Template to obtain instances of configurationExecutor.
In this way, develepor have to worry about only how to create the message and how to publish.


Example 1.1 - 5 Threads sends 5 messages each with 1 second delay intra-messages

```
SimulatorFlowBuilder.flow()
                .message( ()-> new Long(System.currentTimeMillis()).toString() ) //define a messageFactory
                .<String>publish( message -> System.out.println(message)) //define a messagePublisher
                .<String>configure((messageFactory, publisher) ->
                        new ConfigurationExecutorTemplate<String>().
                                executeWithMessages(5,5, 1000,messageFactory,publisher)) //define a configurationExecutor very easy with ConfigurationExecutorTemplate
                .build()
                .simulate();
```             
   
Example 1.2 - 5 Threads sends messages for 10 minutes with 1 second delay intra-messages

```
SimulatorFlowBuilder.flow()
                .message( ()-> new Long(System.currentTimeMillis()).toString() ) 
                .<String>publish( message -> System.out.println(message)) 
                .<String>configure((messageFactory, publisher) ->
                        new ConfigurationExecutorTemplate<String>().
                                executeWithTimeRangeMinutes(5,10, 1000,messageFactory,publisher)) 
                .build()
                .simulate();
```  

Example 2 - 50 Threads invokes a WebService, 10 times each wich 5 seconds delay each time

```

//using Spring RestTemplate for Http POST Request

SimulatorFlowBuilder.flow()
                .message( ()-> {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(UUID.randomUUID().toString());
                    transaction.setAmount( ThreadLocalRandom.current().nextDouble(0.0, 10000.0) );
                    return transaction;
                } )
                .<Transaction>publish( message -> {
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Transaction> request =
                            new HttpEntity<Transaction>(message, headers);
                    ResponseEntity<Transaction> responseEntityPerson = restTemplate.
                            postForEntity(webServiceTransactionUrl, request, Transaction.class);
                })
                .<Transaction>configure((messageFactory, publisher) ->
                        new ConfigurationExecutorTemplate<Transaction>().
                                executeWithMessages(50,10, 5000,messageFactory,publisher))
                .build()
                .simulate();

```

Version 1.0.2
======

From version 1.0.2 framework provides so many templates to make more easy the way to publish messages.
Framework provides five built-in PublisherTemplate for the most usage application protocol messaging:

- HttpPublisherTemplate - for Http requests
- KafkaPublisherTemplate - for Apache Kafka producers
- MqttPublisherTemplate - for Mqtt publishers
- FtpPublisherTemplate - for Ftp storing files
- FileSystemPublisherTemplate - to create and store files on own filesystem

Every Template provides static methods to send,publish,store, and save file.

In order to make very easy to set-up a connection with Servers or broker, framework provides another
Template, PublisherTemplateConnectionFactory, wich has got three static methods (mqtt,kafka,ftp) to
connect stress tests to the servers.

To set-up a connection is been added another method to SimulationFlowBuilder, connect, wich user
can defines the type of connection to set-up.

Example with Apache Kafka

```
SimulationFlowBuilder.flow()
.message( ... )
.connect( () -> PublisherTemplateConnectionFactory.kafka("localhost:9092") )
.<String>publish(message -> {
                    try {
                        KafkaPublisherTemplate.send("test",message);
                    } catch (KafkaPublisherException e) {
                        e.printStackTrace();
                    }
                })
```
Note that we use in new FlowBuilder method, connect, the PublisherTemplateConnectionFactory
to set-up connection to the Apache Kafka broker and into publish method the KafkaPublisherTemplate
to send a message to a topic.

With latest release we rewrite stress tests in Example 2 with HttpPublisherTemplate

```

SimulatorFlowBuilder.flow()
                .message( ()-> {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(UUID.randomUUID().toString());
                    transaction.setAmount( ThreadLocalRandom.current().nextDouble(0.0, 10000.0) );
                    return transaction;
                } )
                .<Transaction>publish( message -> HttpPublisherTemplate.post("http://localhost:8080/test",message))
                .<Transaction>configure((messageFactory, publisher) ->
                        new ConfigurationExecutorTemplate<Transaction>().
                                executeWithMessages(50,10, 5000,messageFactory,publisher))
                .build()
                .simulate();

```

Or publish a message on a Mqtt topic

```
.connect( () -> PublisherTemplateConnectionFactory.mqtt("localhost",1883,"XXX","XXX") )
                .<Transaction>publish( message -> {
                    try {MqttPublisherTemplate.send(message,"TEST",0);
                    } catch (MqttPublisherException e) { e.printStackTrace(); }
                })
```

Write less! Do More!

License
======
GNU General Public License v3.0: 
[https://www.gnu.org/licenses/gpl-3.0.html](https://www.gnu.org/licenses/gpl-3.0.html)
