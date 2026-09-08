package sptech.school;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public class CheckinDao {

    private final JdbcTemplate jdbcTemplate;
    public CheckinDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    public List<Checkin> listarTodos() {
        String sql = "SELECT id, data_hora, tipo_treino FROM checkin ORDER BY data_hora DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Checkin.class));
    }

    public boolean existeCheckinNoDia(LocalDate dia) {
        String sql = "SELECT COUNT(*) FROM checkin WHERE data_hora >= ? AND data_hora < ?";
        Timestamp inicio = Timestamp.valueOf(dia.atStartOfDay());
        Timestamp fim = Timestamp.valueOf(dia.plusDays(1).atStartOfDay());
        Integer quantidade = jdbcTemplate.queryForObject(sql, Integer.class, inicio, fim);
        return quantidade != null && quantidade > 0;
    }

    public Checkin inserir(Checkin checkin) {
        String sql = "INSERT INTO checkin (data_hora, tipo_treino) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setTimestamp(1, Timestamp.valueOf(checkin.getDataHora()));
            ps.setString(2, checkin.getTipoTreino());
            return ps;
        }, keyHolder);
        Number chave = keyHolder.getKey();
        if (chave == null) {
            throw new IllegalStateException("O banco não retornou o ID do check-in.");
        }
        checkin.setId(chave.intValue());
        return checkin;
    }
}
