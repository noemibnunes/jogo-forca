package com.example.jogodaforca.controller;

import com.example.jogodaforca.service.JogoDaForcaService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/jogo-da-forca")
public class JogoDaForcaController {

    private final JogoDaForcaService jogoDaForcaService;

    public JogoDaForcaController(JogoDaForcaService jogoDaForcaService) {
        this.jogoDaForcaService = jogoDaForcaService;
    }

    @RequestMapping("/iniciar")
    public Map<String, String> iniciarJogo() {
        return jogoDaForcaService.inicializarJogo();
    }

    @RequestMapping("/tentar")
    public Map<String, Object> tentar(@RequestBody Map<String, String> request) {
        char letra = request.get("letra").charAt(0);
        return jogoDaForcaService.tentar(letra);
    }

    @RequestMapping("/adivinhar")
    public Map<String, Object>   adivinharPalavra(@RequestParam("palavra") String palavra) {
        return jogoDaForcaService.adivinharPalavra(palavra);
    }
}
