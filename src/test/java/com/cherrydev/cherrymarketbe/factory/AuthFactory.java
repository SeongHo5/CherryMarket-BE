package com.cherrydev.cherrymarketbe.factory;


import com.cherrydev.cherrymarketbe.auth.dto.SignInRequestDto;

public class AuthFactory {

//    public static SignInRequestDto createSignInRequestDtoA() {
//        return new SignInRequestDto("boram17@example.org", "Password12#");
//    }
    public static SignInRequestDto createSignInRequestDtoA() {
        return new SignInRequestDto("boram17@example.org", "Password12#");
    }

    public static SignInRequestDto createSignInRequestDtoB() {
        return new SignInRequestDto("boram17@example.org", "Password1212");
    }

    public static SignInRequestDto createSignInRequestDtoC() {
        return new SignInRequestDto("hellonon1@example.org", "Password12#");
    }

    public static SignInRequestDto createSignInRequestDtoD() {
        return new SignInRequestDto("test1@marketcherry.com", "Password12#");
    }

    public static SignInRequestDto createSignInRequestDtoE() {
        return new SignInRequestDto("test2@marketcherry.com", "Password12#");
    }

}
