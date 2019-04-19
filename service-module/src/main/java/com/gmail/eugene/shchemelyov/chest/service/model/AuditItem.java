package com.gmail.eugene.shchemelyov.chest.service.model;

import com.gmail.eugene.shchemelyov.chest.service.enums.ActionEnum;

public class AuditItem {
    private Long id;
    private ActionEnum action;
    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
