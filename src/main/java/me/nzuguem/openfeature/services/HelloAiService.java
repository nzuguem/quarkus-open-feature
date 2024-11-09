package me.nzuguem.openfeature.services;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface HelloAiService {

    String hello(@UserMessage String message);
}