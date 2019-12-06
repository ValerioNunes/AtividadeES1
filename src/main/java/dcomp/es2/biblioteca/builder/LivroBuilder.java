package dcomp.es2.biblioteca.builder;

import dcomp.es2.biblioteca.modelo.Livro;

public class LivroBuilder {

    private Livro livro;

    private LivroBuilder() {}

    public static LivroBuilder umLivro() {

        LivroBuilder builder = new LivroBuilder();

        builder.livro = new Livro();
        builder.livro.setEmprestado(false);
        builder.livro.setTitulo("Livro sem titulo");
        builder.livro.setReservado(false);
        return builder;
    }

    public LivroBuilder estaEmprestado() {
        this.livro.setEmprestado(true);
        return this;
    }

    public LivroBuilder estaReservedo() {
        this.livro.setEmprestado(true);
        return this;
    }

    public LivroBuilder comNome(String titulo) {
        this.livro.setTitulo(titulo);
        return this;
    }

    public Livro constroi() {
        return this.livro;
    }


}
