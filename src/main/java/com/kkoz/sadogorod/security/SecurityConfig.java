package com.kkoz.sadogorod.security;

import com.kkoz.sadogorod.security.jwt.secret_key.ServiceJwtSecretKey;
import com.kkoz.sadogorod.services.ServiceUzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ServiceUzer serviceUzer;
    private final ServiceJwtSecretKey serviceJwtSecretKey;
    private final AuthEntryPoint authEntryPoint;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder,
                          ServiceUzer serviceUzer,
                          ServiceJwtSecretKey serviceJwtSecretKey,
                          AuthEntryPoint authEntryPoint,
                          CustomAuthenticationProvider customAuthenticationProvider) {
        this.passwordEncoder = passwordEncoder;
        this.serviceUzer = serviceUzer;
        this.serviceJwtSecretKey = serviceJwtSecretKey;
        this.authEntryPoint = authEntryPoint;
        //this.serviceApplication = serviceApplication;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //.exceptionHandling()
                //.authenticationEntryPoint(this.authEntryPoint)
                //.and()
                //.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //.and()
                //.addFilterBefore(new FilterJwtExceptionHandler(), UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(new FilterJwtVerifier(this.serviceJwtSecretKey), UsernamePasswordAuthenticationFilter.class)
                //.authorizeRequests()
                //.antMatchers("/webjars/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**").permitAll()
                //.antMatchers("/auth/**").permitAll()
                // Для доступа к ресурсам (файлам)
                //.antMatchers(HttpMethod.GET, "/api/file/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/application/pdf/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/report-expenses/pdf/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/report-activity/pdf/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/report-index/pdf/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/application/zip/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/application/zip/sig/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/application/refusal/file/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/consolidated/report-expenses/excel").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/consolidated/report-index/excel").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/consolidated/report-activity/excel").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/subsidy/pdf/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/subsidy/excel/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/additional-consideration/pdf/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/additional-consideration/excel/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/api/summary-data/excel/*").permitAll()
                // Для доступа к ресурсам (файлам) - END
                //.anyRequest()
                //.authenticated()
                //.accessDecisionManager(this.accessDecisionManager())
        ;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(
                new WebExpressionVoter(),
                new RoleVoter(),
                new AuthenticatedVoter()//,
                //new MethodAccessVoter(this.serviceUzer, this.serviceApplication)
        );

        return new UnanimousBased(decisionVoters);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // TODO: DELETE THIS
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(serviceUzer);

        return provider;
    }
    // TODO: DELETE THIS - END

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.customAuthenticationProvider);
    }
}
