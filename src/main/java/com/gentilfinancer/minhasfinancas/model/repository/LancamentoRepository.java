package com.gentilfinancer.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gentilfinancer.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
