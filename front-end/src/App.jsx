import { useEffect, useState } from 'react'
import FormularioCheckin from './components/FormularioCheckin'
import ListaCheckins from './components/ListaCheckins'
import { getCheckins } from './services/checkinApi'
import styles from './App.module.css'

function App() {
  const [checkins, setCheckins] = useState([])
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')

  async function carregarCheckins() {
    setCarregando(true)
    setErro('')
    try {
      setCheckins(await getCheckins())
    } catch (erro) {
      setErro(erro.message)
    } finally {
      setCarregando(false)
    }
  }

  useEffect(() => {
    let ativo = true
    getCheckins()
      .then((dados) => { if (ativo) setCheckins(dados) })
      .catch((erro) => { if (ativo) setErro(erro.message) })
      .finally(() => { if (ativo) setCarregando(false) })
    return () => { ativo = false }
  }, [])

  function adicionarCheckin(checkin) {
    setCheckins((anteriores) => {
      const lista = [...anteriores, checkin]
      lista.sort((a, b) => b.dataHora.localeCompare(a.dataHora))
      return lista
    })
  }

  let minutos = 0
  for (const checkin of checkins) {
    minutos += checkin.duracaoMinutos
  }

  return (
    <div className={styles.pagina}>
      <header className={styles.cabecalho}>
        <a href="#inicio" className={styles.marca}>frequence<span>!</span></a>
      </header>
      <main id="inicio">
        <section className={styles.abertura}>
          <div>
            <p className={styles.sobretitulo}>UM DIA DE CADA VEZ</p>
            <h1>Seu treino.<br /><span>Seu ritmo.</span></h1>
          </div>
          <div className={styles.resumo}>
            <div><strong>{carregando || erro ? '—' : String(checkins.length).padStart(2, '0')}</strong><span>Treinos</span></div>
            <div><strong>{carregando || erro ? '—' : String(minutos).padStart(3, '0')}</strong><span>Minutagem</span></div>
          </div>
        </section>
        <div className={styles.conteudo}>
          <FormularioCheckin aoCadastrar={adicionarCheckin} />
          <ListaCheckins checkins={checkins} carregando={carregando} erro={erro} aoAtualizar={carregarCheckins} />
        </div>
      </main>
      <footer className={styles.rodape}>"O importante é não parar." - Lau, Lincoln "fnx"</footer>
    </div>
  )
}
export default App
