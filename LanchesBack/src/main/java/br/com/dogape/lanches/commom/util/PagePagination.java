package br.com.dogape.lanches.commom.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class PagePagination<T> extends PageImpl<T> implements Page<T> {

    @Serial
    private static final long serialVersionUID = 867755909294344406L;

    public PagePagination(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PagePagination(List<T> content) {
        this(content, Pageable.unpaged(), null == content ? 0 : content.size());
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        for (Order order : this.getPageable().getSort()) {
            orders.add(order);
        }
        return orders;
    }
}