package br.com.backEndVendas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Primary
    @Bean
    public RestTemplate getRestTemplate(){
        RestTemplate rt = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        rt.setRequestFactory(requestFactory);
        return rt;
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/pedido/**", "/produtos/**", "/pedido/atualizar/**","/pedido/cancelar/**", "/compras/**","/pedido/buscar/**",
                    "/item", "/item/listar/**", "/item/atualizar/**", "/item/deletar/**", "/notafiscal", "/notafiscal/buscar/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/calcular/vendedor/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().sameOrigin();
    }
}
