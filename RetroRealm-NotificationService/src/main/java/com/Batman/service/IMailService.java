package com.Batman.service;

import com.Batman.mail.EmailDetails;

public interface IMailService {
    void sendMail(EmailDetails emailDetails);
}
