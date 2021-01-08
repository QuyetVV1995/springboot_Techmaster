package com.example.demo.controller;

import com.example.demo.model.Currency;
import com.example.demo.model.ExchangeRate;
import com.example.demo.request.MoneyConvertRequest;
import com.example.demo.response.MoneyConvertResult;
import com.example.demo.service.MoneyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/exp")
public class ExperimentController {

    @Autowired
    private MoneyConverter moneyConverter;

    @GetMapping("/select")
    public String renderSelect(Model model) {
        model.addAttribute("moneyConvertRequest", new MoneyConvertRequest());

        ArrayList<Currency> currencies = new ArrayList<>();
        List<ExchangeRate> exchangeRates = moneyConverter.parseExchangeRate();
        Iterator<ExchangeRate> it = exchangeRates.iterator();
        while(it.hasNext()){
            currencies.add(new Currency(it.next().getCode(),"" ));
        }
        model.addAttribute("currencies", currencies);  //trả về danh sách mã 3 ký tự của tiền tệ và tên tiền tệ
        return "select";
    }

    @GetMapping("/convert")
    public String renderConvert(Model model) {
        model.addAttribute("moneyConvertRequest", new MoneyConvertRequest());

        ArrayList<Currency> currencies = new ArrayList<>();

        List<ExchangeRate> exchangeRates = moneyConverter.parseExchangeRate();
        Iterator<ExchangeRate> it = exchangeRates.iterator();
        while(it.hasNext()){
            currencies.add(new Currency(it.next().getCode(),"" ));
        }

//        float code = moneyConverter.getExchangeRate("USD");
//        model.addAttribute("exchangeRates", exchangeRates);

        model.addAttribute("currencies", currencies);  //trả về danh sách mã 3 ký tự của tiền tệ và tên tiền tệ
        return "convert";

    }

    @PostMapping("/convert")
    public String handleConvertMoney(@ModelAttribute MoneyConvertRequest request, Model model, BindingResult bindingResult){
        if(! bindingResult.hasErrors()){
            ArrayList<Currency> currencies = new ArrayList<>();
            List<ExchangeRate> exchangeRates = moneyConverter.parseExchangeRate();
            Iterator<ExchangeRate> it = exchangeRates.iterator();
            while(it.hasNext()){
                currencies.add(new Currency(it.next().getCode(),"" ));
            }
            MoneyConvertResult moneyConvertResult = convert(request);
            model.addAttribute("moneyConvertRequest", request);
            model.addAttribute("result", moneyConvertResult);
            model.addAttribute("currencies", currencies);  //trả về danh sách mã 3 ký tự của tiền tệ và tên tiền tệ
        }
        return "convert";
    }

    private MoneyConvertResult convert(MoneyConvertRequest request){
        float toCurrency = moneyConverter.getExchangeRate(request.getToCurrency());
        float fromCurrency = moneyConverter.getExchangeRate(request.getFromCurrency());
        float rs = request.getAmount() * toCurrency / fromCurrency;
        return  new MoneyConvertResult(rs);
    }
}