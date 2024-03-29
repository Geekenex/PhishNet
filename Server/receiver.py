from solace.messaging.config.solace_properties import transport_layer_properties, service_properties, authentication_properties
from solace.messaging.config.authentication_strategy import BasicUserNamePassword
from solace.messaging.messaging_service import MessagingService, RetryStrategy
from solace.messaging.config.transport_security_strategy import TLS
from solace.messaging.receiver.inbound_message import InboundMessage
from solace.messaging.resources.queue import Queue
import sender
import json
import os
from dotenv import load_dotenv
import ScamScript

load_dotenv()

SOLACE_HOST = os.getenv("SOLACE_HOST")
SOLACE_VPN = os.getenv("SOLACE_VPN")
SOLACE_USERNAME = os.getenv("SOLACE_USERNAME")
SOLACE_PASSWORD = os.getenv("SOLACE_PASSWORD")
SOLACE_RECEIVING_QUEUE_NAME = os.getenv("SOLACE_RECEIVING_QUEUE_NAME")

broker_props = {
    transport_layer_properties.HOST: SOLACE_HOST,
    service_properties.VPN_NAME: SOLACE_VPN,
    authentication_properties.SCHEME_BASIC_USER_NAME: SOLACE_USERNAME,
    authentication_properties.SCHEME_BASIC_PASSWORD: SOLACE_PASSWORD,
}

transport_security = TLS.create().without_certificate_validation()

# Build A messaging service with a reconnection strategy of 20 retries over an interval of 3 seconds
messaging_service = MessagingService.builder().from_properties(broker_props)\
    .with_reconnection_retry_strategy(RetryStrategy.parametrized_retry(20, 3)) \
    .with_authentication_strategy(BasicUserNamePassword.of(SOLACE_USERNAME, SOLACE_PASSWORD)) \
    .with_transport_security_strategy(transport_security) \
    .build()

# Blocking connect thread
messaging_service.connect()	

# Creating the PersistentMessageReceiver
# Used to recieve the messageID and messages from the broker
 
durable_exclusive_queue = Queue.durable_exclusive_queue(SOLACE_RECEIVING_QUEUE_NAME)			


persistent_receiver= messaging_service.create_persistent_message_receiver_builder() \
                .build(durable_exclusive_queue)
persistent_receiver.start()		

def messageProcessingLoop():
    while True:
        messageRaw = persistent_receiver.receive_message(1000)
        if messageRaw is None:
            continue
        messageBody = messageRaw.get_payload_as_bytes().decode('utf-8').replace("\n", "")
        message = json.loads(messageBody)
        print("(IN) RECEIVED MSG: " + str(message) + "\n")
        verdict = ScamScript.combinedcheck(message["message"])
        sender.sendMessageVerdict(message["messageId"], message["conversationId"], verdict)
        persistent_receiver.ack(messageRaw)