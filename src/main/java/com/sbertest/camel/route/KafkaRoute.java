package com.sbertest.camel.route;

import com.sbertest.camel.processor.UserProcessor;
import com.sbertest.camel.vm.UserVM;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaRoute extends RouteBuilder {

    private final UserProcessor userProcessor;

    //@formatter:off
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .contextPath("/api")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .port("8080");

        onException(org.apache.camel.ValidationException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setBody().simple("{\"error\": \"Invalid input JSON\"}");

        rest("/cameltest")
                .consumes("application/json").produces("application/json")
                .post("/fakelogin")
                    .id("fakeLogin")
                    .type(UserVM[].class)
                    .outType(UserVM[].class)
                    .to("direct:processUser");



        from("direct:processUser")
                .log("Received: ${body}")
                    .marshal().json(JsonLibrary.Jackson)
                    .to("json-validator:classpath:schemas/userSchema.json")
                    .unmarshal(new ListJacksonDataFormat(UserVM.class))
//                .filter().method(UserProcessor.class, "filterByRole")
                .process(userProcessor)
                    .marshal().json(JsonLibrary.Jackson)
                    .log("Forwarding valid body to Kafka: ${body}")
                .to("kafka:fakelogin?brokers=localhost:9092")
                .unmarshal(new ListJacksonDataFormat(UserVM.class)) // это сделал чтобы в постмане возвращался обработанный JSON, а не 500
                .log("body after all steps: ${body}");
    }
    //@formatter:on


}
