package com.project.webserver.model.dali;

public class UpdateControllerResponse {
    UpdateStatus status;
    String message;
    String nextId;

    public UpdateControllerResponse() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public void setStatus(UpdateStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getNextId() {
        return nextId;
    }

    public UpdateStatus getStatus() {
        return status;
    }

    private UpdateControllerResponse(UpdateControllerResponseBuilder builder) {
        this.message = builder.message;
        this.nextId = builder.nextId;
        this.status = builder.status;
    }

    public static class UpdateControllerResponseBuilder {

        //required
        String message;
        UpdateStatus status;

        //optional
        String nextId;

        public UpdateControllerResponseBuilder() {

        }

        public UpdateControllerResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public UpdateControllerResponseBuilder status(UpdateStatus status) {
            this.status = status;
            return this;
        }

        public UpdateControllerResponseBuilder nextId(String nextId) {
            this.nextId = nextId;
            return this;
        }

        public UpdateControllerResponse build() {
            return new UpdateControllerResponse(this);
        }
    }
}
