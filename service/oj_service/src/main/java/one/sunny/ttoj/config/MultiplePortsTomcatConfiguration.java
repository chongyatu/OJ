//package one.sunny.ttoj.config;
//
//import org.apache.catalina.connector.Connector;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MultiplePortsTomcatConfiguration {
//
//    @Bean
//    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> multipleConnectors() {
//        return factory -> {
//            // 额外的端口，可以从配置文件中加载
//            factory.addAdditionalTomcatConnectors(createConnector(9999));
//        };
//    }
//
//    private Connector createConnector(int port) {
//        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//        connector.setPort(port);
//        return connector;
//    }
//}