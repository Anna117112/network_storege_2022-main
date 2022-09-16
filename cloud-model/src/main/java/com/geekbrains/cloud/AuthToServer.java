package com.geekbrains.cloud;

import lombok.Data;

@Data

public class AuthToServer implements CloudMessage {
    private final String nik;
}
