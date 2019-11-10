package br.com.psoft.ajude;

import br.com.psoft.ajude.controllers.filters.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AJuDeBackendApplication {

	@Bean
	public FilterRegistrationBean<TokenFilter> filterJwt() {

		FilterRegistrationBean<TokenFilter> filterRB = new FilterRegistrationBean<>();
		filterRB.setFilter(new TokenFilter());
		filterRB.addUrlPatterns("/ajude/usuarios");

		return filterRB;
	}

	public static void main(String[] args) {

		SpringApplication.run(AJuDeBackendApplication.class, args);
	}

}
