package lazecoding.keeper.util;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * AccessTokenUtil
 *
 * @author lazecoding
 */
public class AccessTokenUtil {

    /**
     * 构造私有
     */
    private AccessTokenUtil() {
    }

    /**
     * 获取 access-token
     */
    public static String getAccessToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(requestAttributes)) {
            return null;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        if (ObjectUtils.isEmpty(request)) {
            return null;
        }
        return getAccessToken(request);
    }

    /**
     * 获取 access-token
     */
    public static String getAccessToken(HttpServletRequest request) {
        if (ObjectUtils.isEmpty(request)) {
            return null;
        }
        String accessToken = getCookieValue(request, "access-token");
        if (!StringUtils.hasText(accessToken)) {
            accessToken = request.getHeader("access-token");
        }
        return accessToken;
    }

    /**
     * 获取 Cookie 中指定属性的值
     *
     * @param request HttpServletRequest
     * @param name    key
     */
    private static String getCookieValue(HttpServletRequest request, String name) {
        if (ObjectUtils.isEmpty(request)) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
