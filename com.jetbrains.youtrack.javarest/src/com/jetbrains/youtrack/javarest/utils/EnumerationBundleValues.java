/**
 * @author: amarch
 */

package com.jetbrains.youtrack.javarest.utils;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "enumeration")
public class EnumerationBundleValues extends BundleValues {

  @XmlElement(name = "value", type = EnumerationValue.class)
  private LinkedList<EnumerationValue> bundleValues;

  public LinkedList<EnumerationValue> getEnumerationValues() {
    return bundleValues;
  }

  @Override
  public LinkedList<String> getValues() {
    LinkedList<String> values = new LinkedList<String>();
    if (bundleValues != null) {
      for (BundleValue value : bundleValues) {
        values.add(value.getValue());
      }
    }
    return values;
  }
}
