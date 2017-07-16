package com.xiaopo.flying.poiphoto.datatype;

/**
 * Album bean
 * @author wupanjie
 */
public class Album {
  private String name;
  private String id;
  private String coverPath;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCoverPath() {
    return coverPath;
  }

  public void setCoverPath(String coverPath) {
    this.coverPath = coverPath;
  }
}
