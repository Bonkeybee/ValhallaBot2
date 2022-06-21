package com.valhalla.resources;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.validation.Validated;

@Validated
@Controller
public class Ping {

	@Get(uri = "/ping", produces = MediaType.TEXT_PLAIN)
	public String ping() {
		return HttpStatus.OK.toString();
	}
}
