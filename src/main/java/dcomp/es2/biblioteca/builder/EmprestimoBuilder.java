package dcomp.es2.biblioteca.builder;

import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.modelo.Pagamento;
import dcomp.es2.biblioteca.modelo.Usuario;

import java.time.LocalDateTime;
import java.util.List;

public class EmprestimoBuilder {
    private Emprestimo emprestimo;

    private EmprestimoBuilder() { }

    public static EmprestimoBuilder umEmprestivo() {

        EmprestimoBuilder builder = new EmprestimoBuilder();

        builder.emprestimo = new Emprestimo();
        builder.emprestimo.setPagamento(new Pagamento(0d));
        return builder;
    }

    public EmprestimoBuilder colocarDataEmprestimo(LocalDateTime dataEmprestimo) {
        this.emprestimo.setDataEmprestimo(dataEmprestimo);
        return this;
    }

    public EmprestimoBuilder colocarDataPrevista(LocalDateTime dataPrevista) {
        this.emprestimo.setDataPrevista(dataPrevista);
        return this;
    }

    public EmprestimoBuilder colocarPagamento(Pagamento pagamento) {

        this.emprestimo.setPagamento(pagamento);
        return this;
    }

    public EmprestimoBuilder selecionarUsuario(Usuario usuario) {
        this.emprestimo.setUsuario(usuario);
        return this;
    }

    public EmprestimoBuilder selecionarLivro(List<Livro> livros) {
        this.emprestimo.setLivros(livros);
        return this;
    }
    public Emprestimo constroi() {
        return this.emprestimo;
    }

}
