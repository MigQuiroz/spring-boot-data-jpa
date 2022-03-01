package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

public interface IClienteDao {

	public List<Cliente> findAll();
	//guardar cliente
	public void save(Cliente cliente);
	//para buscar cliente por id
	public Cliente findOne(Long id);
}
 