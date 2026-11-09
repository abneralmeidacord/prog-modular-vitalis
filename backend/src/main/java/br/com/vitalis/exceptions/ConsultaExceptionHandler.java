package br.com.vitalis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.vitalis.controller.ConsultaController;

@RestControllerAdvice(assignableTypes = ConsultaController.class)
public class ConsultaExceptionHandler {
    @ExceptionHandler(DadosProfissionaisInvalidosException.class)
    public ProblemDetail tratarDadosInvalidos(
            DadosProfissionaisInvalidosException exception) {

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
    }

    @ExceptionHandler(ConsultaConflitoException.class)
    public ProblemDetail tratarConflito(
            ConsultaConflitoException exception) {

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage());
    }

    @ExceptionHandler({
            ConsultaNaoEncontradaException.class,
            ProfissionalNaoEncontradoException.class
    })
    public ProblemDetail tratarNaoEncontrado(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage());
    }
}
