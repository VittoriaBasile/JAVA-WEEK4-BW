package dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import entities.Percorrenza;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PercorrenzaDAO {
	private final EntityManager em;

	public PercorrenzaDAO(EntityManager em) {
		this.em = em;
	}

	public void createByList(List<Percorrenza> percorrenze) throws HibernateException, ConstraintViolationException {
		if (percorrenze.size() > 0) {
			percorrenze.forEach(p -> create(p));
		} else {
			log.info("Lista percorrenze vuota!");
		}
	}

	public void create(Percorrenza p) throws HibernateException, ConstraintViolationException {
		EntityTransaction t = em.getTransaction();
		t.begin();
		em.persist(t);
		t.commit();
		log.info("Percorrenza aggiornata!");
	}

	public void update(Percorrenza p) throws HibernateException, ConstraintViolationException {
		Percorrenza found = em.find(Percorrenza.class, p);
		if (found != null) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			em.merge(found);
			transaction.commit();
			log.info("Percorrenza: " + found + " aggiornata!");
		} else {
			log.info("Percorrenza: " + p + " non trovata!");
		}
	}

	public void delete(String id) throws HibernateException, ConstraintViolationException {
		Percorrenza found = em.find(Percorrenza.class, UUID.fromString(id));
		if (found != null) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			em.remove(found);
			transaction.commit();
			log.info("Percorrenza con id " + id + " eliminata!");
		} else {
			log.info("Percorrenza con id " + id + " non trovata!");
		}
	}

	public Percorrenza findById(String id) throws HibernateException, ConstraintViolationException {
		return em.find(Percorrenza.class, UUID.fromString(id));
	}

	public List<Percorrenza> findAll() throws HibernateException, ConstraintViolationException {
		TypedQuery<Percorrenza> getAllQuery = em.createQuery("SELECT p FROM Percorrenza p", Percorrenza.class);
		return getAllQuery.getResultList();
	}

}
/*
 * select trattaid_id,veicoloid_id , count(trattaid_id) as numero from
 * percorrenze p where oraarrivo is not null group by trattaid_id, veicoloid_id
 * 
 * select trattaid_id ,veicoloid_id , sum(EXTRACT(EPOCH FROM (oraarrivo -
 * orapartenza))) as percorrenza from percorrenze p where oraarrivo is not null
 * group by trattaid_id, veicoloid_id
 * 
 * select trattaid_id, sum(EXTRACT(EPOCH FROM (oraarrivo -
 * orapartenza)))/count(trattaid_id) as percorrenzamedia from percorrenze p
 * where oraarrivo is not null group by trattaid_id
 */