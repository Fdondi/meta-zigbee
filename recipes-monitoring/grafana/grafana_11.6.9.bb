SUMMARY = "Open source analytics and monitoring platform"
HOMEPAGE = "https://grafana.com"
LICENSE = "AGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb1e647870add0502f8f010b19de32af"

# Use the official pre-built ARMv7 binary — building Grafana from source
# requires a full Node.js/Webpack frontend build which is not practical in Yocto.
SRC_URI = "https://dl.grafana.com/oss/release/grafana-${PV}.linux-armv7.tar.gz \
           file://grafana.service \
           file://grafana.ini \
           "
SRC_URI[sha256sum] = "82161b4e8b83ac3c1fe8aab3a6711630a44e5ce237cc02dd4aacf7387a3384a5"

S = "${WORKDIR}/grafana-${PV}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "grafana.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

GRAFANA_DATA_DIR ?= "/var/lib/grafana"
GRAFANA_LOG_DIR  ?= "/var/log/grafana"

# Pre-built ARM binary — skip QA checks for arch/stripped
INSANE_SKIP:${PN} = "already-stripped arch"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/bin/grafana        ${D}${bindir}/grafana
    install -m 0755 ${S}/bin/grafana-server ${D}${bindir}/grafana-server
    install -m 0755 ${S}/bin/grafana-cli    ${D}${bindir}/grafana-cli

    install -d ${D}${datadir}/grafana
    cp -r ${S}/public ${D}${datadir}/grafana/
    cp -r ${S}/conf   ${D}${datadir}/grafana/

    install -d ${D}${sysconfdir}/grafana
    install -m 0644 ${WORKDIR}/grafana.ini ${D}${sysconfdir}/grafana/grafana.ini

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/grafana.service ${D}${systemd_unitdir}/system/

    install -d ${D}${GRAFANA_DATA_DIR}
    install -d ${D}${GRAFANA_LOG_DIR}
}

FILES:${PN} = " \
    ${bindir}/grafana \
    ${bindir}/grafana-server \
    ${bindir}/grafana-cli \
    ${datadir}/grafana \
    ${sysconfdir}/grafana \
    ${systemd_unitdir}/system/grafana.service \
    ${GRAFANA_DATA_DIR} \
    ${GRAFANA_LOG_DIR} \
    "
