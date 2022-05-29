package com.inorista.situationpuzzle.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Data source config property class.
 */
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfigProperties {

  private String url;
  private String driverClassName;
  private String username;
  private String password;
  private int initialSize;
  private int maxIdle;
  private int minIdle;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getInitialSize() {
    return initialSize;
  }

  public void setInitialSize(int initialSize) {
    this.initialSize = initialSize;
  }

  public int getMaxIdle() {
    return maxIdle;
  }

  public void setMaxIdle(int maxIdle) {
    this.maxIdle = maxIdle;
  }

  public int getMinIdle() {
    return minIdle;
  }

  public void setMinIdle(int minIdle) {
    this.minIdle = minIdle;
  }
}
