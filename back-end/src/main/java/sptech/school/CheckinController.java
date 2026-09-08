package sptech.school;

import org.springframework.http.ResponseEntity;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkin")
public class CheckinController {

    private final JdbcTemplate jdbcTemplate;

    public CheckinController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private boolean existeCheckinNoDia(LocalDate dia) {
        String sql = "SELECT COUNT(*) FROM checkin WHERE data_hora >= ? AND data_hora < ?";
        Timestamp inicio = Timestamp.valueOf(dia.atStartOfDay());
        Timestamp fim = Timestamp.valueOf(dia.plusDays(1).atStartOfDay());

        Integer quantidade = jdbcTemplate.queryForObject(sql, Integer.class, inicio, fim);
        return quantidade != null && quantidade > 0;
    }

    @GetMapping
    public ResponseEntity<List<Checkin>> getCheckins() {
        String sql = "SELECT id, data_hora, tipo_treino, duracao_minutos, intensidade, local_treino FROM checkin ORDER BY data_hora DESC";
        List<Checkin> checkins = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Checkin.class));
        return ResponseEntity.status(200).body(checkins);
    }

    @PostMapping
    public ResponseEntity<?> postCheckin(@RequestBody Checkin checkin) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataHora = checkin.getDataHora();

        if (dataHora == null) {
            dataHora = agora;
        } else if (dataHora.isAfter(agora)) {
            return ResponseEntity.status(400).body(
                    Map.of("mensagem", "Não é permitido check-in no futuro.")
            );
        }

        if (checkin.getTipoTreino() == null || checkin.getTipoTreino().isBlank()) {
            return ResponseEntity.status(400).body(
                    Map.of("mensagem", "O tipo de treino e obrigatorio.")
            );
        }

        String tipoTreino = checkin.getTipoTreino().trim();

        if (tipoTreino.length() > 50) {
            return ResponseEntity.status(400).body(
                    Map.of(
                            "mensagem",
                            "O tipo de treino deve ter até 50 caracteres."
                    )
            );
        }

        if (checkin.getDuracaoMinutos() == null || checkin.getDuracaoMinutos() < 1 || checkin.getDuracaoMinutos() > 1440) {
            return ResponseEntity.status(400).body(Map.of("mensagem", "A duração deve ser entre 1 e 1440 minutos."));
        }

        if (checkin.getIntensidade() == null || !List.of("Leve", "Moderada", "Intensa").contains(checkin.getIntensidade())) {
            return ResponseEntity.status(400).body(Map.of("mensagem", "Selecione uma intensidade: Leve, Moderada ou Intensa."));
        }

        if (checkin.getLocalTreino() == null || checkin.getLocalTreino().isBlank() || checkin.getLocalTreino().trim().length() > 50) {
            return ResponseEntity.status(400).body(Map.of("mensagem", "Informe um local de treino com até 50 caracteres."));
        }

        if (existeCheckinNoDia(dataHora.toLocalDate())) {
            return ResponseEntity.status(409).body(
                    Map.of("mensagem", "Já existe um check-in nesse dia.")
            );
        }

        Checkin novoCheckin = new Checkin(
                null,
                dataHora,
                tipoTreino
        );

        novoCheckin.setDuracaoMinutos(checkin.getDuracaoMinutos());
        novoCheckin.setIntensidade(checkin.getIntensidade());
        novoCheckin.setLocalTreino(checkin.getLocalTreino().trim());

        try {
            String sql = "INSERT INTO checkin (data_hora, tipo_treino, duracao_minutos, intensidade, local_treino) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setTimestamp(1, Timestamp.valueOf(novoCheckin.getDataHora()));
                ps.setString(2, novoCheckin.getTipoTreino());
                ps.setInt(3, novoCheckin.getDuracaoMinutos());
                ps.setString(4, novoCheckin.getIntensidade());
                ps.setString(5, novoCheckin.getLocalTreino());
                return ps;
            }, keyHolder);

            Number chave = keyHolder.getKey();

            if (chave == null) {
                throw new IllegalStateException("O banco não retornou o ID do check-in.");
            }

            novoCheckin.setId(chave.intValue());
            return ResponseEntity.status(201).body(novoCheckin);

        } catch (DuplicateKeyException exception) {
            return ResponseEntity.status(409).body(
                    Map.of("mensagem", "Já existe um check-in nesse dia.")
            );
        }
    }
}
