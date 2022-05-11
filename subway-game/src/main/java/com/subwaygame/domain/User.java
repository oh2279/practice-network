package com.subwaygame.domain;

import com.subwaygame.constant.UserLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter @Setter
@Entity
public class User {

    @Id
    private long id;
    private String username;
    private int age;
    private UserLevel userLevel;
}
