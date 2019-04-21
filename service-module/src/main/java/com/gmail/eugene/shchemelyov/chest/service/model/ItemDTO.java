package com.gmail.eugene.shchemelyov.chest.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.gmail.eugene.shchemelyov.chest.service.constant.ItemConstant.MAX_NAME_SIZE;

public class ItemDTO {
    private Long id;
    @NotNull
    @Size(max = MAX_NAME_SIZE)
    private String name;
    @NotNull
    private String status;
    private Boolean isDeleted;

    public ItemDTO(Long id,
                   String name,
                   String status,
                   Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.isDeleted = isDeleted;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
