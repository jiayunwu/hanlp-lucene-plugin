package com.hankcs.lucene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by zf on 2017/8/31 0031.
 */
public class AbstractAnalysisFactory {

    public static final String LUCENE_MATCH_VERSION_PARAM = "luceneMatchVersion";
    private boolean isExplicitLuceneMatchVersion = false;
    private static final Pattern ITEM_PATTERN = Pattern.compile("[^,\\s]+");
    private static final String CLASS_NAME = "class";


    public String require(Map<String, String> args, String name) {
        String s = (String) args.remove(name);
        if (s == null) {
            throw new IllegalArgumentException("Configuration Error: missing parameter '" + name + "'");
        } else {
            return s;
        }
    }

    public String require(Map<String, String> args, String name, Collection<String> allowedValues) {
        return this.require(args, name, allowedValues, true);
    }

    public String require(Map<String, String> args, String name, Collection<String> allowedValues,
            boolean caseSensitive) {
        String s = (String) args.remove(name);
        if (s == null) {
            throw new IllegalArgumentException("Configuration Error: missing parameter '" + name + "'");
        } else {
            Iterator i$ = allowedValues.iterator();

            while (i$.hasNext()) {
                String allowedValue = (String) i$.next();
                if (caseSensitive) {
                    if (s.equals(allowedValue)) {
                        return s;
                    }
                } else if (s.equalsIgnoreCase(allowedValue)) {
                    return s;
                }
            }

            throw new IllegalArgumentException(
                    "Configuration Error: '" + name + "' value must be one of " + allowedValues);
        }
    }

    public String get(Map<String, String> args, String name) {
        return (String) args.remove(name);
    }

    public String get(Map<String, String> args, String name, String defaultVal) {
        String s = (String) args.remove(name);
        return s == null ? defaultVal : s;
    }

    public String get(Map<String, String> args, String name, Collection<String> allowedValues) {
        return this.get(args, name, allowedValues, (String) null);
    }

    public String get(Map<String, String> args, String name, Collection<String> allowedValues, String defaultVal) {
        return this.get(args, name, allowedValues, defaultVal, true);
    }

    public String get(Map<String, String> args, String name, Collection<String> allowedValues, String defaultVal,
            boolean caseSensitive) {
        String s = (String) args.remove(name);
        if (s == null) {
            return defaultVal;
        } else {
            Iterator i$ = allowedValues.iterator();

            while (i$.hasNext()) {
                String allowedValue = (String) i$.next();
                if (caseSensitive) {
                    if (s.equals(allowedValue)) {
                        return s;
                    }
                } else if (s.equalsIgnoreCase(allowedValue)) {
                    return s;
                }
            }

            throw new IllegalArgumentException(
                    "Configuration Error: '" + name + "' value must be one of " + allowedValues);
        }
    }

    protected final int requireInt(Map<String, String> args, String name) {
        return Integer.parseInt(this.require(args, name));
    }

    protected final int getInt(Map<String, String> args, String name, int defaultVal) {
        String s = (String) args.remove(name);
        return s == null ? defaultVal : Integer.parseInt(s);
    }

    protected final boolean requireBoolean(Map<String, String> args, String name) {
        return Boolean.parseBoolean(this.require(args, name));
    }

    protected final boolean getBoolean(Map<String, String> args, String name, boolean defaultVal) {
        String s = (String) args.remove(name);
        return s == null ? defaultVal : Boolean.parseBoolean(s);
    }

    protected final float requireFloat(Map<String, String> args, String name) {
        return Float.parseFloat(this.require(args, name));
    }

    protected final float getFloat(Map<String, String> args, String name, float defaultVal) {
        String s = (String) args.remove(name);
        return s == null ? defaultVal : Float.parseFloat(s);
    }

    public char requireChar(Map<String, String> args, String name) {
        return this.require(args, name).charAt(0);
    }

    public char getChar(Map<String, String> args, String name, char defaultValue) {
        String s = (String) args.remove(name);
        if (s == null) {
            return defaultValue;
        } else if (s.length() != 1) {
            throw new IllegalArgumentException(name + " should be a char. \"" + s + "\" is invalid");
        } else {
            return s.charAt(0);
        }
    }

    public Set<String> getSet(Map<String, String> args, String name) {
        String s = (String) args.remove(name);
        if (s == null) {
            return null;
        } else {
            Set<String> set = null;
            Matcher matcher = ITEM_PATTERN.matcher(s);
            if (matcher.find()) {
                set = new HashSet();
                set.add(matcher.group(0));

                while (matcher.find()) {
                    set.add(matcher.group(0));
                }
            }

            return set;
        }
    }

    protected final Pattern getPattern(Map<String, String> args, String name) {
        try {
            return Pattern.compile(this.require(args, name));
        } catch (PatternSyntaxException var4) {
            throw new IllegalArgumentException(
                    "Configuration Error: '" + name + "' can not be parsed in " + this.getClass().getSimpleName(),
                    var4);
        }
    }

    protected final List<String> splitFileNames(String fileNames) {
        if (fileNames == null) {
            return Collections.emptyList();
        } else {
            List<String> result = new ArrayList();
            String[] arr$ = fileNames.split("(?<!\\\\),");
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                String file = arr$[i$];
                result.add(file.replaceAll("\\\\(?=,)", ""));
            }

            return result;
        }
    }


    public boolean isExplicitLuceneMatchVersion() {
        return this.isExplicitLuceneMatchVersion;
    }

    public void setExplicitLuceneMatchVersion(boolean isExplicitLuceneMatchVersion) {
        this.isExplicitLuceneMatchVersion = isExplicitLuceneMatchVersion;
    }
}
