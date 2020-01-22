package com.example.gateway.controller;


import com.example.gateway.Config;
import com.netflix.zuul.http.ZuulServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.annotation.WebServlet;

@RequestMapping("/")
@WebServlet
public class Controller extends ZuulServlet {


}
