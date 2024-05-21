package br.com.dogape.lanches.commom.core.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;

@Data
public class RequestResponseDto<T> {
    Boolean status = true;
    Optional<T> data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ObjectError> errors;

    public RequestResponseDto(Optional<T> data) {
        this.data = data;
        if (data.isEmpty()) this.status = false;
    }
}
