package ua.thecompany.eloguru.dto;

import java.io.Serializable;

public record TopicDto(Long topicId, String label) implements Serializable {}
