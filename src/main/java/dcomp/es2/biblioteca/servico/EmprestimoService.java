package dcomp.es2.biblioteca.servico;

import dcomp.es2.biblioteca.builder.EmprestimoBuilder;
import dcomp.es2.biblioteca.modelo.Emprestimo;
import dcomp.es2.biblioteca.modelo.Livro;
import dcomp.es2.biblioteca.modelo.Pagamento;
import dcomp.es2.biblioteca.modelo.Usuario;
import dcomp.es2.biblioteca.repository.EmprestimoRepository;
import dcomp.es2.biblioteca.repository.LivroRepository;
import dcomp.es2.biblioteca.repository.PagamentoRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmprestimoService {

    private static final int numeroDiasEmprestimo = 7;
    private static final double valorAluguelFixo = 5.0d;
    private static final double taxaDiaria = 0.4d;
    private ArrayList<Emprestimo> emprestimosVirgentes;
    private EnviadorDeEmail emailService;
    static EmprestimoRepository emprestimoRepository = new EmprestimoRepository();

    public Emprestimo emprestarLivro(Usuario usuario, List<Livro> livros) {

        if (livros == null) {

            throw new IllegalArgumentException("Livros Invalidos");
        }
        livros.forEach(livro -> {

            if (livro.isEmprestado())
            throw new IllegalArgumentException("Livro emprestado");
        if (livro.isReservado())
            throw new IllegalArgumentException("Livro reservado");

        });
        List<Emprestimo> emprestimosDoUsuario = getEmprestimosVirgentesDoUsuario(usuario);

       if (emprestimosDoUsuario.size() >= 2)
           throw new IllegalArgumentException("Usuario com numero maximo de emprestimos");

//        livro.setReservado(true);
//        livro.setEmprestado(true);
//        List<Livro> livros =  new ArrayList<>();
//        livros.add(livro);

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
        if (emprestimosVirgentes == null) {
            emprestimosVirgentes = new ArrayList<Emprestimo>();
        }
        emprestimo.getLivros().forEach(livro -> {
            livro.setEmprestado(true);
            livro.setReservado(true);
        });
        emprestimosVirgentes.add(emprestimo);
    }

    private Emprestimo removerEmprestimoVirgente(Emprestimo emprestimo) {

        emprestimo.getLivros().forEach(livro -> {
            livro.setEmprestado(false);
            livro.setReservado(false);
            LivroRepository.getInstance().atualizar(livro);
        });

        return emprestimo;
    }

    public List<Emprestimo> getEmprestimosVirgentesDoUsuario(Usuario usuario) {
        List<Emprestimo> emprestimos = new ArrayList<Emprestimo>();
        if (emprestimosVirgentes == null)
            return emprestimos;
        emprestimos = emprestimosVirgentes.stream().filter(p
                -> p.getUsuario().getNome().equals(usuario.getNome())).collect(Collectors.toList());
        return emprestimos;
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

    public double getValorParaSerPago(Emprestimo emprestimo) {
        double valorPago = getValorAluguelFixo();

        Duration numeroDiasEmprestimo = Duration.between(emprestimo.getDataPrevista(), emprestimo.getDataDevolucao());

        if (!numeroDiasEmprestimo.isNegative()) {
            double valorAcrescimo = Double.valueOf(numeroDiasEmprestimo.toDays()) * taxaDiaria;

            if (valorAcrescimo > getValorAluguelFixoPoncentagemDe(60)) {
                valorPago += getValorAluguelFixoPoncentagemDe(60);
            } else {
                valorPago += valorAcrescimo;
            }
        }
        int quantidadeLivro = emprestimo.getLivros().size();

        return valorPago * quantidadeLivro;
    }

    public Emprestimo finalizarEmprestimo(Emprestimo emprestimo) {
        //  if((emprestimosVirgentes !=  null  & emprestimosVirgentes.contains(emprestimo)) || emprestimo.getId() != null){
        if (emprestimo.getDataDevolucao() == null)
            emprestimo.setDataDevolucao(LocalDateTime.now());
        double valor = getValorParaSerPago(emprestimo);
        Pagamento pagamento = new Pagamento(valor);
        PagamentoRepository.getInstance().salvar(pagamento);
        emprestimo.setPagamento(pagamento);
        emprestimo = removerEmprestimoVirgente(emprestimo);
        // }
        emprestimo = emprestimoRepository.atualizar(emprestimo);

        return emprestimo;
    }


    public List<Usuario> enviarEmailParaUsuarioComAtraso() {
        List<Usuario> usuarioComErroEmail = new ArrayList<>();
        emprestimoRepository.getUsuarioEmAtraso().forEach(usuario -> {
            if (enviarEmailDeAtrasoEmprestimo(usuario) != null) {
                usuarioComErroEmail.add(usuario);
            }
        });
        if (usuarioComErroEmail.size() > 0)
            throw new IllegalArgumentException("Usuário sem email cadastrado");

        return usuarioComErroEmail;
    }

    public Usuario enviarEmailDeAtrasoEmprestimo(Usuario usuario) {
        return emailService.enviarEmailPara(usuario, "Atraso na devolução de Livros blabla");
    }

    public EnviadorDeEmail getEmailService() {
        return emailService;
    }

    public void setEmailService(EnviadorDeEmail emailService) {
        this.emailService = emailService;
    }
}
