package com.endpoint.patientservice.service;


import com.endpoint.patientservice.dto.PatientRequestDTO;
import com.endpoint.patientservice.dto.PatientResponseDTO;
import com.endpoint.patientservice.exception.EmailAlreadyExistexception;
import com.endpoint.patientservice.exception.PatientNotFoundException;
import com.endpoint.patientservice.grpc.BillingServiceGrpcClient;
import com.endpoint.patientservice.mapper.PatientMapper;
import com.endpoint.patientservice.model.Patient;
import com.endpoint.patientservice.repository.PatientRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepo patientRepo;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepo.findAll();

        return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepo.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistexception("A patient with this email " + patientRequestDTO.getEmail() + " already exists");
        }

        Patient patientToBeAdded = PatientMapper.toPatient(patientRequestDTO);
        Patient newPatient = patientRepo.save(patientToBeAdded);

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        return PatientMapper.toPatientResponseDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepo.findById(id).orElseThrow(() -> new PatientNotFoundException("patient not found"));

        if (patientRepo.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistexception("A patient with this email " + patientRequestDTO.getEmail() + " already exists");
        }

        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient = patientRepo.save(patient);

        return PatientMapper.toPatientResponseDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepo.deleteById(id);

    }
}
