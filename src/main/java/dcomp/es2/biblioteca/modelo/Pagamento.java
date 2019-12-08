package dcomp.es2.biblioteca.modelo;

import javax.persistence.*;

@Entity
public class Pagamento {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    //@ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST})
    //private Emprestimo emprestimo;

    double valorPago;

    public Pagamento(){}

    public Pagamento(double valorPago) {
        this.valorPago = valorPago;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", valorPago=" + valorPago +
                '}';
    }
}
