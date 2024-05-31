import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ConditionSubscriber {
    public static void main(String[] args) {
        String broker = "tcp://test.mosquitto.org:1883";
        String topic = "weather/condition";
        String clientId = "ConditionSubscriber";

        try {
            MqttClient client = new MqttClient(broker, clientId);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Received weather condition update: " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
            client.connect();
            client.subscribe(topic);
            System.out.println("Subscribed to weather condition updates...");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

