import CardCheckin from './CardCheckin'
import styles from './ListaCheckins.module.css'

function ListaCheckins({ checkins, carregando, erro, aoAtualizar }) {
  return (
    <section className={styles.painel}>
      <div className={styles.cabecalho}>
        <div><h2 id="titulo-historico">Meu histórico</h2></div>
        <button onClick={aoAtualizar} disabled={carregando}>Atualizar</button>
      </div>
      <p className={styles.descricao}>Suas conquistas estão aqui.</p>
      {carregando ? <p className={styles.estado} role="status">Carregando seus treinos...</p>
        : erro ? <p className={styles.erro} role="alert">{erro} Clique em Atualizar para tentar novamente.</p>
          : checkins.length === 0 ? (
            <div className={styles.estado}>
              <h3>Seu próximo passo começa aqui.</h3>
              <p>Registre seu primeiro treino para começar.</p>
            </div>
          ) : <ul className={styles.lista}>{checkins.map((checkin) => <CardCheckin key={checkin.id} checkin={checkin} />)}</ul>}
    </section>
  )
}
export default ListaCheckins
