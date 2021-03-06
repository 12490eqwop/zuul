package com.example.gateway.loadbalncer;


import com.example.gateway.Config;
import com.netflix.loadbalancer.*;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class LoadBalancer {

    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    RestTemplate restTemplate = new RestTemplate();

    public BaseLoadBalancer GetServerList(List<DiscoveryEnabledServer> serverList, BaseLoadBalancer lb){


        for (int i = 0; i < serverList.size(); i++) {
            lb.addServer((Server) serverList.get(i));
        }

        return lb;
    }

    public Server Choose_RoundRobin(BaseLoadBalancer loadBalancer){

        IRule roundRobinRule = new RoundRobinRule();
        loadBalancer.setRule(roundRobinRule);
        Server server = loadBalancer.chooseServer();
        return server;
    }

    public Server Choose_WeightedResponseTime(BaseLoadBalancer loadBalancer){

        IRule weightedResponseTimeRule = new WeightedResponseTimeRule();
        loadBalancer.setRule(weightedResponseTimeRule);
        Server server = loadBalancer.chooseServer();
        return server;
    }

    public Server Choose_Random(BaseLoadBalancer loadBalancer){

        IRule randomRule = new RandomRule();
        loadBalancer.setRule(randomRule);
        Server server = loadBalancer.chooseServer();
        return server;
    }


    public String GetURL(Server server){
        server.getPort();
        request.getRequestURI();
        String uri;


        return server.getHost()+":"+server.getPort();
    }

}
