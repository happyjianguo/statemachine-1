package com.wenyu7980.statemachine;

public class Review {

    private ReviewStatus status;

    public Review(ReviewStatus status) {
        this.status = status;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

}
