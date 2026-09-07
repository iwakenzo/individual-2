package sptech.school;

import java.time.LocalDateTime;

public class Checkin {
    private Integer id;
    private LocalDateTime dataHora;
    private String tipoTreino;

    public Checkin() {}

    public Checkin(Integer id, LocalDateTime dataHora, String tipoTreino) {
        this.id = id;
        this.dataHora = dataHora;
        this.tipoTreino = tipoTreino;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getTipoTreino() {
        return tipoTreino;
    }
}
