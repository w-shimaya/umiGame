package com.inorista.situationpuzzle.controller.form;

import java.io.Serializable;

/**
 * Filter form class for thyemeleaf.
 */
public class FilterForm implements Serializable {

  private boolean includeYes;
  private boolean includeNo;
  private boolean includeAwait;
  private boolean includeVague;

  public boolean isIncludeYes() {
    return includeYes;
  }

  public void setIncludeYes(boolean includeYes) {
    this.includeYes = includeYes;
  }

  public boolean isIncludeNo() {
    return includeNo;
  }

  public void setIncludeNo(boolean includeNo) {
    this.includeNo = includeNo;
  }

  public boolean isIncludeAwait() {
    return includeAwait;
  }

  public void setIncludeAwait(boolean includeAwait) {
    this.includeAwait = includeAwait;
  }

  public boolean isIncludeVague() {
    return includeVague;
  }

  public void setIncludeVague(boolean includeVague) {
    this.includeVague = includeVague;
  }
}
