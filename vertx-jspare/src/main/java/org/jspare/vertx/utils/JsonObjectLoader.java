package org.jspare.vertx.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jspare.core.annotation.Resource;

import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Resource
public class JsonObjectLoader {

	private static final String CONVERTER_PATTERN = "%sConverter";

	@Setter
	@Getter
	private Charset defaultCharset = StandardCharsets.UTF_8;

	@Setter
	@Getter
	private String defaultRootPath = "vertx";

	@SneakyThrows
	public <T> T loadOptions(String filename, Class<T> optionClazz) {

		return loadOptions(defaultRootPath, filename, optionClazz);
	}

	@SneakyThrows
	public <T> T loadOptions(String defaultRootPath, String filename, Class<T> optionClazz) {

		String optionClassName = String.format(CONVERTER_PATTERN, optionClazz.getName());
		Method method = Class.forName(optionClassName).getDeclaredMethod("fromJson", JsonObject.class, optionClazz);
		T instance = optionClazz.newInstance();
		method.invoke(null, loadOptions(defaultRootPath, filename), instance);
		return instance;
	}

	public JsonObject loadOptions(String filename) {

		return loadOptions(defaultRootPath, filename);
	}

	@SneakyThrows(IOException.class)
	public JsonObject loadOptions(String defaultRootPath, String filename) {
		
		String json = null;
		Path path = Paths.get(defaultRootPath, filename);
		File file = path.toFile();
		if(file.exists()){
			
			json = FileUtils.readFileToString(path.toFile(), defaultCharset);
		}else{
			
			json = IOUtils.toString(this.getClass().getResourceAsStream(String.format("/%s", path.toString())),  getDefaultCharset());
		}
		
		return new JsonObject(json);
	}
}