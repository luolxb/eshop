package com.common.config;

import com.common.properties.SoubaoProperties;
import com.common.service.SoubaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SoubaoProperties.class)
@ConditionalOnProperty(
        prefix = "soubao",
        name = "isopen",
        havingValue = "true"
)
public class SoubaoConfig {
    @Autowired
    private SoubaoProperties soubaoProperties;

    @Bean(name = "soubao")
    public SoubaoService demoService(){
        return new SoubaoService(soubaoProperties.getSayWhat(), soubaoProperties.getToWho());
    }
}
