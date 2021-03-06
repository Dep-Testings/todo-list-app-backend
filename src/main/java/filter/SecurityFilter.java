package filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import util.AppUtil;

import javax.crypto.SecretKey;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : Lucky Prabath <lucky.prabath94@gmail.com>
 * @since : 2021-01-11
 **/
@WebFilter(filterName = "SecurityFilter", servletNames = {"TodoItemServlet", "UserServlet"})
public class SecurityFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req.getMethod().equals("OPTIONS")){
            chain.doFilter(req,res);
        }else if (req.getServletPath().equals("/api/v1/auth") && req.getMethod().equals("POST")) {
            chain.doFilter(req, res);
        }else if(req.getServletPath().equals("/api/v1/users") && req.getMethod().equals("POST")){
            chain.doFilter(req, res);
        } else {
            String authorization = req.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer")) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                String token = authorization.replace("Bearer ", "");
                Jws<Claims> jws;
                try {
                    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(AppUtil.getAppSecretKey()));
                    jws = Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token);
                    req.setAttribute("user", jws.getBody().get("name"));
                    chain.doFilter(req, res);
                } catch (JwtException ex) {
                    ex.printStackTrace();
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }
}

