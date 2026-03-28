package com.educativa.intranet.service.academico;

import com.educativa.intranet.dto.MatriculaResponseDTO;
import com.educativa.intranet.dto.CursoResponseDTO;
import com.educativa.intranet.repository.MatriculaRepository;
import com.educativa.intranet.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaConsultaServiceImpl implements IMatriculaConsultaService {

    private final MatriculaRepository matriculaRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaMapper matriculaMapper; // SRP: Mapeo delegado

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> verListaDeSalon(Long cursoId) {
        return matriculaRepository.findByCursoIdAndActivoTrue(cursoId).stream()
            .map(matriculaMapper::convertirHaciaDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> listarTodosLosCursos() {
        return cursoRepository.findAll().stream()
                .map(curso -> CursoResponseDTO.builder()
                        .id(curso.getId())
                        .nombre(curso.getNombre())
                        .grado(curso.getGrado())
                        .seccion(curso.getSeccion())
                        .anio(curso.getAnio())
                        .build())
                .collect(Collectors.toList());
    }
}
