package com.inorista.situationpuzzle.domain;

public class UserSelector {
    private String userId;
    public static UserSelector byUserId(String id) {
        UserSelector selector = new UserSelector();
        selector.userId = id;
        return selector;
    }
}
