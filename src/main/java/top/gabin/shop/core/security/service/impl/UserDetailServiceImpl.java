/**
 * Copyright (c) 2016 云智盛世
 * Created with UserDetailServiceImpl.
 */
package top.gabin.shop.core.security.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 *
 * @author linjiabin on  16/5/5
 */
@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("DEFAULT");
        grantedAuthorityList.add(admin);
        return new User(username, username, grantedAuthorityList);
    }

}
