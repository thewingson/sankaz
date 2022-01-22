package kz.open.sankaz.exception;

import java.util.Map;

public class EntityNotFoundException extends RuntimeException {
    private static final String ENTITY_NOT_FOUND = "Entity not found!";

    public EntityNotFoundException(Class clazz, Map<String, Object> params) {
        super(ENTITY_NOT_FOUND + " Entity: " + clazz + ". Params: " + params.toString());
    }

    public EntityNotFoundException(Class clazz, Map<String, Object> params, Throwable err) {
        super(ENTITY_NOT_FOUND + " Entity: " + clazz + ". Params: " + params.toString(), err);
    }
}
