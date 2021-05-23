package com.dataart.playme.service;

import java.io.IOException;
import java.util.Map;

public interface MailService {

    void sendThroughRemote(String recipient, String subject, String mailTemplateFilepath,
                                  Map<String, String> placeholders) throws IOException;
}
