package com.somuncu.SecurityWithJWT.webtoken;

//login yapmaya çalışan userın requestıdir . Adını en başta FormLogin yaptım diye değişmedim ancak login requestidir
public record FormLogin (String username , String password) {
}
