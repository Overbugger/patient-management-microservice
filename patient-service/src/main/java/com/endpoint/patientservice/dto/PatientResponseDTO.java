package com.endpoint.patientservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {

    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
}
