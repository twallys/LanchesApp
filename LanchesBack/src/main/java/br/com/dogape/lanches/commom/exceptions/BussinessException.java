package br.com.dogape.lanches.commom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BussinessException extends RuntimeException {
    public BussinessException(String message) {
        super(message);
    }
}
