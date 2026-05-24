SUMMARY = "InfluxDB 3 Core - time series database"
HOMEPAGE = "https://github.com/influxdata/influxdb3_core"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

# No release tags yet — pinned to latest main as of 2026-05-25
PV = "3.0+git"
SRC_URI = "git://github.com/influxdata/influxdb3_core.git;protocol=https;branch=main;srcrev=${SRCREV} \
           file://influxdb3.service \
           file://influxdb3.env \
           "
SRCREV = "349d802bf9a54ab5876399ab013502eeb0d52a6b"

S = "${WORKDIR}/git"

inherit cargo systemd

SYSTEMD_SERVICE:${PN} = "influxdb3.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

# InfluxDB 3 data directory on eMMC
INFLUXDB3_DATA_DIR ?= "/var/lib/influxdb3"

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
