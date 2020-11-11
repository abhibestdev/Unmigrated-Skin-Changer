package me.abhi.unmigrated.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StringUtil {

    public static String join(List<String> pieces, String separator) {
        StringBuilder buffer = new StringBuilder();
        Iterator iter = pieces.iterator();

        while(iter.hasNext()) {
            buffer.append((String)iter.next());
            if (iter.hasNext()) {
                buffer.append(separator);
            }
        }

        return buffer.toString();
    }

    public static String buildString(String[] args, int start) {
        return String.join(" ", Arrays.copyOfRange(args, start, args.length));
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    public static String replace(String message, String... replacements) {
        String toSend = message;
        String key = null;
        for (int i = 0; i < replacements.length; i++) {
            if ((i + 1) % 2 != 0) {
                key = replacements[i];
            } else {
                toSend = toSend.replace(key, replacements[i]);
            }
        }
        return toSend;
    }

    public static List<String> getList(final String string) {
        final List<String> list = new ArrayList<String>();
        final String[] split;
        final String[] items = split = string.split(":");
        for (final String item : split) {
            list.add(item);
        }
        return list;
    }

    public static String listToString(final List<String> list) {
        String output = "";
        for (int i = 0; i < list.size(); ++i) {
            if (i + 1 < list.size()) {
                output = output + list.get(i) + ":";
            }
            else {
                output += list.get(i);
            }
        }
        return output;
    }

    public static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
