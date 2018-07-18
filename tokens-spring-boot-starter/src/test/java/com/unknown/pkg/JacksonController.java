package com.unknown.pkg;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JacksonController {

    @RequestMapping(value = "/jackson", method = RequestMethod.POST)
    public MyCustomObject incoming(@RequestBody MyCustomObject incoming) {
        System.out.println(incoming);
        return incoming;
    }

}
