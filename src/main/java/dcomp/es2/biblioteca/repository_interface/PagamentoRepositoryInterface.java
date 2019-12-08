package dcomp.es2.biblioteca.repository_interface;

import dcomp.es2.biblioteca.modelo.Pagamento;

public interface PagamentoRepositoryInterface {
    public Pagamento getById(final int id);
    public Pagamento salvar(Pagamento pagamento);
    public Pagamento atualizar(Pagamento pagamento);
}
