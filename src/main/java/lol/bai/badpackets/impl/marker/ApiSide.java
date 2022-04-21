package lol.bai.badpackets.impl.marker;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ApiSide {

    /**
     * Anything that has this annotation means that it function only available on the logical client.
     * <p>
     * Marker only annotation, doesn't actually restrict access on runtime.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    public @interface ClientOnly {

    }

    /**
     * Anything that has this annotation means that it function only available on the logical server.
     * <p>
     * Marker only annotation, doesn't actually restrict access on runtime.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    public @interface ServerOnly {

    }

}
