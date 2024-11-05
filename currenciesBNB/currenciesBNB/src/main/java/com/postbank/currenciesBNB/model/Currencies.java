package com.postbank.currenciesBNB.model;

import org.springframework.data.annotation.Id;

public record Currencies ( @Id String code, double rate, String bglabel, String englabel) {

}
