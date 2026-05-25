SUMMARY = "Plugin-driven server agent for collecting and reporting metrics"
HOMEPAGE = "https://www.influxdata.com/time-series-platform/telegraf/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c1a1f346b9e30f1b6c5905f459c0bf77"

# Pre-built ARMv7 hard-float binary — building from source hits duplicate symbol
# linker errors with Go 1.22 generics (antlr4-go vs cel-go/parser conflict).
SRC_URI = "https://dl.influxdata.com/telegraf/releases/telegraf-${PV}_linux_armhf.tar.gz \
           file://LICENSE \
           file://telegraf.conf \
           file://telegraf.service \
           "
SRC_URI[sha256sum] = "8e3a53fc77b134eec23ba6fcef0f138051d2f9c65f637f9708e653d2d1b3980b"

S = "${WORKDIR}"

inherit systemd

INSANE_SKIP:${PN} = "already-stripped arch"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

SYSTEMD_SERVICE:${PN} = "telegraf.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/telegraf-${PV}/usr/bin/telegraf ${D}${bindir}/telegraf

    install -d ${D}${sysconfdir}/telegraf
    install -m 0644 ${WORKDIR}/telegraf.conf ${D}${sysconfdir}/telegraf/telegraf.conf

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/telegraf.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} += " \
    ${sysconfdir}/telegraf \
    ${systemd_unitdir}/system/telegraf.service \
    "
