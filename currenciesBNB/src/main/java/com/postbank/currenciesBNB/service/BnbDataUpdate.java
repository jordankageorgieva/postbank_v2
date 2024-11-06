package com.postbank.currenciesBNB.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.postbank.currenciesBNB.config.WebSocketConfig;
import com.postbank.currenciesBNB.model.Currencies;
import com.postbank.currenciesBNB.repository.CurrenciesRepository;

@Service
public class BnbDataUpdate {

	@Autowired
	BnbCurrenciesReadService bnbCurrenciesReadService;

	@Autowired
	CurrenciesRepository repository;

	public boolean dataUpdate() {

		boolean isDataUpdated = false;

		// свали най-актуалната информация за всички валутни курсове от БНБ
		// get the best actual information for all BNB currencies
		Map<String, String> bnbCUR = readBnbData();

		// only update the corresponding data in the DB
		isDataUpdated = updateMyBnbCUR(bnbCUR);

		// extract data in the data.sql file - DB synchronization

		return isDataUpdated;
	}

	// свали най-актуалната информация за всички валутни курсове от БНБ
	// get the best actual information for all BNB currencies
	private Map<String, String> readBnbData() {

		bnbCurrenciesReadService.connect();

		Map<String, String> bnbCUR = bnbCurrenciesReadService.xmlCurrenciesParser();

		System.out.println("++++++++++++ final out data ++++++++++++++++++++");
		bnbCUR.forEach((key, value) -> System.out.println(key + " = " + value));

		return bnbCUR;

	}

	private boolean updateMyBnbCUR(Map<String, String> bnbCUR) {

		List<Currencies> curreencies = (List<Currencies>) repository.findAll();

		List<Currencies> onlyUpdateCurreencies = new ArrayList<Currencies>();

		int i = 0;
		boolean isDataUpdated = false;
		for (Currencies cur : curreencies) {

			String code = cur.code();
			String bglabel = cur.bglabel();
			String englabel = cur.englabel();

			if (bnbCUR.containsKey(cur.code()) && !"RUB".equals(cur.code())) {

				double rate = Double.parseDouble(bnbCUR.get(cur.code()));

				System.out.println(cur.code() + " old rate is " + cur.rate());
				System.out.println(cur.code() + " new rate is " + rate);

				if (cur.rate() != rate) {
					// make update
					System.out.println("row to be updated " + i++);
					
					// save NEW CUR

					Currencies newCUR = new Currencies(code, rate, bglabel, englabel);
					onlyUpdateCurreencies.add(newCUR);
					repository.save(newCUR);

					isDataUpdated = true;
				}
			}
		}

		// forword this data to WS
		Gson gson = new Gson();
		String jsonArray = gson.toJson(onlyUpdateCurreencies);
		WebSocketConfig.onlyUpdateCurreencies = jsonArray;

		return isDataUpdated;

	}

}
