package jpknox.starling.roundup.api.rest;

import jakarta.servlet.http.HttpServletRequest;
import jpknox.starling.roundup.dto.api.rest.roundup.request.RoundupRequest;
import jpknox.starling.roundup.dto.api.rest.roundup.response.RoundupResponse;
import jpknox.starling.roundup.log.LogUtil;
import jpknox.starling.roundup.service.RoundUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RoundupController {

    private final RoundUpService roundUpService;

    @Autowired
    public RoundupController(RoundUpService roundUpService) {
        this.roundUpService = roundUpService;
    }

    /**
     * The current implementation is not idempotent, so POST has been used. Otherwise, PUT is more appropriate.
     */
    @PostMapping("/round-up/trigger")
    public ResponseEntity<RoundupResponse> activateRoundup(final HttpServletRequest httpServletRequest,
                                                           @RequestBody final RoundupRequest roundupRequest) {
        final String token = httpServletRequest.getHeader("Authorization");
        return ResponseEntity.ok(roundUpService.roundUp(roundupRequest, token));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RoundupResponse> handleException(Exception e) {
        LogUtil.warn("Something went wrong!", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RoundupResponse(false,
                        true,
                        "Something went wrong. Please contact support and provide the approximate date/time of the error",
                        null,
                        null,
                        null,
                        null));
    }

}
