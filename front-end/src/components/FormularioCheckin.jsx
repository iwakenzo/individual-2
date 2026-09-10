import { useState } from 'react'
import { postCheckin } from '../services/checkinApi'
import styles from './FormularioCheckin.module.css'

function FormularioCheckin({ aoCadastrar }) {
  const [dataHora, setDataHora] = useState('')
  const [tipoTreino, setTipoTreino] = useState('')
  const [duracaoMinutos, setDuracaoMinutos] = useState('')
  const [intensidade, setIntensidade] = useState('')
  const [localTreino, setLocalTreino] = useState('')
  const [salvando, setSalvando] = useState(false)
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  async function cadastrarCheckin(evento) {
    evento.preventDefault()
    setErro('')
    setSucesso('')
    if (!tipoTreino.trim() || !localTreino.trim()) {
      setErro('Preencha o tipo e o local do treino.')
      return
    }
    if (dataHora && new Date(dataHora) > new Date()) {
      setErro('Não é permitido check-in no futuro.')
      return
    }
    setSalvando(true)
    try {
      const checkin = await postCheckin({
        dataHora: dataHora || null,
        tipoTreino: tipoTreino.trim(),
        duracaoMinutos: Number(duracaoMinutos),
        intensidade,
        localTreino: localTreino.trim(),
      })
      aoCadastrar(checkin)
      setDataHora('')
      setTipoTreino('')
      setDuracaoMinutos('')
      setIntensidade('')
      setLocalTreino('')
      setSucesso('Treino registrado!')
    } catch (erro) {
      setErro(erro.message)
    } finally {
      setSalvando(false)
    }
  }

  return (
    <section className={styles.painel}>
      <div className={styles.titulo}>
        <h2 id="titulo-cadastro">Registrar treino</h2>
      </div>
      <p className={styles.descricao}>Um check-in por dia. Uma vitória por dia.</p>
      <form onSubmit={cadastrarCheckin}>
        <fieldset disabled={salvando}>
          <label htmlFor="tipoTreino">O que você treinou?</label>
          <input id="tipoTreino" value={tipoTreino} onChange={(e) => setTipoTreino(e.target.value)} maxLength={50} placeholder="Peito e tríceps" required />
          <label htmlFor="dataHora">Data e hora</label>
          <input id="dataHora" type="datetime-local" value={dataHora} onChange={(e) => setDataHora(e.target.value)} />
          <small id="ajuda-data">Deixe vazio para registrar o horário atual ou registre um treino passado.</small>
          <div className={styles.linha}>
            <div>
              <label htmlFor="duracao">Duração em minutos</label>
              <input id="duracao" type="number" min="1" max="1440" step="1" value={duracaoMinutos} onChange={(e) => setDuracaoMinutos(e.target.value)} placeholder="60" required />
            </div>
            <div>
              <label htmlFor="intensidade">Intensidade</label>
              <select id="intensidade" value={intensidade} onChange={(e) => setIntensidade(e.target.value)} required>
                <option value="">Selecione</option>
                <option>Leve</option><option>Moderada</option><option>Intensa</option>
              </select>
            </div>
          </div>
          <label htmlFor="localTreino">Onde você treinou?</label>
          <input id="localTreino" value={localTreino} onChange={(e) => setLocalTreino(e.target.value)} maxLength={50} placeholder="Academia, casa ou parque" required />
          <button type="submit">Fazer check-in</button>
        </fieldset>
        {erro && <p className={styles.erro} role="alert">{erro}</p>}
        {sucesso && <p className={styles.sucesso} role="status">{sucesso}</p>}
      </form>
    </section>
  )
}
export default FormularioCheckin
