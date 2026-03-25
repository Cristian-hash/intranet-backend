package com.educativa.intranet.service.perfil;

import com.educativa.intranet.dto.PadrePerfilCreateDTO;
import com.educativa.intranet.dto.PadrePerfilResponseDTO;
import com.educativa.intranet.model.Padre;
import com.educativa.intranet.model.Rol;
import com.educativa.intranet.model.Usuario;
import com.educativa.intranet.repository.PadreRepository;
import com.educativa.intranet.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PadrePerfilServiceImpl implements IPadrePerfilService {

    private final UsuarioRepository usuarioRepository;
    private final PadreRepository padreRepository;

    @Override
    @Transactional
    public PadrePerfilResponseDTO construirPerfilPadre(PadrePerfilCreateDTO dto) {
        Usuario usuario = buscarYValidarUsuario(dto.getUsuarioId(), Rol.PADRE);

        Padre padreFisico = Padre.builder()
                .usuario(usuario)
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();

        padreRepository.save(padreFisico);
        return mapearADTO(padreFisico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PadrePerfilResponseDTO> listarTodosLosPadresCompletos() {
        return padreRepository.findAll().stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    private Usuario buscarYValidarUsuario(Long usuarioId, Rol rolEsperado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Ese usuario matriz no existe."));

        if (!usuario.getRol().equals(rolEsperado)) {
            throw new RuntimeException("Regla de RRHH: Intentas crear un perfil de " + rolEsperado + " sobre un usuario que es " + usuario.getRol());
        }
        return usuario;
    }

    private PadrePerfilResponseDTO mapearADTO(Padre pa) {
        return PadrePerfilResponseDTO.builder()
                .perfilId(pa.getId())
                .usuarioId(pa.getUsuario().getId())
                .nombre(pa.getUsuario().getNombre())
                .correo(pa.getUsuario().getEmail())
                .telefono(pa.getTelefono())
                .direccion(pa.getDireccion())
                .build();
    }
}
