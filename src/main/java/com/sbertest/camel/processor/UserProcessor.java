package com.sbertest.camel.processor;

import com.sbertest.camel.configuration.UserRole;
import com.sbertest.camel.vm.UserVM;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProcessor implements Processor {

    @Value("${app.role-filtering.level}")
    private UserRole roleToFilter;

    @Override
    public void process(Exchange exchange) throws Exception {
        List<UserVM> users = exchange.getIn().getBody(List.class);
        List<UserVM> filteredUsers = users.stream()
                .filter(user -> roleToFilter.name().equalsIgnoreCase(user.getRole().name()))
                .toList();

        exchange.getIn().setBody(filteredUsers);
    }

}
