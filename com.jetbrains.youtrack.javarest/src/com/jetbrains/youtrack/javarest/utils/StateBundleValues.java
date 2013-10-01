/**
@author: amarch
*/

package com.jetbrains.youtrack.javarest.utils;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stateBundle")
public class StateBundleValues {
	
	private LinkedList<String> values = new LinkedList<String>();
	
	@XmlElement(name = "state", type = String.class)
	public LinkedList<String> getValues() {
		return values;
	}

}