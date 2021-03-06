/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */

package br.com.caelum.vraptor.http.route;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternBasedType {

	private final List<String> parameters = new ArrayList<String>();
	private final Pattern pattern;
	private String originalPattern;
	private String finalPattern;

	public PatternBasedType(String pattern) {
		this.originalPattern = pattern;
		String finalUri = "";
		String patternUri = "";
		String paramName = "";
		// not using stringbuffer because this is only run in startup
		boolean ignore = false;
		for (int i = 0; i < pattern.length(); i++) {
			if (pattern.charAt(i) == '{') {
				ignore = true;
				patternUri += "(";
				continue;
			} else if (pattern.charAt(i) == '}') {
				ignore = false;
				finalUri += ".*";
				patternUri += ".*)";
				parameters.add(paramName);
				paramName = "";
				continue;
			} else if (!ignore) {
				patternUri += pattern.charAt(i);
				finalUri += pattern.charAt(i);
			} else {
				paramName += pattern.charAt(i);
			}
		}
		if(ignore) {
			throw new IllegalRouteException("Illegal route contains invalid pattern: " + originalPattern);
		}
		this.finalPattern = patternUri;
		this.pattern = Pattern.compile(patternUri);
	}

	public boolean matches(String name) {
		return pattern.matcher(name).matches();
	}

	public String apply(String key, String value) {
		return originalPattern.replace("{"+key+"}", value);
	}

	public String extract(String paramName, String from) {
		Matcher matcher = pattern.matcher(from);
		matcher.matches();
		return matcher.group(parameters.indexOf(paramName)+1);
	}

}
