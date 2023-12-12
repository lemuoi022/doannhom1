package com.example.danhom1.Model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;

@Setter
@Getter
public class ResponseMessage {
    private String message;

    @Contract(pure = true)
    public ResponseMessage(String message) {
        this.message = message;
    }

}
