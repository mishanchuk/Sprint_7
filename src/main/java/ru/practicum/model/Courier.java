package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Courier {
    @JsonProperty(required = true)  // Указываем, что поле обязательно при сериализации
    private String login;

    @JsonProperty(required = true)  // Указываем, что поле обязательно при сериализации
    private String password;

    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)  // Будет включено только если не null
    private Integer id;


    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
    public Courier setLogin(String login) {
        this.login = login;
        return this;
    }


    public Courier setPassword(String password) {
        this.password = password;
        return  this;
    }


    public Courier setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Courier setId(Integer id) {
        this.id = id;
        return this;
    }


}
