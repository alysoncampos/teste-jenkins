package br.com.dbc.dbcarapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    
    private final TokenService tokenService;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((auth) ->
                        auth.antMatchers("/cliente").permitAll()
                                //usuario
                                .antMatchers(HttpMethod.PUT, "/usuario/ativar-desativar-usuario/{idUsuario}").hasRole("ADMIN")
                                .antMatchers(HttpMethod.POST, "/usuario/cadastrar-admin").hasRole("ADMIN")
                                .antMatchers(HttpMethod.PUT, "/usuario/atualizar").hasAnyRole("ADMIN", "CLIENTE", "FUNCIONARIO")
                                .antMatchers(HttpMethod.PUT, "/usuario/atualizar-senha").hasAnyRole("ADMIN", "CLIENTE", "FUNCIONARIO")
                                .antMatchers(HttpMethod.GET, "/usuario/logado").hasAnyRole("ADMIN", "CLIENTE", "FUNCIONARIO")
                                
                                //cliente
                                .antMatchers(HttpMethod.GET, "/cliente/listar/{idCliente}").hasAnyRole("ADMIN", "FUNCIONARIO")
                                .antMatchers(HttpMethod.DELETE, "/cliente/{idCliente}").hasRole("ADMIN")
                                .antMatchers(HttpMethod.GET, "/cliente/listar").hasAnyRole("ADMIN", "FUNCIONARIO")
                                .antMatchers(HttpMethod.GET, "/cliente/infos-pessoais").hasRole("CLIENTE")
                                .antMatchers(HttpMethod.PUT, "/cliente/atualizar").hasRole("CLIENTE")
                                
                                //funcionario
                                .antMatchers(HttpMethod.GET, "/funcionario/listar-para-aluguel").hasRole("CLIENTE")
                                .antMatchers("/funcionario/**").hasRole("ADMIN")
                                
                                //carro
                                .antMatchers(HttpMethod.DELETE, "/carro/{idCarro}").hasRole("ADMIN")
                                .antMatchers(HttpMethod.PUT, "/carro/{idCarro}").hasAnyRole("ADMIN", "FUNCIONARIO")
                                .antMatchers(HttpMethod.GET, "/carro/relatorio-carro").hasAnyRole("ADMIN", "FUNCIONARIO")
                                .antMatchers(HttpMethod.GET, "/carro").hasAnyRole("ADMIN", "FUNCIONARIO", "CLIENTE")
                                .antMatchers(HttpMethod.POST, "/carro").hasAnyRole("ADMIN", "FUNCIONARIO")
                                
                                //aluguel
                                .antMatchers(HttpMethod.PUT, "/aluguel/cancelar-contrato/{idAluguel}").hasRole("CLIENTE")
                                .antMatchers(HttpMethod.PUT, "/aluguel/devolver-carro/{idAluguel}").hasRole("CLIENTE")
                                .antMatchers(HttpMethod.PUT, "/aluguel/{idAluguel}").hasRole("CLIENTE")
                                .antMatchers(HttpMethod.POST, "/aluguel/{idCarro}").hasRole("CLIENTE")
                                .antMatchers(HttpMethod.GET, "/aluguel/meus-alugueis").hasRole("CLIENTE")
                                .antMatchers(HttpMethod.GET, "/aluguel/relatorio-aluguel").hasAnyRole("ADMIN", "FUNCIONARIO")
                                .antMatchers(HttpMethod.GET, "/aluguel").hasAnyRole("ADMIN","FUNCIONARIO")
                                .anyRequest().authenticated()
                );
        
        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/",
                "/usuario/login")
        );
    }
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
