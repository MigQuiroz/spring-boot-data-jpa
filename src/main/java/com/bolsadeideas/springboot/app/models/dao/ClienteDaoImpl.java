package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Repository
public class ClienteDaoImpl implements IClienteDao {

	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return em.createQuery("from Cliente").getResultList();
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		//si se cumple el if actualiza
		if(cliente.getId() != null && cliente.getId() > 0) {
			em.merge(cliente);
		}else {
			
		//crea un nuevo cliente
		em.persist(cliente);
		}
	}

	@Override
	public Cliente findOne(Long id) {
		//se agrego para buscar usuario y editar
		return em.find(Cliente.class, id);
	}

}
