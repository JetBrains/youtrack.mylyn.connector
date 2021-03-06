/**
 * @author: amarch
 */

package com.jetbrains.youtrack.javarest.utils;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IntelliSense")
public class IntellisenseValues {

  public static class SuggestIntellisenseValues {

    private LinkedList<IntellisenseItem> values = new LinkedList<IntellisenseItem>();

    @XmlElement(name = "item")
    public LinkedList<IntellisenseItem> getValues() {
      return values;
    }

  }

  @XmlElement(name = "suggest")
  public SuggestIntellisenseValues suggest;

  public static class RecentIntellisenseValues {

    private LinkedList<IntellisenseItem> values = new LinkedList<IntellisenseItem>();

    @XmlElement(name = "item")
    public LinkedList<IntellisenseItem> getValues() {
      return values;
    }

  }

  @XmlElement(name = "recent")
  public RecentIntellisenseValues recent;

  public String[] getFullOptions() {
    LinkedList<String> values = new LinkedList<String>();
    for (IntellisenseItem item : suggest.getValues()) {
      if (item.getOption() != null) {
        values.add(item.getFullOption());
      }
    }
    for (IntellisenseItem item : recent.getValues()) {
      if (item.getOption() != null) {
        values.add(item.getFullOption());
      }
    }
    return values.toArray(new String[values.size()]);
  }

  public String[] getOptions() {
    LinkedList<String> values = new LinkedList<String>();
    for (IntellisenseItem item : suggest.getValues()) {
      if (item.getOption() != null) {
        values.add(item.getOption());
      }
    }
    for (IntellisenseItem item : recent.getValues()) {
      if (item.getOption() != null) {
        values.add(item.getOption());
      }
    }
    return values.toArray(new String[values.size()]);
  }

  public LinkedList<IntellisenseItem> getIntellisenseItems() {
    LinkedList<IntellisenseItem> merge;
    if (suggest != null && suggest.getValues() != null) {
      merge = new LinkedList<IntellisenseItem>(suggest.getValues());
    } else {
      merge = new LinkedList<IntellisenseItem>();
    }
    if (recent != null && recent.getValues() != null) {
      for (IntellisenseItem item : recent.getValues()) {
        merge.add(item);
      }
    }
    return merge;
  }

  public IntellisenseValues getIntellisenseValues() {
    return this;
  }
}
