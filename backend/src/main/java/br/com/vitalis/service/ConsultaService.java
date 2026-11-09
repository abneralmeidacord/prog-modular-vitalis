package br.com.vitalis.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.com.vitalis.entity.ClinicoGeral;
import br.com.vitalis.entity.Consulta;
import br.com.vitalis.entity.ConsultaEspecializada;
import br.com.vitalis.entity.ConsultaRotina;
import br.com.vitalis.entity.Especialista;
import br.com.vitalis.entity.Profissional;
import br.com.vitalis.exceptions.ConsultaConflitoException;
import br.com.vitalis.exceptions.ConsultaNaoEncontradaException;
import br.com.vitalis.exceptions.DadosInvalidosException;
import br.com.vitalis.model.ConcluirConsultaRequest;
import br.com.vitalis.model.ConsultaRegistrada;
import br.com.vitalis.model.CriarConsultaRequest;
import br.com.vitalis.model.ReagendarConsultaRequest;
import br.com.vitalis.model.StatusAtendimento;
import br.com.vitalis.model.Validacao;

@Service
public class ConsultaService {
    private final ProfissionalService profissionalService;
    private final PacienteService pacienteService;
    private final Map<Long, Consulta> consultas = new LinkedHashMap<>();
    private long proximoId = 1;

    public ConsultaService(ProfissionalService profissionalService, PacienteService pacienteService) {
        this.profissionalService = profissionalService;
        this.pacienteService = pacienteService;
    }

    public synchronized ConsultaRegistrada cadastrar(CriarConsultaRequest request) {
        if (request == null || request.tipo() == null) {
            throw new DadosInvalidosException("Tipo de consulta é obrigatório.");
        }

        Profissional profissional = profissionalService.buscar(request.profissionalId());
        Validacao.pacienteObrigatorio(request.pacienteId());
        pacienteService.buscar(request.pacienteId());

        Consulta consulta = switch (request.tipo()) {
            case ROTINA -> criarConsultaRotina(request, profissional);
            case ESPECIALIZADA -> criarConsultaEspecializada(request, profissional);
        };

        validarHorario(profissional.getId(), consulta.getDataHoraInicio(), null);
        long id = proximoId++;
        consultas.put(id, consulta);

        return registrar(id, consulta);
    }

    public synchronized List<ConsultaRegistrada> listar(Long pacienteId) {
        return consultas.entrySet()
                .stream()
                .filter(entrada -> pacienteId == null || entrada.getValue().getPacienteId() == pacienteId)
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
        if (request.data() != null && request.horario() != null) {
            validarHorario(consulta.getProfissional().getId(), LocalDateTime.of(request.data(), request.horario()), consulta);
        }
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

    private void validarHorario(long profissionalId, LocalDateTime horario, Consulta reagendada) {
        boolean ocupado = consultas.values().stream()
                .filter(consulta -> consulta != reagendada && consulta.getProfissional().getId() == profissionalId)
                .filter(consulta -> consulta.getStatus() != StatusAtendimento.CANCELADO
                        && consulta.getStatus() != StatusAtendimento.NAO_COMPARECEU)
                .anyMatch(consulta -> consulta.getDataHoraInicio().equals(horario));
        if (ocupado) {
            throw new ConsultaConflitoException("O profissional já possui uma consulta neste horário.");
        }
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
}
