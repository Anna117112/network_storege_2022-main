package com.geekbrains.cloud;

import lombok.Data;
@Data

public class Auth implements CloudMessage{

    private boolean auth;

    public Auth(boolean auth) {
        this.auth = auth;
    }
}
