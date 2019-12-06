package dcomp.es2.biblioteca.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Livro {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String autor;
    private String titulo;
    private boolean isEmprestado;
    private boolean isReservado;


    public Livro() {}


    public Livro(String autor, String titulo, boolean isEmprestado, boolean isReservado) {
        this.autor = autor;
        this.titulo = titulo;
        this.isEmprestado = isEmprestado;
        this.isReservado = isReservado;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isEmprestado() {
        return isEmprestado;
    }

    public void setEmprestado(boolean emprestado) {
        isEmprestado = emprestado;
    }

    public boolean isReservado() {
        return isReservado;
    }

    public void setReservado(boolean reservado) {
        isReservado = reservado;
    }
}
