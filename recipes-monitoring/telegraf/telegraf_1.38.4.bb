SUMMARY = "Plugin-driven server agent for collecting and reporting metrics"
HOMEPAGE = "https://www.influxdata.com/time-series-platform/telegraf/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=fe53cff8eef1afa881ea0e6325071ecd"

GO_IMPORT = "github.com/influxdata/telegraf"
GO_INSTALL = "${GO_IMPORT}/cmd/telegraf"

SRC_URI = "git://${GO_IMPORT}.git;protocol=https;branch=master;destsuffix=${BPN}-${PV}/src/${GO_IMPORT} \
           file://telegraf.conf \
           file://telegraf.service \
           "
SRCREV = "c79b06d58e912124624d029a88bbe182254f0ff4"

inherit go-mod systemd

export GOPROXY = "https://proxy.golang.org,direct"
do_compile[network] = "1"

SYSTEMD_SERVICE:${PN} = "telegraf.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install:append() {
    install -d ${D}${sysconfdir}/telegraf
    install -m 0644 ${WORKDIR}/telegraf.conf ${D}${sysconfdir}/telegraf/telegraf.conf

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/telegraf.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} += " \
    ${sysconfdir}/telegraf \
    ${systemd_unitdir}/system/telegraf.service \
    "
