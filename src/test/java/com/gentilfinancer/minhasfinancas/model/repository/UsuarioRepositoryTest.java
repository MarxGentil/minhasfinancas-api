package com.gentilfinancer.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gentilfinancer.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
//Com o ActiveProfiles apontando para testes, a app vai em busca deste properties
//e vai apontar para a base h2 que é uma base em memória.
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//ação/execução
		boolean result = repository.existsByEmail("marx@gmail.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradocomOEmail() {
		//ação/execução
		boolean result = repository.existsByEmail("marx@gmail.com");
		
		//verificação
		Assertions.assertThat(result).isFalse();
		
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario = criarUsuario();
		
		//ação
		Usuario usuarioSalvo = repository.save(usuario);
		
		//Verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}

	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		
		entityManager.persist(usuario);
		
		//Verificação
		Optional<Usuario> result = repository.findByEmail("marx@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPormailQuandoNaoExisteNaBase() {
		//Verificação
		Optional<Usuario> result = repository.findByEmail("marx@gmail.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();
	}

	public static Usuario criarUsuario() {
		//cenário
		return Usuario
			  .builder().nome("Marx")
			  .email("marx@gmail.com")
			  .senha("senha")
			  .build();
	}
}
