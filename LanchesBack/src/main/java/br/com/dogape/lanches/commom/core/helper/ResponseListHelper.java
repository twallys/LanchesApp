package br.com.dogape.lanches.commom.core.helper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ResponseListHelper<T> extends PageImpl<T> implements Page<T> {
    public transient List<T> data;

    @JsonIgnore
    public transient List<T> content;

    @JsonIgnore
    public transient Pageable pageable;

    @Serial
    private static final long serialVersionUID = 867755909294344406L;

    public ResponseListHelper(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.data = content;
        this.pageable = pageable;
    }

    public ResponseListHelper(Page<T> page) {
        this(page.getContent(), Pageable.unpaged(), page.getContent().size());
        this.data = page.getContent();
    }

    public List<Sort.Order> getOrders() {
        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order order : this.getPageable().getSort()) {
            orders.add(order);
        }
        return orders;
    }
}