package com.valhalla.filters;

import java.util.UUID;

import org.reactivestreams.Publisher;
import org.slf4j.MDC;

import io.micronaut.core.annotation.Order;
import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;

@Filter(Filter.MATCH_ALL_PATTERN)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TrackingIdFilter implements HttpServerFilter {
	private static final String TRACKING_ID = "trackingId";

	@Override
	public Publisher<MutableHttpResponse<?>> doFilter(final HttpRequest<?> request, final ServerFilterChain chain) {
		MDC.put(TRACKING_ID, UUID.randomUUID()
			.toString());
		return chain.proceed(request);
	}

	@Override
	public int getOrder() {
		return -1;
	}
}

