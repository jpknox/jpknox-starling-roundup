package jpknox.starling.roundup.dto.api.rest;

/**
 * Opted to use an ENUM for currency because they're well understood. The downside is this will need updating whenever a
 * new currency is supported. The upside is we'll gain backend input validation - the justification of which is open for
 * debate. Leaving validation on the frontend (and just using a String to represent currency code) will distribute the
 * computation - marginally decreasing load on the backend.
 */
public enum Currency {

    GBP;

}
