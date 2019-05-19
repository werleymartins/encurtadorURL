package validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLValidator {
    private static final String URL_REGEX = "((((f|ht)tps?:)?//)?([a-zA-Z0-9!#$%&'*+-/=?^_`{|}~]+(:[^ @:]+)?@)?((([a-zA-Z0-9\\-]{1,255}|xn--[a-zA-Z0-9\\-]+)\\.)+(xn--[a-zA-Z0-9\\-]+|[a-zA-Z]{2,6}|\\d{1,3})|localhost|(%[0-9a-fA-F]{2})+|[0-9]+)(:[0-9]{1,5})?([/\\?][^ \\s/]*)*)";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
    
    public boolean urlValidator(String url) {
        if (url == null) {
            return false;
        }
        
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
}