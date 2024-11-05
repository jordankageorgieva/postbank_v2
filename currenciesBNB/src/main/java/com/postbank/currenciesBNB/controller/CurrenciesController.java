package com.postbank.currenciesBNB.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postbank.currenciesBNB.model.Currencies;
import com.postbank.currenciesBNB.repository.CurrenciesRepository;

@RestController
public class CurrenciesController {
	
	private final CurrenciesRepository repository;

    public CurrenciesController(CurrenciesRepository repository) {
        this.repository = repository;
    }
	
	@GetMapping("/download-currencies")
    public  List<Currencies> displayDataCurrencies() {

		
        //NOT WORK - BULLSHEET
		/*
		 * boolean rowUpdate =repository.updateRateByCode("AUD", 3.2); if (rowUpdate) {
		 * System.out.println("row updatetd"); } else {
		 * System.out.println("row NOT updatetd"); }
		 */
            
        List<Currencies> curreencies= (List<Currencies>) repository.findAll();
        
        return curreencies;
    }
	
}
