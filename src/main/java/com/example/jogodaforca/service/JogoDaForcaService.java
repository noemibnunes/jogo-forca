package com.example.jogodaforca.service;

import com.example.jogodaforca.infra.GerarPalavraEDica;
import com.example.jogodaforca.model.JogoDaForca;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JogoDaForcaService {

    private GerarPalavraEDica gerarPalavraEDica;
    private JogoDaForca jogoDaForca = new JogoDaForca();
    private StringBuilder progresso;
    private int erros;  // Número de erros cometidos
    private int maxErros;

    public JogoDaForcaService(GerarPalavraEDica gerarPalavraEDica) {
        this.gerarPalavraEDica = gerarPalavraEDica;
        this.erros = 0;
        this.maxErros = 6;
    }

    public Map<String, String> inicializarJogo() {
        erros = 0;
        String response = gerarPalavraEDica.gerarPalavraEDica();

        String[] partes = response.split(" - ");
        String palavraSecreta = partes[0];
        jogoDaForca.setPalavraSecreta(palavraSecreta);
        jogoDaForca.setDica(partes[1]);

        // Inicializar progresso
        progresso = new StringBuilder();
        for (int i = 0; i < palavraSecreta.length(); i++) {
            if (Character.isLetter(palavraSecreta.charAt(i))) {
                progresso.append("_"); // Caracter para letras escondidas
            } else {
                progresso.append(palavraSecreta.charAt(i)); // Caracteres como espaços ou pontuações permanecem visíveis
            }
        }

        Map<String, String> resultado = new HashMap<>();
        resultado.put("dica", jogoDaForca.getDica());
        resultado.put("qtd_letras", String.valueOf(jogoDaForca.getPalavraSecreta().length()));
        resultado.put("progresso", progresso.toString());

        return resultado;
    }

    public Map<String, Object> tentar(char letra) {
        boolean acertou = false;
        String palavraSecreta = jogoDaForca.getPalavraSecreta();

        // Iterar pela palavra secreta para verificar ocorrências da letra
        for (int i = 0; i < palavraSecreta.length(); i++) {
            if (Character.toLowerCase(palavraSecreta.charAt(i)) == Character.toLowerCase(letra)) {
                this.progresso.setCharAt(i, palavraSecreta.charAt(i)); // Atualiza o progresso com a letra correta
                acertou = true;
            }
        }

        // Se a letra não foi encontrada, incrementar os erros
        if (!acertou) {
            erros++;
        }

        // Criar um mapa para retornar as informações necessárias ao frontend
        Map<String, Object> response = new HashMap<>();
        response.put("acerto", acertou);
        response.put("progresso", progresso.toString());
        response.put("erros", erros);

        // Verificar se o jogador completou a palavra
        if (progresso.toString().replace(" ", "").equalsIgnoreCase(palavraSecreta.replace(" ", ""))) {
            response.put("mensagem", "Parabéns! Você venceu! A palavra era: " + palavraSecreta);
            response.put("vitoria", true);
        } else if (erros >= maxErros) { // Supondo que há um limite de erros
            response.put("mensagem", "Você perdeu! A palavra era: " + palavraSecreta);
            response.put("vitoria", false);
        } else {
            response.put("mensagem", "Progresso atualizado. Continue tentando!");
            response.put("vitoria", null); // Ainda em progresso
        }

        return response;
    }

    public String adivinharPalavra(String tentativa) {
        String palavraSecreta = jogoDaForca.getPalavraSecreta();

        if (palavraSecreta.equalsIgnoreCase(tentativa)) {
            progresso = new StringBuilder(palavraSecreta);
            return "Parabéns! Você venceu! A palavra era: " + palavraSecreta;
        } else {
            erros = 6;
            return "Resposta incorreta! Você perdeu!\nA palavra era: " + palavraSecreta;
        }
    }
}
