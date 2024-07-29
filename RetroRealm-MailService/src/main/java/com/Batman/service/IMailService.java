package com.Batman.service;

import com.Batman.enums.Entity;

public interface IMailService {
    void sendMail(Integer entityID,Entity entityName);
}
