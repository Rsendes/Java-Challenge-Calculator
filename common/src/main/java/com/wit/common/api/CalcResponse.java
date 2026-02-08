package com.wit.common.api;

import java.util.UUID;

public record CalcResponse(
        UUID requestId,
        boolean success,
        String result,
        String error
) {
}
