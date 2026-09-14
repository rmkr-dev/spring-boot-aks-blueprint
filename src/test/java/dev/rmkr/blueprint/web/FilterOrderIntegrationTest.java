package dev.rmkr.blueprint.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FilterOrderIntegrationTest {

    @Autowired
    private RequestIdFilter requestIdFilter;

    @Autowired
    private SecurityHeadersFilter securityHeadersFilter;

    @Test
    void requestIdFilterRunsBeforeSecurityHeadersFilter() {
        Order requestIdOrder = RequestIdFilter.class.getAnnotation(Order.class);
        Order securityOrder = SecurityHeadersFilter.class.getAnnotation(Order.class);
        assertThat(requestIdOrder).isNotNull();
        assertThat(securityOrder).isNotNull();
        assertThat(requestIdOrder.value()).isLessThan(securityOrder.value());

        List<Object> filters = List.of(securityHeadersFilter, requestIdFilter);
        filters = filters.stream().sorted(AnnotationAwareOrderComparator.INSTANCE).toList();
        assertThat(filters.get(0)).isSameAs(requestIdFilter);
        assertThat(filters.get(1)).isSameAs(securityHeadersFilter);
    }
}
