package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * SASL is a freamwork for authentication and data security in Internet protocols.
 * <ul>
 *     <li>see <a href="https://en.wikipedia.org/wiki/Simple_Authentication_and_Security_Layer"> wiki </a> for more information</li>
 *     <li>see <a href="https://kafka.apache.org/documentation/#security"> kafka </a> for more about kafka</li>
 * </ul>
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/2/12 10:06
 */
public enum SaslMechanism {

    // use them to support the connect mode of sasl/scram for kafka
    PLAIN("PLAIN"),
    SCRAM_SHA_256("SCRAM-SHA-256"),
    SCRAM_SHA_512("SCRAM-SHA-512"),
    //    GSSAPI("GSSAPI"),
    //    OAUTHBEARER("OAUTHBEARER"),
    ;

    public final String mechanism;

    SaslMechanism(String mechanism){
        this.mechanism = mechanism;
    }

    public static SaslMechanism valueOfCode(String mechanism) {
        for (SaslMechanism value : SaslMechanism.values()) {
            if (value.name().equals(mechanism) || value.mechanism.equals(mechanism)) {
                return value;
            }
        }

        return null;
    }

}
