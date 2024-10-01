package com.somuncu.SecurityWithJWT.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest {

    private int userId;
    private String refreshToken;

}
