package com.postbank.currenciesBNB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.postbank.currenciesBNB.config.WebSocketConfig;
import com.postbank.currenciesBNB.model.Currencies;
import com.postbank.currenciesBNB.repository.CurrenciesRepository;
import com.postbank.currenciesBNB.service.BnbCurrenciesReadService;

@Component
public class UpdateData{   
	
	@Autowired
	BnbCurrenciesReadService bnbCurrenciesReadService;
	
	@Autowired
	CurrenciesRepository repository;
	
    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 * * * * *") // Cron expression for running every minute
    public void loadData()
    {    	
    	 //update the two data source - BNB data + DB data
    	System.out.println("New data update start " + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()));
        dataUpdate();
    }

	private void dataUpdate() {
		
		//свали най-актуалната информация за всички валутни курсове от БНБ
		// get the best actual information for all BNB currencies
		Map<String, String> bnbCUR= readBnbData();
		
		//only update the corresponding data in the DB
		updateMyBnbCUR(bnbCUR);	 
		
		//extract data in the data.sql file - DB synchronization
	}
	
	//свали най-актуалната информация за всички валутни курсове от БНБ
		// get the best actual information for all BNB currencies
		private Map<String, String> readBnbData() {
			
			bnbCurrenciesReadService.connect();
			 
			Map<String, String> bnbCUR = bnbCurrenciesReadService.xmlCurrenciesParser();
			 
			System.out.println("++++++++++++ final out data ++++++++++++++++++++");
	        bnbCUR.forEach(
	                (key, value)
	                    -> System.out.println(key + " = " + value));
	        
	        return bnbCUR;
			
		}
		
		private void updateMyBnbCUR(Map<String, String> bnbCUR) {
			
			List<Currencies> curreencies= (List<Currencies>) repository.findAll();
			
			List<Currencies> onlyUpdateCurreencies = new ArrayList<Currencies>();
			
			int i=0;
			for (Currencies cur : curreencies) {
				
				String code = cur.code();
				String bglabel = cur.bglabel();
				String englabel = cur.englabel();
				
				if (bnbCUR.containsKey(cur.code()) &&  cur.rate()!= 0.0) {
					
					double rate = Double.parseDouble(bnbCUR.get(cur.code()));
								
					
					System.out.println(cur.code() + " old rate is " + cur.rate());
					System.out.println(cur.code() + " new rate is " + rate);
					
					if (cur.rate() != rate) {
						//make update 
						System.out.println("row to be updated " + i++ );
						//delete OLD CUR
						//repository.delete(cur);
						
						//save NEW CUR
						
						Currencies newCUR= new Currencies(code, rate, bglabel, englabel);
						onlyUpdateCurreencies.add(newCUR);
					    repository.save(newCUR);
					}
				}
			}
			
			for (Iterator iterator = onlyUpdateCurreencies.iterator(); iterator.hasNext();) {
				Currencies currencies = (Currencies) iterator.next();
				
				System.out.println(currencies);
				
			}
			
			//forword this data to WS
			Gson gson = new Gson();
		    String jsonArray = gson.toJson(onlyUpdateCurreencies);
			WebSocketConfig.onlyUpdateCurreencies =jsonArray;
			
		}
}
