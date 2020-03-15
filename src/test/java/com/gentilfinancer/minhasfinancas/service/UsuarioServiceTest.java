package com.gentilfinancer.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gentilfinancer.minhasfinancas.exceptions.ErroAutenticacao;
import com.gentilfinancer.minhasfinancas.exceptions.RegraNegocioException;
import com.gentilfinancer.minhasfinancas.model.entity.Usuario;
import com.gentilfinancer.minhasfinancas.model.repository.UsuarioRepository;
import com.gentilfinancer.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)

//Com o ActiveProfiles apontando para testes, a app vai em busca deste properties
//e vai apontar para a base h2 que é uma base em memória.
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean //cria um mock
	UsuarioRepository repository;

	@Test
	public void deveSalvarUmUsuario() {
		//cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
						  .id(1l)
						  .nome("nome")
						  .email("marx@gmail.com")
						  .senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//Verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("marx@gmail.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//cenário
		String email = "marx@gmail.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//ação
		org.junit.jupiter.api.Assertions
			.assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario));
		
		//Verificação
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenário
		String email = "marx@gmail.com";
		String senha = "senha";
		Long id = (long) 1;
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(id).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//ação
		Usuario result = service.autenticar(email, senha);
		
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenário
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("marx@gmail.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//ação
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("marx@gmail.com", "123"));
		
		//Verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		//cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		//ação
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("marx@gmail.com", "123"));

		//Verificação
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");
	}
	
	@Test
	public void deveValidarEmail() {
		//cenário		
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação/execução
		service.validarEmail("marx@gmail.com");
	}

	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//ação
		org.junit.jupiter.api.Assertions
			.assertThrows(RegraNegocioException.class, () -> service.validarEmail("marx@gmail.com"));
	}
}
