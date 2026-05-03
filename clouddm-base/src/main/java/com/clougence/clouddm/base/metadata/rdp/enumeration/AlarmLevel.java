package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * @author bucketli 2020/2/12 19:08
 */
public enum AlarmLevel {

    /**
     * 1.function not work well,but work normal and correct ,just some metrics shows
     * it is go to dangerous 2.resource will not enough,pre-alarm send SMS/MAIL/IM
     * message to admin 3. console exception in controller. Need pay attention, but
     * may not alert
     */
    Major,

    /**
     * 1.data job delay 2.data job crash send SMS/MAIL/IM/PHONE to admin and data
     * job owner
     */
    Critical,

    /**
     * 1.console/sidecar crash 2.resource not enough send SMS/MAIL/IM/PHONE message
     * to admin
     */
    Blocker
}
