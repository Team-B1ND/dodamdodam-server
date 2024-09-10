package b1nd.dodam.restapi.support.pushalarm;

import b1nd.dodam.firebase.client.PushAlarmEvent;

public record ApprovalAlarmEvent(String pushToken, String title, String body) implements PushAlarmEvent {}