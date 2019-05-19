package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import hibernate.HibernateUtil;

@SuppressWarnings("unchecked")
public class DaoGeneric<E> {
	private EntityManager entityManager = HibernateUtil.geEntityManager();
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.merge(entidade);
		transaction.commit();
	}	

	public void excluir(E entidade) throws Exception {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.createNativeQuery("delete from " + entidade.getClass().getSimpleName() + " where id =" + id).executeUpdate();
		transaction.commit();
	}

	public E buscar(String urlLonga, Class<E> entidade) {
		entityManager.clear();
		try {
			E e = (E) entityManager.createQuery("from " + entidade.getSimpleName() + " where urlLonga = :urlLonga").setParameter("urlLonga", urlLonga).getSingleResult();
			return  e;
		} catch (NoResultException nre) {
			return null;
		}
	}
   
	public List<E> listar(Class<E> entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		List<E> lista = entityManager.createQuery("from " + entidade.getSimpleName() + " order by id").getResultList();
		transaction.commit();
		return lista;
	}
}