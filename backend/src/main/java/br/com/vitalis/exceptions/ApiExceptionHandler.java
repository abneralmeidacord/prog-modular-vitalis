package br.com.vitalis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.vitalis.controller.InternacaoController;
import br.com.vitalis.controller.PacienteController;

@RestControllerAdvice(assignableTypes = {InternacaoController.class, PacienteController.class})
public class ApiExceptionHandler {
    @ExceptionHandler(DadosInvalidosException.class)
    public ProblemDetail tratarDadosInvalidos(DadosInvalidosException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ProblemDetail tratarConflito(ConflitoException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler({RecursoNaoEncontradoException.class, ProfissionalNaoEncontradoException.class,
            QuartoNaoEncontradoException.class})
    public ProblemDetail tratarNaoEncontrado(RuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
