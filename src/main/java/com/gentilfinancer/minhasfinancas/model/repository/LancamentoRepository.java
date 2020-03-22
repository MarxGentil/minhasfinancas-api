package com.gentilfinancer.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gentilfinancer.minhasfinancas.model.entity.Lancamento;
import com.gentilfinancer.minhasfinancas.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	//l.usuario onde usuario não é como está na chave estrangeira no bd id_usuari e sim como está na classe model
	//o mesmo para tipo
	@Query(value = 
			  "select sum(l.valor) from Lancamento l join l.usuario u "
			+ "where u.id = :idUsuario and l.tipo =:tipo group by u ")
	BigDecimal obterSaldoPorTipoLancamentoEUsuario(@Param("idUsuario") Long idULong, @Param("tipo") TipoLancamento tipo);
}
