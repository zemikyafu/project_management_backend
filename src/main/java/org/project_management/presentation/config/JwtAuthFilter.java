package org.project_management.presentation.config;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.project_management.application.services.User.UserDetailsServiceImpl;
import org.project_management.domain.entities.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;
import java.util.UUID;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtHelper jwtHelper;
    private final HttpServletRequest request;

    public JwtAuthFilter(UserDetailsServiceImpl userDetailsService, JwtHelper jwtHelper, HttpServletRequest request) {
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
        this.request = request;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtHelper.extractUserEmail(token);
            }

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String companyId= jwtHelper.extractCompanyId(token);

            String workspaceId = request.getHeader("workspace_id");

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;

                if (companyId != null) {
                    userDetails = userDetailsService.loadUserAsOwnerOfCompany(username, UUID.fromString(companyId));
                } else if (workspaceId != null) {
                    userDetails = userDetailsService.loadUserAndAuthByUsername(username, UUID.fromString(workspaceId));
                } else {
                    userDetails = userDetailsService.loadUserByUsername(username);
                }
                if (jwtHelper.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (AccessDeniedException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(e.getMessage());
        }  catch (DataAccessException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Database error: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unexpected error: " + e.getMessage() + "\"}");
        }
    }

    public String getUserEmailFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtHelper.extractUserEmail(token);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

}