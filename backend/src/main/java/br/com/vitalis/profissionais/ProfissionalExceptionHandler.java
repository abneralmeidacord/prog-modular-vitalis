package br.com.vitalis.profissionais;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ProfissionalController.class)
public class ProfissionalExceptionHandler {
    @ExceptionHandler(DadosInvalidosException.class)
    public ProblemDetail tratarDadosInvalidos(DadosInvalidosException exception) {
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
