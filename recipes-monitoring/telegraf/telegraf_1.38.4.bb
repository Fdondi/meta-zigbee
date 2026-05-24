SUMMARY = "Plugin-driven server agent for collecting and reporting metrics"
HOMEPAGE = "https://www.influxdata.com/time-series-platform/telegraf/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fa818a259cbed7ce8bc2a22d35a464fc"

SRC_URI = "git://github.com/influxdata/telegraf.git;protocol=https;branch=master;srcrev=${SRCREV} \
           file://telegraf.conf \
           file://telegraf.service \
           "
SRCREV = "c79b06d58e912124624d029a88bbe182254f0ff4"

S = "${WORKDIR}/git"

inherit go-module systemd

GO_IMPORT = "github.com/influxdata/telegraf"

SYSTEMD_SERVICE:${PN} = "telegraf.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_compile() {
    cd ${S}
    export GOARCH="${TARGET_GOARCH}"
    export GOOS="linux"
    export CGO_ENABLED="0"
    go build -o ${B}/telegraf ./cmd/telegraf
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/telegraf ${D}${bindir}/telegraf

    install -d ${D}${sysconfdir}/telegraf
    install -m 0644 ${WORKDIR}/telegraf.conf ${D}${sysconfdir}/telegraf/telegraf.conf

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/telegraf.service ${D}${systemd_unitdir}/system/
}

FILES:${PN} = " \
    ${bindir}/telegraf \
    ${sysconfdir}/telegraf \
    ${systemd_unitdir}/system/telegraf.service \
    "
