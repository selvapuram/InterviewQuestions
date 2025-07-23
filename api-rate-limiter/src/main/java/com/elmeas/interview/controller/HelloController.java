package com.elmeas.interview.controller;

import com.elmeas.interview.model.ResponseToHello;
import com.elmeas.interview.service.HelloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  private final HelloService helloService;

  public HelloController(HelloService helloService) {this.helloService = helloService;}

  @GetMapping("/hello")
  public ResponseEntity<ResponseToHello> sayHello(@RequestParam("userId") String userId) {
    return ResponseEntity.ok(helloService.sayHello(userId));
  }
}
