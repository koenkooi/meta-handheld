DESCRIPTION = "Installkit for kexecboot-kernel"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"
DEPENDS = "${@base_conditional('MACHINE', 'collie', 'linux-yocto-tiny-kexecboot', 'zaurus-updater linux-yocto-tiny-kexecboot', d)}"
DEPENDS += "${@base_conditional('MACHINE', 'spitz', 'zaurus-legacy-tar', '', d)}"
PR = "r7"

INHIBIT_DEFAULT_DEPS = "1"

do_compile() {
        :
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES = ""

COMPATIBLE_MACHINE = "collie|poodle|c7x0|spitz|akita|tosa"

inherit deploy

addtask deploy before do_populate_sysroot after do_compile

do_deploy() {
        cd ${DEPLOY_DIR_IMAGE}
        rm -rf ${DEPLOY_DIR_IMAGE}/installkit-${MACHINE}/
        mkdir installkit-${MACHINE}/
        [ -f "${KERNEL_IMAGETYPE}-yocto-tiny-kexecboot-${MACHINE}.bin" ] && cp ${KERNEL_IMAGETYPE}-yocto-tiny-kexecboot-${MACHINE}.bin installkit-${MACHINE}/${KERNEL_IMAGETYPE}
        if [ ! "${MACHINE}" = "collie" ]; then
                cp updater.sh installkit-${MACHINE}/updater.sh
        fi
        if [ "${MACHINE}" = "spitz" ]; then
                cp ${DEPLOY_DIR_IMAGE}/gnu-tar installkit-${MACHINE}/gnu-tar
        fi
        tar czf ${DEPLOY_DIR_IMAGE}/installkit-${MACHINE}.tar.gz installkit-${MACHINE}/
        md5sum ${DEPLOY_DIR_IMAGE}/installkit-${MACHINE}.tar.gz > ${DEPLOY_DIR_IMAGE}/installkit-${MACHINE}.tar.gz.md5
        rm -rf ${DEPLOY_DIR_IMAGE}/installkit-${MACHINE}/
}
