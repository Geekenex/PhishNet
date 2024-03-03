import os
import platform
import time
import threading
import json
from solace.messaging.config.transport_security_strategy import TLS
from solace.messaging.messaging_service import MessagingService, ReconnectionListener, ReconnectionAttemptListener, ServiceInterruptionListener, RetryStrategy, ServiceEvent
from solace.messaging.config.solace_properties import transport_layer_properties, service_properties, authentication_properties
from solace.messaging.publisher.persistent_message_publisher import PersistentMessagePublisher, MessagePublishReceiptListener, PublishReceipt
from solace.messaging.resources.topic import Topic
from dotenv import load_dotenv

if platform.uname().system == 'Windows': 
    os.environ["PYTHONUNBUFFERED"] = "1"  # Disable stdout buffer 

load_dotenv()

SOLACE_HOST = os.getenv("SOLACE_HOST")
SOLACE_VPN = os.getenv("SOLACE_VPN")
SOLACE_USERNAME = os.getenv("SOLACE_USERNAME")
SOLACE_PASSWORD = os.getenv("SOLACE_PASSWORD")
SOLACE_SENDING_QUEUE_NAME = os.getenv("SOLACE_SENDING_QUEUE_NAME")

lock = threading.Lock()  # Lock object for thread synchronization
# Inner classes for error handling
class ServiceEventHandler(ReconnectionListener, ReconnectionAttemptListener, ServiceInterruptionListener):
    def on_reconnected(self, e: ServiceEvent):
        print("\non_reconnected")
        print(f"Error cause: {e.get_cause()}")
        print(f"Message: {e.get_message()}")
    
    def on_reconnecting(self, e: "ServiceEvent"):
        print("\non_reconnecting")
        print(f"Error cause: {e.get_cause()}")
        print(f"Message: {e.get_message()}")

    def on_service_interrupted(self, e: "ServiceEvent"):
        print("\non_service_interrupted")
        print(f"Error cause: {e.get_cause()}")
        print(f"Message: {e.get_message()}")

class MessageReceiptListener(MessagePublishReceiptListener):
    def __init__(self):
        self._receipt_count = 0

    @property
    def receipt_count(self):
        return self._receipt_count

    def on_publish_receipt(self, publish_receipt: 'PublishReceipt'):
        with lock:
            self._receipt_count += 1

broker_props = {
    transport_layer_properties.HOST: SOLACE_HOST,
    service_properties.VPN_NAME: SOLACE_VPN,
    authentication_properties.SCHEME_BASIC_USER_NAME: SOLACE_USERNAME,
    authentication_properties.SCHEME_BASIC_PASSWORD: SOLACE_PASSWORD,
}

transport_security = TLS.create().without_certificate_validation()

# Build A messaging service with a reconnection strategy of 20 retries over an interval of 3 seconds
messaging_service = MessagingService.builder().from_properties(broker_props)\
                    .with_reconnection_retry_strategy(RetryStrategy.parametrized_retry(20,3))\
                    .with_transport_security_strategy(transport_security)\
                    .build()

# Blocking connect thread
messaging_service.connect()
print(f'Messaging Service connected? {messaging_service.is_connected}')

# Event Handling for the messaging service
service_handler = ServiceEventHandler()
messaging_service.add_reconnection_listener(service_handler)
messaging_service.add_reconnection_attempt_listener(service_handler)
messaging_service.add_service_interruption_listener(service_handler)

# Create a persistent message publisher and start it
publisher: PersistentMessagePublisher = messaging_service.create_persistent_message_publisher_builder().build()
publisher.start()

# Set a message delivery listener to the publisher
receipt_listener = MessageReceiptListener()
publisher.set_message_publish_receipt_listener(receipt_listener)

# Prepare the destination topic
topic = Topic.of(SOLACE_SENDING_QUEUE_NAME)

def sendMessageVerdict(messageId: str, conversationID: str, verdict: int): 

    message_content = {
        "messageId":  messageId,
        "conversationId": conversationID,
        "verdict":  verdict
    }
    
    message_body = json.dumps(message_content)

    print(f"(OUT) SENDING MSG: {message_body} \n")
    
    outbound_msg = messaging_service.message_builder() \
            .with_application_message_type("text") \
            .build(message_body)
    
    publisher.publish(outbound_msg, topic)