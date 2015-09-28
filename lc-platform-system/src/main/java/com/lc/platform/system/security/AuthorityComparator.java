package com.lc.platform.system.security;

import java.io.Serializable;
import java.util.Comparator;

import org.springframework.security.core.GrantedAuthority;

/**
 * 认证比较器
 * @author chenjun
 *
 */
public class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
	private static final long serialVersionUID = -3169245515475191835L;

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