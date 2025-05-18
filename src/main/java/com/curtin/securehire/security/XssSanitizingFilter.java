package com.curtin.securehire.security;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class XssSanitizingFilter implements Filter {

    private static final PolicyFactory POLICY = Sanitizers.FORMATTING
            .and(Sanitizers.LINKS)
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.TABLES)
            .and(Sanitizers.IMAGES);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            if (!"GET".equalsIgnoreCase(httpRequest.getMethod())) {
                Map<String, String[]> parameterMap = httpRequest.getParameterMap();
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String[] values = entry.getValue();
                    for (int i = 0; i < values.length; i++) {
                        values[i] = sanitize(values[i]);
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }

    private String sanitize(String input) {
        // Sanitize against XSS
        String sanitizedInput = POLICY.sanitize(input);

        // Basic SQL Injection prevention
        sanitizedInput = sanitizedInput.replaceAll("(['\";])+|(--)+", "");

        // Additional sanitization for other injections
        sanitizedInput = sanitizedInput.replaceAll("<", "<").replaceAll(">", ">"); // HTML tags
        sanitizedInput = sanitizedInput.replaceAll("\\(", "(").replaceAll("\\)", ")"); // Parentheses
        sanitizedInput = sanitizedInput.replaceAll("eval\\((.*)\\)", ""); // eval() function
        sanitizedInput = sanitizedInput.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\""); // javascript:
        sanitizedInput = sanitizedInput.replaceAll("script", ""); // script tags

        // Bitwise operators and other common injection patterns
        sanitizedInput = sanitizedInput.replaceAll("&", "&"); // Ampersand
        sanitizedInput = sanitizedInput.replaceAll("\\|", "|"); // Pipe
        sanitizedInput = sanitizedInput.replaceAll("\\^", "^"); // Caret
        sanitizedInput = sanitizedInput.replaceAll("~", "~"); // Tilde
        sanitizedInput = sanitizedInput.replaceAll("<<", "<<"); // Left shift
        sanitizedInput = sanitizedInput.replaceAll(">>", ">>"); // Right shift

        return sanitizedInput;
    }
}
