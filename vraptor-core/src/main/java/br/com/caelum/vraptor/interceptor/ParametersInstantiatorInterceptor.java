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

package br.com.caelum.vraptor.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.http.ParametersProvider;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.RequestOutjectMap;

/**
 * An interceptor which instantiates parameters and provide them to the stack.
 *
 * @author Guilherme Silveira
 */
public class ParametersInstantiatorInterceptor implements Interceptor {

    private final ParametersProvider provider;
    private final MethodInfo parameters;

    private static final Logger logger = LoggerFactory.getLogger(ParametersInstantiatorInterceptor.class);
    private final Validator validator;
    private final Localization localization;
	private final HttpServletRequest request;
	private final List<Message> errors = new ArrayList<Message>();

    public ParametersInstantiatorInterceptor(ParametersProvider provider, MethodInfo parameters,
            Validator validator, Localization localization, HttpServletRequest request) {
        this.provider = provider;
        this.parameters = parameters;
        this.validator = validator;
        this.localization = localization;
		this.request = request;
    }

    public boolean accepts(ResourceMethod method) {
        return true;
    }

    public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {

        Object[] values = provider.getParametersFor(method, errors, localization.getBundle());

        if (!errors.isEmpty()) {
    		validator.addAll(errors);
			prepareOutjectMap();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Parameter values for " + method + " are " + Arrays.asList(values));
        }

        parameters.setParameters(values);
        stack.next(method, resourceInstance);
    }

	void prepareOutjectMap() {
		@SuppressWarnings("unchecked")
		Set<String> paramNames = request.getParameterMap().keySet();

		for (String paramName : paramNames) {
			paramName = extractBaseParamName(paramName);
			new RequestOutjectMap(paramName, request);
		}
	}

	private String extractBaseParamName(String paramName) {
		int indexOf = paramName.indexOf('.');
		paramName = paramName.substring(0, indexOf != -1 ? indexOf : paramName.length());

		indexOf = paramName.indexOf('[');
		paramName = paramName.substring(0, indexOf != -1 ? indexOf : paramName.length());
		return paramName;
	}

}
