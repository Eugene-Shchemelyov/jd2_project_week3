package com.gmail.eugene.shchemelyov.chest.repository.model;

import com.gmail.eugene.shchemelyov.chest.repository.enums.StatusEnum;

public class Item {
    private Long id;
    private String name;
    private StatusEnum statusEnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }
}
