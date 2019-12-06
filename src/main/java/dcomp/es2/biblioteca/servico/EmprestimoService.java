package dcomp.es2.biblioteca.servico;

import dcomp.es2.biblioteca.builder.EmprestimoBuilder;
import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.modelo.Pagamento;
import dcomp.es2.biblioteca.modelo.Usuario;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class EmprestimoService {
    private static final int numeroDiasEmprestimo = 7;
    private static final double valorAluguelFixo = 5.0d;
    private  static  final double taxaDiaria = 0.4d;
    private ArrayList<Emprestimo> emprestimosVirgentes;

    public Emprestimo emprestarLivro(Usuario usuario, Livro  livro) {

        if (livro.isEmprestado())
            throw new IllegalArgumentException("Livro emprestado");
        if (livro.isReservado())
            throw new IllegalArgumentException("Livro reservado");

        List<Emprestimo> emprestimosDoUsuario = getEmprestimosVirgentesDoUsuario(usuario);

       if (emprestimosDoUsuario.size() >= 2)
           throw new IllegalArgumentException("Usuario com numero maximo de emprestimos");

        livro.setReservado(true);
        livro.setEmprestado(true);
        List<Livro> livros =  new ArrayList<>();
        livros.add(livro);
        Emprestimo emprestimo = EmprestimoBuilder.umEmprestivo()
                .selecionarUsuario(usuario)
                .selecionarLivro(livros)
                .colocarDataEmprestimo(LocalDateTime.now())
                .colocarDataPrevista(LocalDateTime.now().plusDays(numeroDiasEmprestimo))
                .constroi();

        //Salvando a Emprestivo...
        //TODO adicionar método para salvar
        //daoEmprestivo.salva(Emprestimo);
        addEmprestimoVirgente(emprestimo);
        return emprestimo;
    }

    private void addEmprestimoVirgente(Emprestimo emprestimo){
        if (emprestimosVirgentes == null)
            emprestimosVirgentes = new ArrayList<Emprestimo>();
        emprestimosVirgentes.add(emprestimo);
    }

    private void removerEmprestimoVirgente(Emprestimo emprestimo){
        if (emprestimosVirgentes != null) {
            if(emprestimosVirgentes.contains(emprestimo)) {
                emprestimosVirgentes.remove(emprestimo);
                emprestimo.getLivros().forEach( livro -> livro.setEmprestado(false));
            }
        }
    }

    public List<Emprestimo> getEmprestimosVirgentesDoUsuario(Usuario usuario){
        List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
        if(emprestimosVirgentes == null)
            return  emprestimos;
        emprestimos = emprestimosVirgentes.stream().filter(p
                -> p.getUsuario().getNome().equals(usuario.getNome())).collect(Collectors.toList());
        return  emprestimos;
    }

    public static int getNumeroDiasMaximoSemAtraso(){
        return numeroDiasEmprestimo;
    }

    public static double getValorAluguelFixo() {
        return valorAluguelFixo;
    }

    public static double getTaxaDiaria() {
        return taxaDiaria;
    }

    public static double getValorAluguelFixoPoncentagemDe(double pct) {
        return valorAluguelFixo*(pct/100.0d);
    }

    private double getValorParaSerPago(Emprestimo emprestimo){
        double valorPago = getValorAluguelFixo();

        Duration numeroDiasEmprestimo =  Duration.between( emprestimo.getDataPrevista(), emprestimo.getDataDevolucao());
        System.out.println( Double.valueOf(numeroDiasEmprestimo.toDays()));

        if( !numeroDiasEmprestimo.isNegative()) {
            double valorAcrescimo = Double.valueOf(numeroDiasEmprestimo.toDays()) * taxaDiaria;

            if(valorAcrescimo > getValorAluguelFixoPoncentagemDe(60)){
                valorPago += getValorAluguelFixoPoncentagemDe(60);
            }else{
                valorPago += valorAcrescimo;
            }
        }

        return valorPago;
    }

    public Emprestimo finalizarEmprestimo(Emprestimo emprestimo){
        if( emprestimosVirgentes !=  null  & emprestimosVirgentes.contains(emprestimo)){
            if(emprestimo.getDataDevolucao() == null)
                emprestimo.setDataDevolucao(LocalDateTime.now());
            double valor = getValorParaSerPago(emprestimo);
            Pagamento  pagamento = new Pagamento(valor);

            emprestimo.setPagamento(pagamento);
            removerEmprestimoVirgente(emprestimo);
        }
        return emprestimo;
    }
}
