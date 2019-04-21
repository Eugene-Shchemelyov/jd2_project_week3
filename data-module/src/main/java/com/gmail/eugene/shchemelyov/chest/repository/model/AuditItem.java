package com.gmail.eugene.shchemelyov.chest.repository.model;

import java.util.Date;

public class AuditItem {
    private Long id;
    private String action;
    private Long itemId;
    private Date date;
    private Boolean deleted;

    public AuditItem(Long id,
                     String action,
                     Long itemId,
                     Date date,
                     Boolean deleted) {
        this.id = id;
        this.action = action;
        this.itemId = itemId;
        this.date = date;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
