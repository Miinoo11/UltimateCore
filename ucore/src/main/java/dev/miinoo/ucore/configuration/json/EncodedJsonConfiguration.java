package dev.miinoo.ucore.configuration.json;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.*;
import java.nio.charset.Charset;

public class EncodedJsonConfiguration extends JsonConfiguration {

	private final Charset charset;

	public EncodedJsonConfiguration(String charset) {
		this.charset = Charset.forName(charset);
	}

	@Override
	public void save(File file) throws IOException {
		Validate.notNull(file, "File cannot be null");

		Files.createParentDirs(file);

		String data = saveToString();

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));

		try {
			writer.write(data);
		} finally {
			writer.close();
		}
	}

	@Override
	public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
		Validate.notNull(file, "File cannot be null");
		FileInputStream stream = new FileInputStream(file);
		load(new InputStreamReader(stream, charset));
	}
		
}