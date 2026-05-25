SUMMARY = "Full Zigbee gateway image with onboard monitoring stack"
DESCRIPTION = "Zigbee2MQTT + Mosquitto + Telegraf + InfluxDB 3 + Grafana, targeting BeagleBone Black eMMC"

inherit core-image

IMAGE_INSTALL:append = " \
    zigbee2mqtt \
    mosquitto \
    nodejs \
    python3 \
    telegraf \
    influxdb \
    grafana \
    "

# eMMC-friendly: ext4 rootfs + WIC with emmc partition layout
IMAGE_FSTYPES = "ext4 wic wic.bmap"
WKS_FILE = "beaglebone-emmc.wks"
