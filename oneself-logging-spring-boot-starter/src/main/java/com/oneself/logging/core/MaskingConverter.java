package com.oneself.logging.core;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.helpers.MessageFormatter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

/**
 * 日志脱敏与加密转换器。
 */
public class MaskingConverter extends CompositeConverter<ILoggingEvent> {

    private static final Pattern[] KEY_VALUE_PATTERNS = new Pattern[] {
            Pattern.compile("(?i)(password|passwd|pwd)\\s*[:=]\\s*([^\\s,;]+)"),
            Pattern.compile("(?i)(account|username|user)\\s*[:=]\\s*([^\\s,;]+)")
    };

    private static final Pattern[] VALUE_PATTERNS = new Pattern[] {
            Pattern.compile("\\b\\d{17}[0-9Xx]\\b"),
            Pattern.compile("\\b1[3-9]\\d{9}\\b")
    };

    private volatile String cachedPatternText;
    private volatile List<Pattern> cachedCustomPatterns = new ArrayList<>();
    private volatile String cachedMaskFieldsText;
    private volatile List<String> cachedMaskFields = new ArrayList<>();

    @Override
    protected String transform(ILoggingEvent event, String in) {
        String message;
        if (event == null) {
            message = in;
        } else if (in == null || in.equals(event.getFormattedMessage())) {
            message = buildMaskedMessage(event);
        } else {
            message = in;
        }
        if (message == null) {
            return null;
        }
        List<FieldRule> rules = event == null ? Collections.emptyList() : collectRules(event.getArgumentArray());
        List<String> fixedFields = getMaskFields();
        String defaultKey = resolveDefaultKey();
        return maskRegex(maskJsonString(message, rules, fixedFields, defaultKey));
    }

    private String buildMaskedMessage(ILoggingEvent event) {
        Object[] args = event.getArgumentArray();
        if (args == null || args.length == 0) {
            return event.getFormattedMessage();
        }
        String defaultKey = resolveDefaultKey();
        Object[] sanitizedArgs = new Object[args.length];
        List<FieldRule> rules = collectRules(args);
        List<String> fixedFields = getMaskFields();
        for (int i = 0; i < args.length; i++) {
            sanitizedArgs[i] = sanitizeArgument(args[i], defaultKey, rules, fixedFields);
        }
        return MessageFormatter.arrayFormat(event.getMessage(), sanitizedArgs).getMessage();
    }

    private String resolveDefaultKey() {
        if (getContext() == null) {
            return null;
        }
        return getContext().getProperty("LOG_ENCRYPT_KEY");
    }

