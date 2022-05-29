package com.inorista.situationpuzzle.domain;

/**
 * Selector class for User domain.
 */
public class UserSelector {

  private String userId;

  /**
   * Used when find user by the ID.
   *
   * @param id user ID.
   * @return selector object
   */
  public static UserSelector byUserId(String id) {
    UserSelector selector = new UserSelector();
    selector.userId = id;
    return selector;
  }
}
