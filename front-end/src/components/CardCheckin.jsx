import styles from './CardCheckin.module.css'

function CardCheckin({ checkin }) {
  const data = new Date(checkin.dataHora)
  return (
    <li className={styles.card}>
      <div className={styles.data}>
        <strong>{data.toLocaleDateString('pt-BR', { day: '2-digit' })}</strong>
        <span>{data.toLocaleDateString('pt-BR', { month: 'short' }).replace('.', '')}</span>
      </div>
      <div className={styles.detalhes}>
        <div className={styles.topo}><h3>{checkin.tipoTreino}</h3><span className={styles.bullet}>{checkin.intensidade}</span></div>
        <p>{checkin.localTreino} <span>·</span> {checkin.duracaoMinutos} min</p>
        <time dateTime={checkin.dataHora}>{data.toLocaleDateString('pt-BR')} às {data.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })}</time>
      </div>
    </li>
  )
}
export default CardCheckin
