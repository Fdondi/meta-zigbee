SUMMARY = "Zigbee to MQTT bridge"
DESCRIPTION = "Allows you to use Zigbee devices without the vendor's bridge"
HOMEPAGE = "https://www.zigbee2mqtt.io/"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=84dcc94da3adb52b53ae4fa38fe49e5d"

DEPENDS = "nodejs-native nodejs-oe-cache-native"

PV = "2.10.1"
SRCREV = "94751abaacb66989e688f21d8ac3773cf5d754a9"
SRCBRANCH = "master"

SRC_URI = " \
    git://github.com/Koenkk/zigbee2mqtt.git;protocol=https;branch=${SRCBRANCH};srcrev=${SRCREV} \
    file://configuration.yaml \
    file://zigbee2mqtt.service \
    "

S = "${WORKDIR}/git"

inherit npm systemd

SYSTEMD_SERVICE:${PN} = "zigbee2mqtt.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install:append() {
    install -d ${D}${sysconfdir}/zigbee2mqtt
    install -m 0644 ${WORKDIR}/configuration.yaml ${D}${sysconfdir}/zigbee2mqtt/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/zigbee2mqtt.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} += " \
    ${sysconfdir}/zigbee2mqtt \
    ${systemd_unitdir}/system/zigbee2mqtt.service \
    "
