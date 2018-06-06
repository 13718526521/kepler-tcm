package com.kepler.tcm.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;


public class DetailsUser implements UserDetails, CredentialsContainer {
	

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;;

	
	/**
	 * 用户名
	 */
    private final String username;
    
    /**
     * 密码
     */
	private String password;
	
	/**
	 * 账户是否可用
	 */
    private final boolean enabled;
    
    
    /**
     * 账户是否过期
     */
    private final boolean accountNonExpired;
    
    /**
     * 账户是否锁住
     */
    private final boolean accountNonLocked;
    
    /**
     * 证书是否过期
     */
    private final boolean credentialsNonExpired;
    
	/**
	 * 权限集
	 */
    private final Set<GrantedAuthority> authorities;
    
    /**
     * 系统用户信息
     */
    private SysUser sysUser ;
    
    
    /**
     * 
     * @param 系统用户
     * @param authorities 账户权限集合
     */
    public DetailsUser(SysUser sysUser,
    		Collection<? extends GrantedAuthority> authorities) {
    	
    	  this(sysUser.getLoginName(), sysUser.getPassword(), true, true, true, true, authorities);
    	  this.sysUser = sysUser ;
    }
    
    public DetailsUser(SysUser sysUser,boolean enabled,boolean accountNonLocked,
    		Collection<? extends GrantedAuthority> authorities) {
    	
    	  this(sysUser.getLoginName(), sysUser.getPassword(), enabled, accountNonLocked, true, true, authorities);
    	  this.sysUser = sysUser ;
    }
    
    /**
     * 
     * @param username 用户名
     * @param password 密码
     * @param authorities 账户权限集合
     */
    public DetailsUser(String username, String password,
    		Collection<? extends GrantedAuthority> authorities) {
    	
    	  this(username, password, true, true, true, true, authorities);
    }
    
    
    /**
     * 
     * @param username 用户名
     * @param password 密码
     * @param enabled  用户是否可用
     * @param authorities 账户权限集合
     */
    public DetailsUser(String username, String password,boolean enabled,
    		Collection<? extends GrantedAuthority> authorities) {
    	
    	  this(username, password, enabled, true, true, true, authorities);
    }
    
    /**
     * 
     * @param username 用户名
     * @param password 密码
     * @param enabled  用户是否可用
     * @param accountNonLocked 账户是否被锁
     * @param authorities 账户权限集合
     */
    public DetailsUser(String username, String password,boolean enabled,
    		boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
    	
    	  this(username, password, enabled, accountNonLocked, true, true, authorities);
    }
    
    /**
     * 
     * @param username 用户名
     * @param password 密码
     * @param enabled  用户是否可用
     * @param accountNonLocked 账户是否被锁
     * @param accountNonExpired 账户是否过期
     * @param authorities 账户权限集合
     */
    public DetailsUser(String username, String password,boolean enabled,boolean accountNonLocked,
    		boolean accountNonExpired, Collection<? extends GrantedAuthority> authorities) {
    	
    	  this(username, password, enabled, accountNonLocked, accountNonExpired, true, authorities);
    }
    
    
    /**
     * 
     * @param username 用户名
     * @param password 密码
     * @param enabled  用户是否启用
     * @param accountNonLocked 账户是否被锁
     * @param accountNonExpired 账户是否过期
     * @param credentialsNonExpired
     * @param authorities 账户权限集合
     */
    public DetailsUser(String username, String password, boolean enabled, boolean accountNonLocked,
            boolean accountNonExpired, boolean credentialsNonExpired, Collection<? extends GrantedAuthority> authorities) {
        if (((username == null) || "".equals(username)) || (password == null)) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }

        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }

   

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void eraseCredentials() {
        password = null;
    }
    

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    public void setSysUser(SysUser sysUser){
    	this.sysUser = sysUser ;
    }
    
    public SysUser getSysUser(){
    	return sysUser ;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities =
            new TreeSet<GrantedAuthority>(new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    
    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof DetailsUser) {
            return username.equals(((DetailsUser) rhs).username);
        }
        return false;
    }


    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
    	
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");
        sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
        sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
        sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

        if (!authorities.isEmpty()) {
        	
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (GrantedAuthority auth : authorities) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
        	
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
	
}
