SUMMARY = "InfluxDB 3 Core - time series database"
HOMEPAGE = "https://github.com/influxdata/influxdb3_core"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/influxdata/influxdb3_core.git;protocol=https;branch=main;srcrev=${SRCREV}"
SRCREV = "76d57e5d2c58c6b7a14a13b2a4e23f1ef84bd0a3"

S = "${WORKDIR}/git"

inherit cargo systemd

SYSTEMD_SERVICE:${PN} = "influxdb3.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

# InfluxDB 3 data directory on eMMC
INFLUXDB3_DATA_DIR ?= "/var/lib/influxdb3"

SRC_URI += " \
    file://influxdb3.service \
    file://influxdb3.env \
    "

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${CARGO_TARGET_SUBDIR}/influxdb3 ${D}${bindir}/influxdb3

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/influxdb3.service ${D}${systemd_unitdir}/system/

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/influxdb3.env ${D}${sysconfdir}/default/influxdb3

    install -d ${D}${INFLUXDB3_DATA_DIR}
}

FILES:${PN} = " \
    ${bindir}/influxdb3 \
    ${systemd_unitdir}/system/influxdb3.service \
    ${sysconfdir}/default/influxdb3 \
    ${INFLUXDB3_DATA_DIR} \
    "
