package com.example.gateway.filters;


import com.example.gateway.Config;
import com.example.gateway.SpringContext;
import com.example.gateway.loadbalncer.LoadBalancer;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;


import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class RouteFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        // https://github.com/Netflix/ribbon
        System.out.println("====Inside Route Filter====");

        ApplicationContext context = SpringContext.getApplicationContext();
        Config config = context.getAutowireCapableBeanFactory().createBean(Config.class);

        System.out.println(config.getUrl());
        String url = config.getUrl().get(0);


        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        RestTemplate restTemplate = new RestTemplate();


        System.out.println("requestURL :" + request.getRequestURL());
        System.out.println("requestURI :" + request.getRequestURI());
        System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());

        //서버리스트 얻는 부분
        ServerList<DiscoveryEnabledServer> list1 = new DiscoveryEnabledNIWSServerList("sampleservice1.mydomain.net");
        ServerList<DiscoveryEnabledServer> list2 = new DiscoveryEnabledNIWSServerList("sampleservice2.mydomain.net");

        List<DiscoveryEnabledServer> service1_List = list1.getInitialListOfServers();
        List<DiscoveryEnabledServer> service2_List = list2.getInitialListOfServers();

//ㅡㅡㅡㅡㅡㅡ  yml파일로 할때ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        List<String> service1_ListString = Arrays.asList(url.split("-"));
        List<Server> service1_List_Y = new ArrayList<>();

            //loadBalancing 하는 부분

            BaseLoadBalancer lb = new BaseLoadBalancer();
            LoadBalancer  loadBalancer = new LoadBalancer();
            Server server;
             // Eureka server 에서 받아온 list 없을때
            if(service1_List.size()==0){
                System.out.println("===List of server without eureka====");
                for(int i=1; i<service1_ListString.size();i++){
                    System.out.println(service1_ListString.get(i));
                    service1_List_Y.add(new Server("http:"+service1_ListString.get(i).split(":")[1],Integer.parseInt(service1_ListString.get(i).split(":")[2].substring(0,4))));

                }

            //로드밸런서에 서버리스트 넣어줌
                for (int i = 0; i < service1_List_Y.size(); i++) {
                    lb.addServer((Server) service1_List_Y.get(i));
                }

            }else {
                loadBalancer.GetServerList(service1_List, lb);
            }

            System.out.println("====Random 방식====");
            server = loadBalancer.Choose_Random(lb);
            System.out.println(server);

            System.out.println(loadBalancer.GetURL(server)+request.getRequestURI());

            System.out.println("====출력 화면 메세지====");
            System.out.println(restTemplate.getForObject(loadBalancer.GetURL(server)+request.getRequestURI(), String.class));


        return null;
    }
}
