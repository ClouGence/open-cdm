package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/2/12 14:34
 */
public enum KafkaSecurityProtocol {

    SASL_SSL,
    SASL_PLAINTEXT,
    PLAINTEXT;

    public static KafkaSecurityProtocol valueOfCode(String mechanism) {
        for (KafkaSecurityProtocol value : KafkaSecurityProtocol.values()) {
            if (value.name().equals(mechanism)) {
                return value;
            }
        }

        return null;
    }

}
