package com.wit.common.api;

import java.util.UUID;

public record CalcRequest(
        UUID requestId,
        Operation operation,
        String a,
        String b
) {
}
