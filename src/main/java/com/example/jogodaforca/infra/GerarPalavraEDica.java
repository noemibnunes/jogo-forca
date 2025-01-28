package com.example.jogodaforca.infra;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class GerarPalavraEDica {

    private final OpenAiChatModel openAiChatModel;

    public GerarPalavraEDica(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    public String gerarPalavraEDica() {
        String prompt = "Gere uma palavra e uma dica para um jogo da forca. Formato: Palavra - Dica";

        return openAiChatModel.call(prompt);
    }
}
