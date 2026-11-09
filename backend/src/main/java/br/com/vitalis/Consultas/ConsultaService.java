package br.com.vitalis.Consultas;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.vitalis.profissionais.ClinicoGeral;
import br.com.vitalis.profissionais.DadosInvalidosException;
import br.com.vitalis.profissionais.Especialista;
import br.com.vitalis.profissionais.Profissional;
import br.com.vitalis.profissionais.ProfissionalService;

@Service
public class ConsultaService {
    private final ProfissionalService profissionalService;
    private final Map<Long, Consulta> consultas = new LinkedHashMap<>();
    private long proximoId = 1;

    public ConsultaService(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    public synchronized ConsultaRegistrada cadastrar(CriarConsultaRequest request) {
        if (request == null || request.tipo() == null) {
            throw new DadosInvalidosException("Tipo de consulta é obrigatório.");
        }

        Profissional profissional = profissionalService.buscar(request.profissionalId());

        Consulta consulta = switch (request.tipo()) {
            case ROTINA -> criarConsultaRotina(request, profissional);
            case ESPECIALIZADA -> criarConsultaEspecializada(request, profissional);
        };

        long id = proximoId++;
        consultas.put(id, consulta);

        return registrar(id, consulta);
    }

    public synchronized List<ConsultaRegistrada> listar() {
        return consultas.entrySet()
                .stream()
                .map(entrada -> registrar(entrada.getKey(), entrada.getValue()))
                .toList();
    }

    public synchronized ConsultaRegistrada buscar(long id) {
        return registrar(id, exigirConsulta(id));
    }

    public synchronized ConsultaRegistrada confirmar(long id) {
        Consulta consulta = exigirConsulta(id);
        consulta.confirmar();
        return registrar(id, consulta);
    }

    public synchronized ConsultaRegistrada reagendar(
            long id,
            ReagendarConsultaRequest request) {

        if (request == null) {
            throw new DadosInvalidosException(
                    "Nova data e novo horário são obrigatórios.");
        }

        Consulta consulta = exigirConsulta(id);
        consulta.reagendar(request.data(), request.horario());
        return registrar(id, consulta);
    }

    public synchronized ConsultaRegistrada realizar(long id) {
        Consulta consulta = exigirConsulta(id);
        consulta.realizar();
        return registrar(id, consulta);
    }

    public synchronized ConsultaRegistrada concluir(
            long id,
            ConcluirConsultaRequest request) {

        if (request == null) {
            throw new DadosInvalidosException(
                    "As observações da consulta são obrigatórias.");
        }

        Consulta consulta = exigirConsulta(id);
        consulta.concluir(request.observacoes());
        return registrar(id, consulta);
    }

    public synchronized ConsultaRegistrada cancelar(long id) {
        Consulta consulta = exigirConsulta(id);
        consulta.cancelar();
        return registrar(id, consulta);
    }

    public synchronized ConsultaRegistrada registrarNaoComparecimento(long id) {
        Consulta consulta = exigirConsulta(id);
        consulta.registrarNaoComparecimento();
        return registrar(id, consulta);
    }

    private Consulta criarConsultaRotina(
            CriarConsultaRequest request,
            Profissional profissional) {

        if (!(profissional instanceof ClinicoGeral clinico)) {
            throw new ConsultaConflitoException(
                    "Consulta de rotina exige um clínico geral.");
        }

        return new ConsultaRotina(
                request.pacienteId(),
                clinico,
                request.data(),
                request.horario(),
                request.motivo());
    }

    private Consulta criarConsultaEspecializada(
            CriarConsultaRequest request,
            Profissional profissional) {

        if (!(profissional instanceof Especialista especialista)) {
            throw new ConsultaConflitoException(
                    "Consulta especializada exige um especialista.");
        }

        return new ConsultaEspecializada(
                request.pacienteId(),
                especialista,
                request.data(),
                request.horario(),
                request.motivo(),
                request.especialidade());
    }

    private Consulta exigirConsulta(long id) {
        Consulta consulta = consultas.get(id);

        if (consulta == null) {
            throw new ConsultaNaoEncontradaException(id);
        }

        return consulta;
    }

    private ConsultaRegistrada registrar(long id, Consulta consulta) {
        return new ConsultaRegistrada(id, consulta);
    }

    public record ConsultaRegistrada(long id, Consulta consulta) {
    }
}
