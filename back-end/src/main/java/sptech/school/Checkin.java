package sptech.school;

import java.time.LocalDateTime;

public class Checkin {
    private Integer id;
    private LocalDateTime dataHora;
    private String tipoTreino;

    public Checkin(Integer id, LocalDateTime dataHora, String tipoTreino) {
        this.id = id;
        this.dataHora = dataHora;
        this.tipoTreino = tipoTreino;
    }

    public Checkin() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getTipoTreino() {
        return tipoTreino;
    }

    public void setTipoTreino(String tipoTreino) {
        this.tipoTreino = tipoTreino;
    }
}
