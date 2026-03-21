package com.educativa.intranet.controller;

import com.educativa.intranet.model.LogAcceso;
import com.educativa.intranet.repository.LogAccesoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LogAccesoRepository logAccesoRepository;

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LogAcceso>> verLogs() {
        return ResponseEntity.ok(logAccesoRepository.findAll());
    }
}
