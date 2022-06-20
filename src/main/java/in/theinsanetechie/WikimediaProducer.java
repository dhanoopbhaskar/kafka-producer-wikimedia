package in.theinsanetechie;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaProducer {

    public static void main(String[] args) throws InterruptedException {
        // bootstrap server
        String bootstrapServers = "127.0.0.1:9092";

        // Kafka Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // use safe producer configs (Kafka <= 2.8)
        // enable idempotence
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // same as setting -1
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE)); // same as setting -1

        // Kafka Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        String topic = "wikimedia.recentChanges";

        String urlString = "https://stream.wikimedia.org/v2/stream/recentchange";
        EventHandler eventHandler = new WikimediaEventHandler(producer, topic);
        EventSource.Builder builder = new EventSource.Builder(eventHandler, URI.create(urlString));
        EventSource eventSource = builder.build();

        // starts in another thread
        eventSource.start();

        // we produce for 10 minutes and block the program until then
        TimeUnit.MINUTES.sleep(10);
    }
}
