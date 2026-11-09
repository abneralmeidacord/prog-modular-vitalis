package br.com.vitalis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.vitalis.controller.ConsultaController;
import br.com.vitalis.controller.InternacaoController;
import br.com.vitalis.controller.PacienteController;
import br.com.vitalis.controller.ProfissionalController;

@RestControllerAdvice(assignableTypes = {ConsultaController.class, InternacaoController.class,
        PacienteController.class, ProfissionalController.class})
public class ApiExceptionHandler {
    @ExceptionHandler(DadosInvalidosException.class)
    public ProblemDetail tratarDadosInvalidos(DadosInvalidosException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ProblemDetail tratarConflito(ConflitoException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail tratarNaoEncontrado(RecursoNaoEncontradoException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
