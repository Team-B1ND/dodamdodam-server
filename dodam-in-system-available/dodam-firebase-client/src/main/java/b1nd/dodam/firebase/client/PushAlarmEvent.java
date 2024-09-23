package b1nd.dodam.firebase.client;

public interface PushAlarmEvent {
    String pushToken();

    String title();

    String body();
}