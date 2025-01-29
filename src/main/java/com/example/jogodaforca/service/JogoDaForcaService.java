package com.example.jogodaforca.service;

import com.example.jogodaforca.infra.GerarPalavraEDica;
import com.example.jogodaforca.model.JogoDaForca;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
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
                progresso.append("_");
            } else {
                progresso.append(palavraSecreta.charAt(i));
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
        String letraNormalizada = removerAcentuacao(String.valueOf(letra));

        for (int i = 0; i < palavraSecreta.length(); i++) {
            if (removerAcentuacao(String.valueOf(palavraSecreta.charAt(i)))
                    .equalsIgnoreCase(letraNormalizada)) {
                this.progresso.setCharAt(i, palavraSecreta.charAt(i));
                acertou = true;
            }
        }

        // Se a letra não foi encontrada, incrementar os erros
        if (!acertou) {
            erros++;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("acerto", acertou);
        response.put("progresso", progresso.toString());
        response.put("erros", erros);

        if (progresso.toString().replace(" ", "").equalsIgnoreCase(palavraSecreta.replace(" ", ""))) {
            response.put("mensagem", "Parabéns! Você venceu!");
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

    public Map<String, Object> adivinharPalavra(String tentativa) {
        String palavraSecreta = jogoDaForca.getPalavraSecreta();
        Map<String, Object> response = new HashMap<>();

        String palavraSecretaNormalizada = removerAcentuacao(palavraSecreta);
        String tentativaNormalizada = removerAcentuacao(tentativa);

        if (palavraSecretaNormalizada.equalsIgnoreCase(tentativaNormalizada)) {
            if (progresso == null) {
                progresso = new StringBuilder(palavraSecreta);
            } else {
                progresso.replace(0, progresso.length(), palavraSecreta);
            }

            response.put("progresso", progresso.toString());
            response.put("mensagem", "Parabéns! Você venceu!");
            response.put("vitoria", true);
        } else {
            // Se errar, marca o jogo como perdido
            erros = 6;
            response.put("mensagem", "Resposta incorreta! Você perdeu! A palavra era: " + palavraSecreta);
            response.put("vitoria", false);
        }

        return response;
    }

    public String removerAcentuacao(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

}
