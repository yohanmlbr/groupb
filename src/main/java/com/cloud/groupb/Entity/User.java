package com.cloud.groupb.Entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String birthDay;
    private Position position;
}
