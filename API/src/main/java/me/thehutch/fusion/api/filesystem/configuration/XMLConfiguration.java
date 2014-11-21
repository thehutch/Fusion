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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author thehutch
 */
public class XMLConfiguration implements Configuration {
	private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
	private final String mName;
	private final Element mElement;
	private List<Configuration> mChildren;

	private XMLConfiguration(Element xmlElement) {
		mElement = xmlElement;
		mName = xmlElement.getTagName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return mName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAttribute(String attribute) {
		return mElement.hasAttribute(attribute);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAttributes() {
		final NamedNodeMap map = mElement.getAttributes();
		final int mapLength = map.getLength();

		final List<String> names = new ArrayList<>(mapLength);
		for (int i = 0; i < mapLength; ++i) {
			names.add(map.item(i).getNodeName());
		}
		return names;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAttribute(String name, String def) {
		final String value = mElement.getAttribute(name);
		if ((value == null) || value.isEmpty()) {
			return def;
		}
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttribute(String name, String value) {
		mElement.setAttribute(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasElement(String name) {
		return !getElements(name).isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getElement(String name) {
		final int index = name.indexOf('.', 0);
		if (index >= 0) {
			final List<Configuration> elements = getElements(name.substring(0, index));
			final Configuration config = elements.get(0);
			return config.getElement(name.substring(index + 1));
		}
		final List<Configuration> elements = getElements(name);
		return elements.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Configuration> getElements() {
		List<Configuration> children = mChildren;
		if (children != null) {
			return children;
		}

		final NodeList list = mElement.getChildNodes();
		final int listLength = list.getLength();

		children = new ArrayList<>(listLength);

		for (int i = 0; i < listLength; ++i) {
			final Node node = list.item(i);
			if (node instanceof Element) {
				children.add(new XMLConfiguration((Element) node));
			}
		}

		mChildren = children;
		return children;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Configuration> getElements(String name) {
		final Collection<Configuration> children = getElements();
		final List<Configuration> result = new ArrayList<>(children.size());

		children.stream()
			.filter(child -> child.getName().equals(name))
			.forEach(result::add);

		return result;
	}

	/**
	 * Load an XMLConfiguration for the specified {@link Path}.
	 * <p>
	 * @param file The {@link Path} to the file
	 * <p>
	 * @return The root configuration element of the parsed document
	 * <p>
	 * @throws InvalidConfigurationException Indicates an error in the configuration
	 */
	public static XMLConfiguration load(Path file) throws InvalidConfigurationException {
		try (InputStream stream = Files.newInputStream(file, StandardOpenOption.READ)) {
			return load(stream);
		} catch (IOException ex) {
			throw new InvalidConfigurationException(ex);
		}
	}

	/**
	 * Load an XMLConfiguration for the specified {@link InputStream}.
	 * <p>
	 * @param stream The {@link InputStream} of the file
	 * <p>
	 * @return The root configuration element of the parsed document
	 * <p>
	 * @throws InvalidConfigurationException Indicates an error in the configuration
	 */
	public static XMLConfiguration load(InputStream stream) throws InvalidConfigurationException {
		final Document doc;
		try {
			final DocumentBuilder builder = FACTORY.newDocumentBuilder();
			doc = builder.parse(stream);
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			throw new InvalidConfigurationException(ex);
		}
		return new XMLConfiguration(doc.getDocumentElement());
	}
}
