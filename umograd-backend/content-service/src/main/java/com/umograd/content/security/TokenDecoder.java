package com.umograd.content.security;

import java.util.List;

public interface TokenDecoder {

    Claims decode(String token);

    record Claims(String username, List<String> roles, Long childId, Long userId, String email) {}
}
