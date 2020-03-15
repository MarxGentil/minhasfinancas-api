package com.gentilfinancer.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gentilfinancer.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	//Optional porque ele pode existir ou não
	//finByEmail Email porque tenho esta propriedade na classe model de Usuario
	//tem que colocar o nome igual. Se eu quizesse Email e Nome, poderia ser
	//findByEmailAndNome(String email, String nome)
	//esta seria uma alternativa
	//Optional<Usuario> findByEmail(String email);	
	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);

}
