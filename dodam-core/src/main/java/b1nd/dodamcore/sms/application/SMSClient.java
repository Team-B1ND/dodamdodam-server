package b1nd.dodamcore.sms.application;

import b1nd.dodamcore.sms.application.dto.req.SendSmsReq;

public interface SMSClient {

    void send(SendSmsReq req);

}