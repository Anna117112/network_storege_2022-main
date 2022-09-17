package com.geekbrains.cloud;

import lombok.Data;

@Data
public class Responce implements CloudMessage{
    private final String responceLogin;
    private final String responcePassword;
}
