package com.exercise4;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class MemberController {

    @Bean
    public static RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static Member retrieveMember(DTOMember dtoMember){
        try {
            return restTemplate().getForObject(
                    GroupSearcher.getApiUrl() + dtoMember.getId(), Member.class);
        } catch (HttpClientErrorException e){
            return retryRetrieveMember(dtoMember, e);
        }
    }

    private static Member retryRetrieveMember(DTOMember dtoMember, HttpClientErrorException e){
        while(e.getStatusCode().value() == 429){
            try {
                sleep(60000);
                return restTemplate().getForObject(
                        GroupSearcher.getApiUrl() + dtoMember.getId(), Member.class);
            }
            catch (HttpClientErrorException e2) { e = e2;}
        }
        System.exit(1);
        return null;
    }

    private static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
