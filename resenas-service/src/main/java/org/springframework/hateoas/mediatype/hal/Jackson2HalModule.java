package org.springframework.hateoas.mediatype.hal;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Jackson2HalModule extends SimpleModule {
public Jackson2HalModule() { super("Jackson2HalModule"); }
public static boolean isAlreadyRegisteredIn(ObjectMapper mapper) { return true; }
}
