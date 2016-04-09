package com.cloudmeterclient.controller;

import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudmeterclient.vo.Greeting;

@RestController
public class CloudMeterClientController {
	
	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping("/runjmeter")
    public String startJmeter() throws Exception {
        StandardJMeterEngine jmeter = new StandardJMeterEngine();
     // Initialize Properties, logging, locale, etc.
        JMeterUtils.loadJMeterProperties("/path/to/your/jmeter/bin/jmeter.properties");
        JMeterUtils.setJMeterHome("/path/to/your/jmeter");
        JMeterUtils.initLogging();// you can comment this line out to see extra log messages of i.e. DEBUG level
        JMeterUtils.initLocale();

        // Initialize JMeter SaveService
        SaveService.loadProperties();

        // Load existing .jmx Test Plan
        FileInputStream in = new FileInputStream("/path/to/your/jmeter/extras/Test.jmx");
        HashTree testPlanTree = SaveService.loadTree(in);
        in.close();

        // Run JMeter Test
        jmeter.configure(testPlanTree);
        jmeter.run();
        return "success";
    }
    
}
