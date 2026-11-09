package br.com.vitalis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.vitalis.controller.ProfissionalController;

@RestControllerAdvice(assignableTypes = ProfissionalController.class)
public class ProfissionalExceptionHandler {
    @ExceptionHandler(DadosProfissionaisInvalidosException.class)
    public ProblemDetail tratarDadosInvalidos(DadosProfissionaisInvalidosException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ProblemDetail tratarRegistroDuplicado(RegistroDuplicadoException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(ProfissionalNaoEncontradoException.class)
    public ProblemDetail tratarProfissionalNaoEncontrado(ProfissionalNaoEncontradoException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
