package com.postbank.currenciesBNB.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.postbank.currenciesBNB.model.Currencies;
import com.postbank.currenciesBNB.repository.CurrenciesRepository;
import com.postbank.currenciesBNB.service.BnbDataUpdate;

@RestController
public class CurrenciesController {

	@Autowired
	BnbDataUpdate bnbDataUpdate;

	private final CurrenciesRepository repository;

	public CurrenciesController(CurrenciesRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/download-currencies")
	public List<Currencies> displayDataCurrencies() {
		
		bnbDataUpdate.dataUpdate();

		List<Currencies> curreencies = (List<Currencies>) repository.findAll();

		return curreencies;
	}


//	@SendTo("/ws-download-currencies")
//	public String greeting() throws Exception {
//
//	    bnbDataUpdate.dataUpdate();
//
//		Thread.sleep(1000); // simulated delay
//
//		// forword this data to WS
//		Gson gson = new Gson();
//		String jsonArray = gson.toJson(onlyUpdatedCurrencies);
//
//		return jsonArray;
//	}

}
