package com.bread.app.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@RestController
public class PaymentAPIController {

    private IamportClient api;
	
    public PaymentAPIController() {
    	// REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
    	this.api = new IamportClient("8722736711151264","YrGDTA681mLC0DVBWDM2VuKKuULN5M0xWoYYblWO1rFafbyj58125fwAenIQoDWd697EaZ6VwVCj6OhM");
    }
    
    @ResponseBody
	@RequestMapping(value="/cart/verifyIamport/{imp_uid}")
	public IamportResponse<Payment> paymentByImpUid(
			Model model
			, Locale locale
			, HttpSession session
			, @PathVariable(value= "imp_uid") String imp_uid) throws IamportResponseException, IOException{	
    	
			return api.paymentByImpUid(imp_uid);
	}
    
    @ResponseBody
	@RequestMapping(value="/search/verifyIamport/{imp_uid}")
	public IamportResponse<Payment> paymentByImpUid2(
			Model model
			, Locale locale
			, HttpSession session
			, @PathVariable(value= "imp_uid") String imp_uid) throws IamportResponseException, IOException{	
    	
			return api.paymentByImpUid(imp_uid);
	}
}
