FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://embedded.conf"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system/influxdb.service.d
    install -m 0644 ${WORKDIR}/embedded.conf \
        ${D}${systemd_unitdir}/system/influxdb.service.d/embedded.conf
}

FILES:${PN} += "${systemd_unitdir}/system/influxdb.service.d"
