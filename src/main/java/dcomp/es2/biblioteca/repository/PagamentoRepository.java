package dcomp.es2.biblioteca.repository;
import dcomp.es2.biblioteca.modelo.Pagamento;
import dcomp.es2.biblioteca.repository_interface.PagamentoRepositoryInterface;

public class PagamentoRepository extends Repository  implements PagamentoRepositoryInterface {
    
    private static PagamentoRepository instance;

    public static PagamentoRepository getInstance(){
        if (instance == null){
            instance = new PagamentoRepository();
        }
        return instance;
    }

    public PagamentoRepository() {
        entityManager = getEntityManager();
    }

    @Override
    public Pagamento salvar(Pagamento pagamento) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(pagamento);
            entityManager.getTransaction().commit();
            return pagamento;
        } catch (Exception ex) {
            //ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        return  null;
    }

    @Override
    public Pagamento atualizar(Pagamento pagamento) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(pagamento);
            entityManager.getTransaction().commit();
            return pagamento;
        } catch (Exception ex) {
            //ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        return  null;
    }

    public void remove(Pagamento pagamento) {
        try {
            entityManager.getTransaction().begin();
            pagamento = entityManager.find(Pagamento.class, pagamento.getId());
            entityManager.remove(pagamento);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }

    @Override
    public Pagamento getById(final int id) {
        return entityManager.find(Pagamento.class, id);
    }
}
