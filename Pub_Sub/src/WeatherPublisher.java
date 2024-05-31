
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherPublisher {
    private static final String BROKER = "tcp://test.mosquitto.org:1883";
    private static final String[] WEATHER_CONDITIONS = {"Sunny", "Cloudy", "Rainy", "Snowy", "Foggy"};
    private static final int PUBLISH_INTERVAL = 5000; // 5 seconds

    public static void main(String[] args) {
        try {
            MqttClient client = new MqttClient(BROKER, "WeatherPublisher");
            client.connect();

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    publishWeatherUpdate(client);
                }
            }, 0, PUBLISH_INTERVAL);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void publishWeatherUpdate(MqttClient client) {
        Random random = new Random();

        String condition = WEATHER_CONDITIONS[random.nextInt(WEATHER_CONDITIONS.length)];
        String temperature = (random.nextInt(30 - (-10)) + (-10)) + "°C";

        try {
            MqttMessage conditionMessage = new MqttMessage(condition.getBytes());
            conditionMessage.setQos(2);
            client.publish("weather/condition", conditionMessage);

            MqttMessage temperatureMessage = new MqttMessage(temperature.getBytes());
            temperatureMessage.setQos(2);
            client.publish("weather/temperature", temperatureMessage);

            System.out.println("Published weather update: Condition: " + condition + ", Temperature: " + temperature);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
