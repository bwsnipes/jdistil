package com.bws.jdistil.builder.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.bws.jdistil.builder.data.Attribute;
import com.bws.jdistil.builder.data.AttributeType;
import com.bws.jdistil.builder.data.Fragment;
import com.bws.jdistil.builder.data.Project;
import com.bws.jdistil.builder.generator.util.ResourceReader;
import com.bws.jdistil.builder.generator.util.TextConverter;

public class LookupInfoGenerator {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private static ResourceReader resourceReader = new ResourceReader();

	private static String insertCategoryTemplate;
	
	public LookupInfoGenerator() {
		super();
	}

	private void loadTemplates() throws GeneratorException {

		try {
			if (insertCategoryTemplate == null) {
				insertCategoryTemplate = resourceReader.readResource("/com/bws/jdistil/project/fragment/resources/lookup/insert-category.txt");
			}
		}
		catch (IOException ioException) {
	
			throw new GeneratorException("Error loading SQL templates: " + ioException.getMessage());
		}
	}
	
	public void process(Path sqlPath, Map<String, String> categoryConstants) throws GeneratorException {

		if (!categoryConstants.isEmpty()) {
			
			// Load templates
			loadTemplates();

			// Initialize SQL statements
			StringBuffer sqlStatements = new StringBuffer();

			for (Map.Entry<String, String> entry : categoryConstants.entrySet()) {

				// Get category constant ID and name
				String categoryName = entry.getKey();
				String categoryId = entry.getValue();
				
				// Replace template variables
				String insertCategory = insertCategoryTemplate.replaceAll("CATEGORY-ID", String.valueOf(categoryId));
				insertCategory = insertCategory.replaceAll("CATEGORY-NAME", categoryName);

				// Add to category SQL statements
				sqlStatements.append(insertCategory).append(LINE_SEPARATOR);
			}

			try {
				// Create app-category SQL file
				Path appCategoryFilePath = sqlPath.resolve("app-category.sql");
				Files.writeString(appCategoryFilePath, sqlStatements.toString(), StandardOpenOption.CREATE_NEW);
			}
			catch (IOException ioException) {
	
				throw new GeneratorException("Error creating lookkup info SQL files: " + ioException.getMessage());
			}
		}
	}

}
