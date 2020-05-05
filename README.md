Simulation-Framework
======================
Java Framework for Multi-Threading Test

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.salvatore-lanzini/simulation-framework/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.salvatore-lanzini/simulation-framework)

##Description

A collection of interfaces to make very easy write Multi-Threading and stress tests.
When we have to test a component like WebService, KafkaConsumer, FtpReceiver, MqttSubscriber
we are ever worry about how to write a significant stress tests in order to verify if our component
do his job.
With Simulation-Framework, write massives stress tests is very easy.
We have to worry only define a creator of the message (Factory of a simple java Pojo), the way when we want
publish the message (a clientHttp, a producer Kafka, a Publisher Mqtt etc) and finally what is the
configuration about test (number of threads, number of messages or time-duration, delay intra-message etc)

In the framework we have three simple functional interfaces that describe these concepts:

1) A MessageFactory
2) A MessagePublisher
3) A ConfigurationExecutor

With framework we can make intensive stress test, writing really few lines of code

Write less, do more!

##Require

Jdk 1.8 or +

#Usage

Framework provides a concrete class in order to create a simulation flow very easy.
SimulationFlowBuilder provides three methods to define the messageFactory (tipically random way), the publisher and
the configurationExecutor of the simulation.
Furthemore, the framework provides a useful Template to obtain instances of configurationExecutor.
In this way, develepor have to worry about only how to create the message and how to publish.


Example 1.1 - 5 Threads sends 5 messages each with 1 second delay intra-messages

```java
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

```java
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

```java

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
                        new ConfigurationExecutorTemplate<String>().
                                executeWithMessages(50,10, 5000,messageFactory,publisher))
                .build()
                .simulate();

```

#License
GNU General Public License v3.0: 
[https://www.gnu.org/licenses/gpl-3.0.html](https://www.gnu.org/licenses/gpl-3.0.html)
