package com.valhalla.filters;

import java.lang.invoke.MethodHandles;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;

@Filter(Filter.MATCH_ALL_PATTERN)
public class RequestFilter implements HttpServerFilter {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup()
		.lookupClass());

	@Override
	public Publisher<MutableHttpResponse<?>> doFilter(final HttpRequest<?> request, final ServerFilterChain chain) {
		final String message = request.getMethodName() + StringUtils.SPACE + request.getUri() + StringUtils.SPACE + "(accessed by " + request.getRemoteAddress()
			.getAddress() + ")";
		LOG.info(message);
		return chain.proceed(request);
	}
}
