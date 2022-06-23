package com.golovanov.kanban.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MessageHelper {

    public static ResponseEntity<?> okResponse() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    public static ResponseEntity<?> notFound() {
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> internalServerError() {
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
