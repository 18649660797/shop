/**
 * Copyright (c) 2016 云智盛世
 * Created with SecurityConfig.
 */
package top.gabin.shop.config.java;

/**
 * Class description
 *
 * @author linjiabin on  16/5/4
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource(name = "userDetailService")
    private UserDetailsService userDetailService;
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源过滤
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("**.js");
        web.ignoring().antMatchers("**.css");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 暂时禁用csrf,并自定义登录页和登出URL
        http.csrf().disable();
        // 卧槽,这里自己乱加一个不允许iframe加载的头信息,能不能让自己配置,我去
        http.headers().disable();
        http.authorizeRequests()
                .and().formLogin().loginPage("/login").failureUrl("/login?error")
                .loginProcessingUrl("/login_submit").usernameParameter("username").passwordParameter("password").permitAll()
                .and().logout().logoutUrl("/logout").permitAll()
//                .and().authorizeRequests().antMatchers("/admin/*", "/admin").hasAuthority("DEFAULT")
                // 权限不足页面跳转
                .and().exceptionHandling().accessDeniedPage("/exception/403")
                // 记住密码
                .and().rememberMe().key("9D119EE5A2B7DAF6B4DC1EF871D0AC3C").tokenValiditySeconds(1209600).rememberMeParameter("remember-me")
        ;

    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>();
        providers.add(authenticationProvider());
        ProviderManager providerManager = new ProviderManager(providers);
        return providerManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * 用于实现第三方登录,不需要校验密码的情况
     * @return
     */
    @Bean
    public DaoAuthenticationProvider thirdPartyAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Bean
    public AccessDecisionManager unanimousBased() {
        List roleVoterList = new ArrayList();
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("ROLE_");
        roleVoterList.add(roleVoter);
        UnanimousBased unanimousBased = new UnanimousBased(roleVoterList);
        return unanimousBased;

    }

}