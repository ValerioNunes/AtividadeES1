package dcomp.es2.biblioteca.modelo;

import javax.persistence.*;
import java.util.Objects;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", autor='" + autor + '\'' +
                ", titulo='" + titulo + '\'' +
                ", isEmprestado=" + isEmprestado +
                ", isReservado=" + isReservado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro)) return false;
        Livro livro = (Livro) o;
        return isEmprestado() == livro.isEmprestado() &&
                isReservado() == livro.isReservado() &&
                Objects.equals(getId(), livro.getId()) &&
                Objects.equals(getAutor(), livro.getAutor()) &&
                Objects.equals(getTitulo(), livro.getTitulo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAutor(), getTitulo(), isEmprestado(), isReservado());
    }


}
