package br.com.vitalis.Consultas;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.vitalis.profissionais.DadosInvalidosException;
import br.com.vitalis.profissionais.ProfissionalNaoEncontradoException;

@RestControllerAdvice(assignableTypes = ConsultaController.class)
public class ConsultaExceptionHandler {
    @ExceptionHandler(DadosInvalidosException.class)
    public ProblemDetail tratarDadosInvalidos(
            DadosInvalidosException exception) {

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