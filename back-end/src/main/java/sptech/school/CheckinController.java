package sptech.school;

import org.springframework.http.ResponseEntity;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkin")
public class CheckinController {

    private final CheckinDao checkinDao;
    public CheckinController(CheckinDao checkinDao) { this.checkinDao = checkinDao; }

    @GetMapping
    public ResponseEntity<List<Checkin>> getCheckins() {
        List<Checkin> checkins = checkinDao.listarTodos();
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
                    Map.of("mensagem", "O tipo de treino e obrigatorio."));
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

        if (checkinDao.existeCheckinNoDia(dataHora.toLocalDate())) {
            return ResponseEntity.status(409).body(
                    Map.of("mensagem", "Já existe um check-in nesse dia.")
            );
        }

        Checkin novoCheckin = new Checkin(
                null,
                dataHora,
                tipoTreino
        );

        try {
            Checkin salvo = checkinDao.inserir(novoCheckin);

            return ResponseEntity.status(201).body(salvo);
        } catch (DuplicateKeyException exception) {
            return ResponseEntity.status(409).body(
                    Map.of("mensagem", "Já existe um check-in nesse dia.")
            );
        }
    }
}
