package com.ryaltech.utils.spring.encryption;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;

public class Args {
	public static class PatternConverter implements IStringConverter<Pattern> {

		@Override
		public Pattern convert(String value) {
			return Pattern.compile(value);
		}
		  
	}
	@Parameter(names = { "--includePatterns",
			"-ip" }, description = "Properties following this pattern will be encrypted. This overrides defaults. ", converter=Args.PatternConverter.class)
	List<Pattern> includePatterns;
	@Parameter(names = { "--excludePatterns",
			"-ep" }, description = "Properties following this pattern will not be encrypted. This overrides defaults.", converter=Args.PatternConverter.class)
	List<Pattern> excludePatterns;
	@Parameter(names = { "--addIncludePatterns",
			"-aip" }, description = "Properties following this pattern will be encrypted. These patterns are in addition to defaults.", converter=Args.PatternConverter.class)
	List<Pattern> additionalIncludePatterns;
	@Parameter(names = { "--addExcludePatterns",
			"-aep" }, description = "Properties following this pattern will not be encrypted. These patterns are in addition to defaults.", converter=Args.PatternConverter.class)
	List<Pattern> additionalExcludePatterns;

	@Parameter(names = { "--source",
			"-s" }, description = "Files to encrypt or directories to encrypt files within. At least one must be specified.", required = true)
	List<File> sources;

}
