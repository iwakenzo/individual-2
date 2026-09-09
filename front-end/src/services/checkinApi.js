const url = '/api/checkin'

async function lerResposta(resposta) {
  if (!resposta.ok) {
    const erro = await resposta.json().catch(() => ({}))
    throw new Error(erro.mensagem || 'Não foi possível concluir a requisição. Confira os dados e tente novamente.')
  }
  return resposta.json()
}

export async function getCheckins() {
  let resposta
  try {
    resposta = await fetch(url)
  } catch {
    throw new Error('Não foi possível conectar à API. Confira se o back-end está rodando.')
  }
  return lerResposta(resposta)
}

export async function postCheckin(checkin) {
  let resposta
  try {
    resposta = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(checkin),
    })
  } catch {
    throw new Error('Não foi possível conectar à API. Confira se o back-end está rodando.')
  }
  return lerResposta(resposta)
}
