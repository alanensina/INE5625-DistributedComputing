package model;

import enumeration.Status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Response implements Serializable {

    private Status status;
    private final List<String> messages = new ArrayList<>();

    public Response() {
    }

    public Status getStatus() {
        return status;
    }

    public Response setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Collection<String> getMessages() {
        return messages;
    }

    public Response addMessage(String message) {
        this.messages.add(message);
        return this;
    }

    public Response addMessages(String... message) {
        this.messages.addAll(Arrays.asList(message));
        return this;
    }
}
