/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bolsadeideas.springboot.app.models.entity;

import java.util.List;

/**
 *
 * @author APerez
 */

public class ResponseSearchOntDto {
   List<ModelGetONTDto> list;
    private Boolean error=false;
    private String message="";
    private String transactionLog =null;

    public List<ModelGetONTDto> getList() {
        return list;
    }

    public void setList(List<ModelGetONTDto> list) {
        this.list = list;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionLog() {
        return transactionLog;
    }

    public void setTransactionLog(String transactionLog) {
        this.transactionLog = transactionLog;
    }
    
    
    
}
