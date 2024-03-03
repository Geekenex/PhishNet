/**package com.example.phishnet;

import com.solace.messaging.MessagingService;
import com.solace.messaging.config.profile.ConfigurationProfile;
import com.solace.messaging.publisher.OutboundMessageBuilder;
import com.solace.messaging.publisher.PersistentMessagePublisher;
import com.solace.messaging.resources.Topic;
import com.solace.messaging.config.SolaceProperties.TransportLayerProperties;
import com.solace.messaging.config.*;

import java.util.UUID;

public class Sender {

    public static void sendMessageAsync(String message, int conversationId, UUID messageId) {
        new Thread(() -> send(message, conversationId, messageId)).start();
    }

    public static void send(String message, int conversationId, UUID messageId) {
        // Configure the messaging service with the host details and authentication
        String myKeystorePassword = "";
        MessagingService messagingService = MessagingService.builder(ConfigurationProfile.V1)
                .withTransportSecurityStrategy(TransportSecurityStrategy.TLS.create().withoutCertificateValidation())
                .withAuthenticationStrategy(                 // Enables/overrides the existing authentication strategy with AuthenticationStrategy.
                AuthenticationStrategy.ClientCertificateAuthentication          // Configures transport layer security (TLS) not to validate server certificates.
                .of("https://cacerts.digicert.com/DigiCertGlobalRootCA.crt.pem", "changeme"))

                .fromProperties(new java.util.Properties() {{
                    setProperty("solace.messaging.transport.host", SolaceCredentials.SOLACE_HOST);
                    setProperty("solace.messaging.service.vpn-name", SolaceCredentials.SOLACE_VPN);
                    setProperty("solace.messaging.authentication.basic.username", SolaceCredentials.SOLACE_USERNAME);
                    setProperty("solace.messaging.authentication.basic.password", SolaceCredentials.SOLACE_PASSWORD);
                }})
                .build();

        // Connect to the messaging service
        messagingService.connect();

        // Create a persistent message publisher
        final PersistentMessagePublisher publisher = messagingService.createPersistentMessagePublisherBuilder()
                .build().start();

        // Create a topic to publish to
        final Topic topic = Topic.of("Messages");

        // Build a message
        final OutboundMessageBuilder messageBuilder = messagingService.messageBuilder();
        final byte[] messagePayload = String.format("{\"message\":\"{}\",\"messageId\":\"{}\",\"conversationId\":\"{}\"}", message, messageId, conversationId).getBytes();
        final com.solace.messaging.publisher.OutboundMessage outboundMessage = messageBuilder.build(messagePayload);

        // Publish the message as persistent
        publisher.publish(outboundMessage, topic);

        System.out.println("Message published.");

        // Cleanup - gracefully shutdown the messaging service
        messagingService.disconnect();
    }
}**/