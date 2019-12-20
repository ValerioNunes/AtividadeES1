package dcomp.es2.biblioteca.servico;

import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.repository.EmprestimoRepository;

import java.time.LocalDateTime;
import java.util.List;

public class EstatisticasDeEmprestimo {


    EmprestimoRepository emprestimoRepository;

    public List<Livro> getLivrosEmprestadoNoPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return emprestimoRepository.getLivroNoPeriodo(inicio, fim);
    }

    public EmprestimoRepository getEmprestimoRepository() {
        return emprestimoRepository;
    }

    public void setEmprestimoRepository(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }
}
