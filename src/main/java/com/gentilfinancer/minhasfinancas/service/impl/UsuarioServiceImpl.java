package com.gentilfinancer.minhasfinancas.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gentilfinancer.minhasfinancas.exceptions.ErroAutenticacao;
import com.gentilfinancer.minhasfinancas.exceptions.RegraNegocioException;
import com.gentilfinancer.minhasfinancas.model.entity.Usuario;
import com.gentilfinancer.minhasfinancas.model.repository.UsuarioRepository;
import com.gentilfinancer.minhasfinancas.service.UsuarioService;

import lombok.Data;

@Data
@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		
		return usuario.get();
	}

	@Override
	@org.springframework.transaction.annotation.Transactional
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe usuário cadastrado com este email.");
		}
	}

}
