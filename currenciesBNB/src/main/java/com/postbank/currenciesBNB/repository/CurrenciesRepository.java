package com.postbank.currenciesBNB.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.postbank.currenciesBNB.model.Currencies;

@Repository
public interface CurrenciesRepository extends CrudRepository<Currencies,Integer>{
	
	Currencies
	findByCode(String code);
}
