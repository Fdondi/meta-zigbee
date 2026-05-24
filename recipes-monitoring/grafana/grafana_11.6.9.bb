SUMMARY = "Open source analytics and monitoring platform"
HOMEPAGE = "https://grafana.com"
LICENSE = "AGPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb1e647870add0502f8f010b19de6bdb"

# Use the release tarball which includes pre-built frontend assets,
# avoiding a full Node.js/Webpack build during the Yocto build.
SRC_URI = "https://github.com/grafana/grafana/archive/refs/tags/v${PV}.tar.gz;downloadfilename=grafana-${PV}.tar.gz \
           file://grafana.service \
           file://grafana.ini \
           "
SRC_URI[sha256sum] = "ae72149e25c44aa0d0c8f80b9790e70793711f8ad4ad769d2ebaa069c0e63e50"

S = "${WORKDIR}/grafana-${PV}"

inherit go-module systemd

GO_IMPORT = "github.com/grafana/grafana"

SYSTEMD_SERVICE:${PN} = "grafana.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

GRAFANA_DATA_DIR ?= "/var/lib/grafana"
GRAFANA_LOG_DIR  ?= "/var/log/grafana"

do_compile() {
    cd ${S}
    export GOARCH="${TARGET_GOARCH}"
    export GOOS="linux"
    export CGO_ENABLED="0"
    go build -o ${B}/grafana-server ./pkg/cmd/grafana-server
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/grafana-server ${D}${bindir}/grafana-server

    install -d ${D}${datadir}/grafana
    cp -r ${S}/public ${D}${datadir}/grafana/

    install -d ${D}${sysconfdir}/grafana
    install -m 0644 ${WORKDIR}/grafana.ini ${D}${sysconfdir}/grafana/grafana.ini

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/grafana.service ${D}${systemd_unitdir}/system/

    install -d ${D}${GRAFANA_DATA_DIR}
    install -d ${D}${GRAFANA_LOG_DIR}
}

FILES:${PN} = " \
    ${bindir}/grafana-server \
    ${datadir}/grafana \
    ${sysconfdir}/grafana \
    ${systemd_unitdir}/system/grafana.service \
    ${GRAFANA_DATA_DIR} \
    ${GRAFANA_LOG_DIR} \
    "
