package br.com.dogape.lanches.commom.core.model.dto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
public class ErrorDto {

    public ErrorDto(String field, String message) {
        this.field = field;
        this.message = message;
        this.type = null;
    }

    private final String field;
    private final String message;
    private final String type;
}
