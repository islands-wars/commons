package fr.islandswars.commons.secrets;

import fr.islandswars.commons.service.ServiceType;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * File <b>DockerSecretsLoader</b> located on fr.islandswars.commons.secrets
 * DockerSecretsLoader is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2021 Islands Wars.
 * <p>
 * commons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">GNU license</a>.
 * <p>
 *
 * @author Valentin Burgaud (Xharos), {@literal <xharos@islandswars.fr>}
 * Created the 21/02/2021 at 17:56
 * @since 0.1
 */
public class DockerSecretsLoader {

	/**
	 * Get a specific secret stored in the given mounted service file
	 *
	 * @param type a docker mounted secret file to read
	 * @param key  a specific key to retrieve
	 * @return a value associated to this key if founded (if specified)
	 * @see Properties#getProperty(String)
	 */
	public static String getValue(ServiceType type, String key) {
		return loadFile(type.getSecretFile()).get(key);
	}

	/**
	 * Retrieve all secrets for this service file if exist
	 *
	 * @param type the ServiceType to load from
	 * @return a key value based storage for this file (if specified)
	 */
	public static Map<String, String> load(ServiceType type) {
		return loadFile(type.getSecretFile());
	}

	/**
	 * Retrieve all secrets stored in this file if exist
	 *
	 * @param file a docker mounted secret file to read
	 * @return a key value based storage for this file (if specified)
	 */
	public static Map<String, String> loadFile(File file) {
		Map<String, String> secrets    = new HashMap<>();
		var                 properties = popProperties(file);
		properties.forEach((k, v) -> secrets.put(getValue(k), getValue(v)));
		return secrets;
	}

	/**
	 * Get a specific secret stored in the given mounted file
	 *
	 * @param file a docker mounted secret file to read
	 * @param key  a specific key to retrieve
	 * @return a value associated to this key if founded (if specified)
	 * @see Properties#getProperty(String)
	 */
	public static String loadSpecificKeyInFile(File file, String key) {
		return loadFile(file).get(key);
	}

	private static String getValue(Object object) {
		return object instanceof String ? (String) object : null;
	}

	private static Properties popProperties(File secretFile) {
		var properties = new Properties();
		try (var reader = new FileReader(secretFile)) { //Extends AutoClosable
			properties.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}
}
