package br.com.dogape.lanches.commom.core.helper;

import br.com.dogape.lanches.commom.core.model.dto.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {

    public static ResponseEntity<Object> ok(Object data) {
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK, data));
    }

    public static <T> ResponseEntity<Object> list(Page<T> page) {
        return ResponseEntity.ok(new ResponseListHelper<>(page));
    }

    public static ResponseEntity<Object> created(Object data) {
        return new ResponseEntity<>(new ResponseDto(HttpStatus.CREATED.value(), HttpStatus.CREATED, data),
                HttpStatus.CREATED);
    }

}
