# kafka-producer-wikimedia
To learn Kafka

## Start Zookeeper
    zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties

if installed using brew,
    
    zookeeper-server-start /opt/homebrew/etc/kafka/zookeeper.properties

## Start Kafka server
    kafka-server-start /usr/local/etc/kafka/server.properties

if installed using brew,

    kafka-server-start /opt/homebrew/etc/kafka/server.properties

## Create a topic
    kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic wikimedia.recentChanges

### Read from the topic
    kafka-console-consumer --bootstrap-server localhost:9092 --topic wikimedia.recentChanges