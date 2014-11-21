/*
 * This file is part of API, licensed under the Apache 2.0 License.
 *
 * Copyright (c) 2014 thehutch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.thehutch.fusion.api.filesystem.configuration;

import java.util.List;

/**
 * @author thehutch
 */
public interface Configuration {
	/**
	 * Retrieve the name of this element.
	 * <p>
	 * @return The name of this element
	 */
	public String getName();

	/**
	 * Check if the configuration has the given attribute.
	 * <p>
	 * @param attribute The name of the attribute
	 * <p>
	 * @return True if the attribute is present, false otherwise
	 */
	public boolean hasAttribute(String attribute);

	/**
	 * A collection of all the attributes present on this configuration element.
	 * <p>
	 * @return A collection containg the names of the attributes
	 */
	public List<String> getAttributes();

	/**
	 * Get the attribute value with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * <p>
	 * @return The value of the attribute, if not found then an empty string
	 */
	default public String getAttribute(String name) {
		return getAttribute(name, "");
	}

	/**
	 * Get the attribute value with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * @param def  The default value to return
	 * <p>
	 * @return The value of the attribute, if not found then the default value
	 */
	public String getAttribute(String name, String def);

	/**
	 * Returns a byte representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * <p>
	 * @return The byte value of the attribute, if not found then 0
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to a byte
	 */
	default public byte getByteAttribute(String name) throws InvalidConfigurationException {
		return getByteAttribute(name, (byte) 0);
	}

	/**
	 * Returns a byte representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * @param def  The default value to return
	 * <p>
	 * @return The byte value of the attribute, if not found then the default value
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to a byte
	 */
	default public byte getByteAttribute(String name, byte def) throws InvalidConfigurationException {
		final String attrib = getAttribute(name, Byte.toString(def));
		try {
			return Byte.parseByte(attrib);
		} catch (NumberFormatException ex) {
			throw new InvalidConfigurationException("Attribute read: '" + attrib + "' is not a byte", ex);
		}
	}

	/**
	 * Returns a short representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * <p>
	 * @return The short value of the attribute, if not found then 0
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to a short
	 */
	default public short getShortAttribute(String name) throws InvalidConfigurationException {
		return getShortAttribute(name, (short) 0);
	}

	/**
	 * Returns a short representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * @param def  The default value to return
	 * <p>
	 * @return The short value of the attribute, if not found then the default value
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to a short
	 */
	default public short getShortAttribute(String name, short def) throws InvalidConfigurationException {
		final String attrib = getAttribute(name, Short.toString(def));
		try {
			return Short.parseShort(attrib);
		} catch (NumberFormatException ex) {
			throw new InvalidConfigurationException("Attribute read: '" + attrib + "' is not a short", ex);
		}
	}

	/**
	 * Returns an integer representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * <p>
	 * @return The integer value of the attribute, if not found then 0
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to an integer
	 */
	default public int getIntAttribute(String name) throws InvalidConfigurationException {
		return getIntAttribute(name, 0);
	}

	/**
	 * Returns an integer representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * @param def  The default value to return
	 * <p>
	 * @return The integer value of the attribute, if not found then the default value
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to an integer
	 */
	default public int getIntAttribute(String name, int def) throws InvalidConfigurationException {
		final String attrib = getAttribute(name, Integer.toString(def));
		try {
			return Integer.parseInt(attrib);
		} catch (NumberFormatException ex) {
			throw new InvalidConfigurationException("Attribute read: '" + attrib + "' is not an integer", ex);
		}
	}

	/**
	 * Returns a long representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * <p>
	 * @return The long value of the attribute, if not found then 0
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to a long
	 */
	default public long getLongAttribute(String name) throws InvalidConfigurationException {
		return getLongAttribute(name, 0L);
	}

	/**
	 * Returns a long representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * @param def  The default value to return
	 * <p>
	 * @return The long value of the attribute, if not found then the default value
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to an long
	 */
	default public long getLongAttribute(String name, long def) throws InvalidConfigurationException {
		final String attrib = getAttribute(name, Long.toString(def));
		try {
			return Long.parseLong(attrib);
		} catch (NumberFormatException ex) {
			throw new InvalidConfigurationException("Attribute read: '" + attrib + "' is not a long", ex);
		}
	}

	/**
	 * Returns a float representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * <p>
	 * @return The float value of the attribute, if not found then 0
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to a float
	 */
	default public float getFloatAttribute(String name) throws InvalidConfigurationException {
		return getFloatAttribute(name, +0.0f);
	}

	/**
	 * Returns a float representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * @param def  The default value to return
	 * <p>
	 * @return The float value of the attribute, if not found then the default value
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to an float
	 */
	default public float getFloatAttribute(String name, float def) throws InvalidConfigurationException {
		final String attrib = getAttribute(name, Float.toString(def));
		try {
			return Float.parseFloat(attrib);
		} catch (NumberFormatException ex) {
			throw new InvalidConfigurationException("Attribute read: '" + attrib + "' is not a float", ex);
		}
	}

