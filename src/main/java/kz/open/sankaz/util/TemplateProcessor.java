package kz.open.sankaz.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class TemplateProcessor {
    public static String processTemplate(String messageTemplate, Map<String, Object> params) {
        log.info("Start of processing Template...");
        // some logic
        log.info("End of processing Template...");
        return null; // TODO: replace through REGEX
    }
}
