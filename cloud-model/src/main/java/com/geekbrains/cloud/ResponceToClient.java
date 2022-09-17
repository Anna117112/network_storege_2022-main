package com.geekbrains.cloud;

import lombok.Data;

@Data

public class ResponceToClient implements CloudMessage {
    private boolean responce;

    public ResponceToClient(boolean responce) {
        this.responce = responce;
    }
}
