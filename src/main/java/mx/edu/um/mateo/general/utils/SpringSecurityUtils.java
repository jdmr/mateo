/* Copyright 2006-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.edu.um.mateo.general.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mx.edu.um.mateo.general.model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Helper methods.
 * 
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
@SuppressWarnings("deprecation")
@Component
public class SpringSecurityUtils {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private RoleHierarchy roleHierarchy;
	/**
	 * Default value for the name of the Ajax header.
	 */
	public static final String AJAX_HEADER = "X-Requested-With";
	/**
	 * Used to ensure that all authenticated users have at least one granted
	 * authority to work around Spring Security code that assumes at least one.
	 * By granting this non-authority, the user can't do anything but gets past
	 * the somewhat arbitrary restrictions.
	 */
	public static final String NO_ROLE = "ROLE_NO_ROLES";

	public SpringSecurityUtils() {
	}

	/**
	 * Extract the role names from authorities.
	 * 
	 * @param authorities
	 *            the authorities (a collection or array of
	 *            {@link GrantedAuthority}).
	 * @return the names
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> authoritiesToRoles(final Object authorities) {
		Set<String> roles = new HashSet<>();
		for (GrantedAuthority authority : (Collection<GrantedAuthority>) authorities) {
			String authorityName = authority.getAuthority();
			if (null == authorityName) {
				throw new IllegalArgumentException(
						"Cannot process GrantedAuthority objects which return null from getAuthority() - attempting to process "
								+ authority);
			}
			roles.add(authorityName);
		}

		return roles;
	}

	/**
	 * Get the current user's authorities.
	 * 
	 * @return a list of authorities (empty if not authenticated).
	 */
	public static Collection<GrantedAuthority> getPrincipalAuthorities() {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication == null) {
			return Collections.emptyList();
		}

		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication
				.getAuthorities();
		if (authorities == null) {
			return Collections.emptyList();
		}

		// remove the fake role if it's there
		Collection<GrantedAuthority> copy = new ArrayList<>(authorities);
		for (Iterator<GrantedAuthority> iter = copy.iterator(); iter.hasNext();) {
			if (iter.next().getAuthority().equals(NO_ROLE)) {
				iter.remove();
			}
		}

		return copy;
	}

	/**
	 * Split the role names and create {@link GrantedAuthority}s for each.
	 * 
	 * @param roleNames
	 *            comma-delimited role names
	 * @return authorities (possibly empty)
	 */
	public static List<GrantedAuthority> parseAuthoritiesString(
			final String roleNames) {
		List<GrantedAuthority> requiredAuthorities = new ArrayList<>();
		for (String auth : StringUtils
				.commaDelimitedListToStringArray(roleNames)) {
			auth = auth.trim();
			if (auth.length() > 0) {
				requiredAuthorities.add(new GrantedAuthorityImpl(auth));
			}
		}

		return requiredAuthorities;
	}

	/**
	 * Find authorities in <code>granted</code> that are also in
	 * <code>required</code>.
	 * 
	 * @param granted
	 *            the granted authorities (a collection or array of
	 *            {@link SpringSecurityUtils}).
	 * @param required
	 *            the required authorities (a collection or array of
	 *            {@link SpringSecurityUtils}).
	 * @return the authority names
	 */
	public static Set<String> retainAll(final Object granted,
			final Object required) {
		Set<String> grantedRoles = authoritiesToRoles(granted);
		Set<String> requiredRoles = authoritiesToRoles(required);
		grantedRoles.retainAll(requiredRoles);
		return grantedRoles;
	}

	/**
	 * Check if the current user has all of the specified roles.
	 * 
	 * @param roles
	 *            a comma-delimited list of role names
	 * @return <code>true</code> if the user is authenticated and has all the
	 *         roles
	 */
	public boolean ifAllGranted(final String roles) {
		Collection<GrantedAuthority> inferred = findInferredAuthorities(getPrincipalAuthorities());
		return inferred.containsAll(parseAuthoritiesString(roles));
	}

	/**
	 * Check if the current user has none of the specified roles.
	 * 
	 * @param roles
	 *            a comma-delimited list of role names
	 * @return <code>true</code> if the user is authenticated and has none the
	 *         roles
	 */
	public boolean ifNotGranted(final String roles) {
		Collection<GrantedAuthority> inferred = findInferredAuthorities(getPrincipalAuthorities());
		Set<String> grantedCopy = retainAll(inferred,
				parseAuthoritiesString(roles));
		return grantedCopy.isEmpty();
	}

	/**
	 * Check if the current user has any of the specified roles.
	 * 
	 * @param roles
	 *            a comma-delimited list of role names
	 * @return <code>true</code> if the user is authenticated and has any the
	 *         roles
	 */
	public boolean ifAnyGranted(final String roles) {
		Collection<GrantedAuthority> inferred = findInferredAuthorities(getPrincipalAuthorities());
		Set<String> grantedCopy = retainAll(inferred,
				parseAuthoritiesString(roles));
		return !grantedCopy.isEmpty();
	}

	/**
	 * Check if the current user is switched to another user.
	 * 
	 * @return <code>true</code> if logged in and switched
	 */
	public boolean isSwitched() {
		return ifAllGranted(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR);
	}

	/**
	 * Get the username of the original user before switching to another.
	 * 
	 * @return the original login name
	 */
	public String getSwitchedUserOriginalUsername() {
		if (isSwitched()) {
			Authentication authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			for (GrantedAuthority auth : authentication.getAuthorities()) {
				if (auth instanceof SwitchUserGrantedAuthority) {
					return ((SwitchUserGrantedAuthority) auth).getSource()
							.getName();
				}
			}
		}
		return null;
	}

	/**
	 * Rebuild an Authentication for the given username and register it in the
	 * security context. Typically used after updating a user's authorities or
	 * other auth-cached info.
	 * <p/>
	 * Also removes the user from the user cache to force a refresh at next
	 * login.
	 * 
	 * @param username
	 *            the user's login name
	 * @param password
	 *            optional
	 */
	public void reauthenticate(final String username, final String password) {
		UserDetails userDetails = userDetailsService
				.loadUserByUsername(username);
		SecurityContextHolder.getContext()
				.setAuthentication(
						new UsernamePasswordAuthenticationToken(userDetails,
								password == null ? userDetails.getPassword()
										: password, userDetails
										.getAuthorities()));
	}

	private Collection<GrantedAuthority> findInferredAuthorities(
			final Collection<GrantedAuthority> granted) {
		@SuppressWarnings("unchecked")
		Collection<GrantedAuthority> reachable = (Collection<GrantedAuthority>) roleHierarchy
				.getReachableGrantedAuthorities(granted);
		if (reachable == null) {
			return Collections.emptyList();
		}
		return reachable;
	}

	public Usuario obtieneUsuario() {
		Usuario usuario = (Usuario) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		return usuario;
	}
}