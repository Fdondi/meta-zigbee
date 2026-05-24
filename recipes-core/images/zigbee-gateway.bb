SUMMARY = "Custom Yocto image for Zigbee2MQTT gateway"
DESCRIPTION = "Includes Zigbee2MQTT, Mosquitto, Node.js, and dependencies for a Zigbee gateway."

# Inherit from core-image to get a minimal root filesystem
inherit core-image

# Install additional packages
IMAGE_INSTALL:append = " \
    zigbee2mqtt \
    mosquitto \
    nodejs \
    python3 \
    "
