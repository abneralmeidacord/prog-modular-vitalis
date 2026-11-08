package br.com.vitalis.quartos;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "br.com.vitalis.quartos")
public class QuartoExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> dadosInvalidos(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest()
            .body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflito(
            IllegalStateException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(QuartoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> naoEncontrado(
            QuartoNaoEncontradoException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("erro", ex.getMessage()));
    }
}
