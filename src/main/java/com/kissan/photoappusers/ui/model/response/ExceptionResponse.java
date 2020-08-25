package com.kissan.photoappusers.ui.model.response;

import java.time.OffsetDateTime;

public class ExceptionResponse {
    private OffsetDateTime dateTime;
    private String messageCode;
    private String messageDescription;

    public ExceptionResponse(OffsetDateTime dateTime, String messageCode, String messageDescription) {
        this.dateTime = dateTime;
        this.messageCode = messageCode;
        this.messageDescription = messageDescription;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }
}