	/**
	 * Returns a double representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * <p>
	 * @return The double value of the attribute, if not found then 0
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to a double
	 */
	default public double getDoubleAttribute(String name) throws InvalidConfigurationException {
		return getDoubleAttribute(name, +0.0d);
	}

	/**
	 * Returns a double representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * @param def  The default value to return
	 * <p>
	 * @return The double value of the attribute, if not found then the default value
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to an double
	 */
	default public double getDoubleAttribute(String name, double def) throws InvalidConfigurationException {
		final String attrib = getAttribute(name, Double.toString(def));
		try {
			return Double.parseDouble(attrib);
		} catch (NumberFormatException ex) {
			throw new InvalidConfigurationException("Attribute read: '" + attrib + "' is not a double", ex);
		}
	}

	/**
	 * Returns a boolean representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * <p>
	 * @return The boolean value of the attribute, if not found then false
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to a boolean
	 */
	default public boolean getBooleanAttribute(String name) throws InvalidConfigurationException {
		return getBooleanAttribute(name, false);
	}

	/**
	 * Returns a boolean representation of the attribute with the given name.
	 * <p>
	 * @param name The name of the attribute
	 * @param def  The default value to return
	 * <p>
	 * @return The boolean value of the attribute, if not found then the default value
	 * <p>
	 * @throws InvalidConfigurationException If the value can not be converted to an boolean
	 */
	default public boolean getBooleanAttribute(String name, boolean def) throws InvalidConfigurationException {
		final String attrib = getAttribute(name, Boolean.toString(def));
		try {
			return Boolean.parseBoolean(attrib);
		} catch (NumberFormatException ex) {
			throw new InvalidConfigurationException("Attribute read: '" + attrib + "' is not a boolean", ex);
		}
	}

	/**
	 * Sets the attribute value with the given name.
	 * <p>
	 * @param name  The name of the attribute
	 * @param value The value to set
	 */
	public void setAttribute(String name, String value);

	/**
	 * Sets the attribute with the given name with a byte value.
	 * <p>
	 * @param name  The name of the attribute
	 * @param value The byte value to set
	 */
	default public void SetAttribute(String name, byte value) {
		setAttribute(name, Byte.toString(value));
	}

	/**
	 * Sets the attribute with the given name with a short value.
	 * <p>
	 * @param name  The name of the attribute
	 * @param value The short value to set
	 */
	default public void SetAttribute(String name, short value) {
		setAttribute(name, Short.toString(value));
	}

	/**
	 * Sets the attribute with the given name with an integer value.
	 * <p>
	 * @param name  The name of the attribute
	 * @param value The integer value to set
	 */
	default public void SetAttribute(String name, int value) {
		setAttribute(name, Integer.toString(value));
	}

	/**
	 * Sets the attribute with the given name with a long value.
	 * <p>
	 * @param name  The name of the attribute
	 * @param value The long value to set
	 */
	default public void SetAttribute(String name, long value) {
		setAttribute(name, Long.toString(value));
	}

	/**
	 * Sets the attribute with the given name with a float value.
	 * <p>
	 * @param name  The name of the attribute
	 * @param value The float value to set
	 */
	default public void SetAttribute(String name, float value) {
		setAttribute(name, Float.toString(value));
	}

	/**
	 * Sets the attribute with the given name with a double value.
	 * <p>
	 * @param name  The name of the attribute
	 * @param value The double value to set
	 */
	default public void SetAttribute(String name, double value) {
		setAttribute(name, Double.toString(value));
	}

	/**
	 * Sets the attribute with the given name with a boolean value.
	 * <p>
	 * @param name  The name of the attribute
	 * @param value The boolean value to set
	 */
	default public void SetAttribute(String name, boolean value) {
		setAttribute(name, Boolean.toString(value));
	}

	/**
	 * Checks if this configuration element has a child element with
	 * the given name.
	 * <p>
	 * @param name The name of the child element
	 * <p>
	 * @return True if there is a child element with the given name
	 */
	public boolean hasElement(String name);

	/**
	 * Returns a child configuration element with the specified name.
	 * <p>
	 * @param name The name of the child element
	 * <p>
	 * @return The specified child element, otherwise null if not found
	 */
	public Configuration getElement(String name);

	/**
	 * Returns a collection of all children elements.
	 * <p>
	 * @return A collection containing all child elements
	 */
	public List<Configuration> getElements();

	/**
	 * Returns a collection of all children elements with the given name tag.
	 * <p>
	 * @param name The name of the element
	 * <p>
	 * @return A collection of all elements with the specified name tag
	 */
	public List<Configuration> getElements(String name);
}
