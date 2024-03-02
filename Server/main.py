from solace.messaging.config.solace_properties import transport_layer_properties, service_properties, authentication_properties
import os
from dotenv import load_dotenv

load_dotenv()

SOLACE_HOST = os.getenv("SOLACE_HOST")
SOLACE_VPN = os.getenv("SOLACE_VPN")
SOLACE_USERNAME = os.getenv("SOLACE_USERNAME")
SOLACE_PASSWORD = os.getenv("SOLACE_PASSWORD")

broker_props = {
    transport_layer_properties.HOST: SOLACE_HOST,
    service_properties.VPN_NAME: SOLACE_VPN,
    authentication_properties.SCHEME_BASIC_USER_NAME: SOLACE_USERNAME,
    authentication_properties.SCHEME_BASIC_PASSWORD: SOLACE_PASSWORD,
}
