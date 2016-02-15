package com.unknown.pkg;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public class JacksonController {

    @RequestMapping("/jackson")
    public MyCustomObject incoming(@RequestBody MyCustomObject incoming) {
        return incoming;
    }

}
