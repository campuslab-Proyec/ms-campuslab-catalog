package com.example.ms_campuslab_catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDTO {
    private String recipient;
    private String subject;
    private String message;
    private String type;
}