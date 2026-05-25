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

# eMMC layout for real hardware; QEMU just needs ext4
IMAGE_FSTYPES:beaglebone-yocto = "ext4 wic wic.bmap"
WKS_FILE:beaglebone-yocto = "beaglebone-emmc.wks"