    private Object sanitizeArgument(Object arg, String defaultKey, List<FieldRule> rules, List<String> fixedFields) {
        if (arg == null) {
            return null;
        }
        if (arg instanceof CharSequence) {
            return maskJsonString(arg.toString(), rules, fixedFields, defaultKey);
        }
        if (arg instanceof Number || arg instanceof Boolean || arg instanceof Enum) {
            return arg;
        }
        if (arg instanceof Map<?, ?>) {
            return maskMap((Map<?, ?>) arg, rules, fixedFields, defaultKey);
        }
        List<Field> fields = collectFields(arg.getClass());
        boolean hasAnnotated = false;
        StringBuilder builder = new StringBuilder();
        builder.append(arg.getClass().getSimpleName()).append("{");
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            field.setAccessible(true);
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(field.getName()).append("=");
            try {
                Object value = field.get(arg);
                LogEncrypt encrypt = field.getAnnotation(LogEncrypt.class);
                if (encrypt != null) {
                    hasAnnotated = true;
                    builder.append(applyEncrypt(encrypt, value, defaultKey));
                } else {
                    builder.append(value);
                }
            } catch (IllegalAccessException ex) {
                builder.append("<error>");
            }
        }
        builder.append("}");
        return hasAnnotated ? builder.toString() : arg;
    }

    private Map<String, Object> maskMap(Map<?, ?> map, List<FieldRule> rules, List<String> fixedFields,
                                        String defaultKey) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey() == null ? "null" : entry.getKey().toString();
            Object value = entry.getValue();
            FieldRule rule = findRule(rules, key);
            if (rule != null) {
                result.put(key, applyEncrypt(rule.toEncrypt(), value, defaultKey));
            } else if (fixedFields.contains(key)) {
                result.put(key, applyConfiguredEncrypt(value, defaultKey));
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    private String maskJsonString(String text, List<FieldRule> rules, List<String> fixedFields, String defaultKey) {
        if (text == null || text.isBlank()) {
            return text;
        }
        if (!text.contains("{") || !text.contains("}")) {
            return text;
        }
        String masked = text;
        if (!rules.isEmpty()) {
        for (FieldRule rule : rules) {
            String name = rule.name();
            if (name == null || name.isBlank()) {
                continue;
            }
            Pattern pattern = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:\\s*\"(.*?)\"");
            Matcher matcher = pattern.matcher(masked);
            StringBuilder buffer = new StringBuilder();
            while (matcher.find()) {
                String rawValue = matcher.group(1);
                String replacement = applyEncrypt(rule.toEncrypt(), rawValue, defaultKey);
                matcher.appendReplacement(buffer, "\"" + name + "\":\"" + Matcher.quoteReplacement(replacement) + "\"");
            }
            matcher.appendTail(buffer);
            masked = buffer.toString();
        }
        }
        for (String name : fixedFields) {
            if (name == null || name.isBlank()) {
                continue;
            }
            Pattern pattern = Pattern.compile("\"" + Pattern.quote(name) + "\"\\s*:\\s*\"(.*?)\"");
            Matcher matcher = pattern.matcher(masked);
            StringBuilder buffer = new StringBuilder();
            while (matcher.find()) {
                String rawValue = matcher.group(1);
                String replacement = applyConfiguredEncrypt(rawValue, defaultKey);
                matcher.appendReplacement(buffer, "\"" + name + "\":\"" + Matcher.quoteReplacement(replacement) + "\"");
            }
            matcher.appendTail(buffer);
            masked = buffer.toString();
        }
        return masked;
    }

    private List<Field> collectFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            Collections.addAll(fields, current.getDeclaredFields());
            current = current.getSuperclass();
        }
        return fields;
    }

    private String applyEncrypt(LogEncrypt encrypt, Object value, String defaultKey) {
        if (value == null) {
            return "null";
        }
        if (encrypt.mode() == LogEncrypt.Mode.MASK) {
            return "***";
        }
        String key = encrypt.key();
        if (key == null || key.isBlank()) {
            key = defaultKey;
        }
        if (key == null || key.isBlank()) {
            return "***";
        }
        return encryptValue(String.valueOf(value), key);
    }

    private FieldRule findRule(List<FieldRule> rules, String name) {
        for (FieldRule rule : rules) {
            if (rule.name().equals(name)) {
                return rule;
            }
        }
        return null;
    }

    private List<FieldRule> collectRules(Object[] args) {
        List<FieldRule> rules = new ArrayList<>();
        if (args == null || args.length == 0) {
            return rules;
        }
        for (Object arg : args) {
            if (arg == null || arg instanceof CharSequence || arg instanceof Number
                    || arg instanceof Boolean || arg instanceof Enum || arg instanceof Map<?, ?>) {
                continue;
            }
            for (Field field : collectFields(arg.getClass())) {
                LogEncrypt encrypt = field.getAnnotation(LogEncrypt.class);
                if (encrypt != null) {
                    String name = encrypt.name();
                    if (name == null || name.isBlank()) {
                        name = field.getName();
                    }
                    rules.add(new FieldRule(name, encrypt));
                }
            }
        }
        return rules;
    }

    private String applyConfiguredEncrypt(Object value, String defaultKey) {
        if (value == null) {
            return "null";
        }
        String key = resolveMaskFieldsKey(defaultKey);
        if (key == null || key.isBlank()) {
            return "***";
        }
        return encryptValue(String.valueOf(value), key);
    }

    private String resolveMaskFieldsKey(String defaultKey) {
        if (getContext() == null) {
            return defaultKey;
        }
        String key = getContext().getProperty("LOG_MASK_FIELDS_KEY");
        if (key == null || key.isBlank()) {
            return defaultKey;
        }
        return key;
    }

    private String encryptValue(String value, String key) {
        try {
            byte[] keyBytes = sha256(key);
            byte[] iv = new byte[12];
            java.security.SecureRandom random = new java.security.SecureRandom();
            random.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(128, iv));
            byte[] cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            byte[] result = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(cipherText, 0, result, iv.length, cipherText.length);
            return "ENC(" + java.util.Base64.getEncoder().encodeToString(result) + ")";
        } catch (Exception ex) {
            return "***";
        }
    }

    private byte[] sha256(String key) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(key.getBytes(StandardCharsets.UTF_8));
    }

    private String maskRegex(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        String masked = message;
        for (Pattern pattern : KEY_VALUE_PATTERNS) {
            masked = pattern.matcher(masked).replaceAll("$1=***");
        }
        for (Pattern pattern : VALUE_PATTERNS) {
            masked = pattern.matcher(masked).replaceAll("***");
        }
        for (Pattern pattern : getCustomPatterns()) {
            masked = pattern.matcher(masked).replaceAll("***");
        }
        return masked;
    }

    private List<Pattern> getCustomPatterns() {
        String patternText = getContext() == null ? null : getContext().getProperty("LOG_MASK_PATTERNS");
        if (patternText == null) {
            patternText = "";
        }
        if (patternText.equals(cachedPatternText)) {
            return cachedCustomPatterns;
        }
        String[] parts = patternText.split(",");
        List<Pattern> patterns = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                patterns.add(Pattern.compile(trimmed));
            }
        }
        cachedPatternText = patternText;
        cachedCustomPatterns = patterns;
        return patterns;
    }

    private List<String> getMaskFields() {
        String fieldsText = getContext() == null ? null : getContext().getProperty("LOG_MASK_FIELDS");
        if (fieldsText == null) {
            fieldsText = "";
        }
        if (fieldsText.equals(cachedMaskFieldsText)) {
            return cachedMaskFields;
        }
        String[] parts = fieldsText.split(",");
        List<String> fields = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                fields.add(trimmed);
            }
        }
        cachedMaskFieldsText = fieldsText;
        cachedMaskFields = fields;
        return fields;
    }

    private record FieldRule(String name, LogEncrypt encrypt) {
        LogEncrypt toEncrypt() {
            return encrypt;
        }
    }
}
