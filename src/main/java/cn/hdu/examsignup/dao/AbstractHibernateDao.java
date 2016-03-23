package cn.hdu.examsignup.dao;

import java.io.Serializable;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractHibernateDao<T extends Serializable> {

	private final Class<T> clazz;
	private SessionFactory sessionFactory;

	AbstractHibernateDao(Class<T> InitClazz) {
		clazz = InitClazz;
	}

	public void setSessionFactory(SessionFactory initSessionFactory) {
		this.sessionFactory = initSessionFactory;
	}

	public T getById(final Long id) {
		if (id == null) {
			throw new IllegalArgumentException("The argument is null");
		}
		return (T) this.getCurrentSession().get(this.clazz, id);
	}
	
	public T findById(final String id){
		if(id == null){
			throw new IllegalArgumentException("The argument is null");
		}
		return (T) this.getCurrentSession().get(this.clazz, id);
	}

	public List<T> findAll() {
		try {
			return this.getCurrentSession()
					.createQuery("from " + this.clazz.getName()).list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void save(final T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("The argument is null");
		}
		this.getCurrentSession().persist(entity);
	}

	public void update(final T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("The argument is null");
		}
		this.getCurrentSession().merge(entity);
	}

	public void delete(final T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("The argument is null");
		}
		this.getCurrentSession().delete(entity);
	}

	public void deleteById(final Long entityId) {
		final T entity = this.getById(entityId);
		if (entityId == null) {
			throw new IllegalArgumentException("The argument is null");
		}
		this.delete(entity);
	}

	public List<T> findByProperty(String propertyName, Object value) {
		try {
			String queryString = "from " + this.clazz.getName()
					+ " model where model." + propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public List<T> findByProperty(String propertyName, Object value, String institutionnum) {
		try {
			String queryString = "from " + this.clazz.getName()
					+ " model where model." + propertyName + "= ? and model.exInstitution.institutionnum =:institutionnum";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value).setParameter("institutionnum", institutionnum);
			return queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	protected final Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
}