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

package br.com.caelum.vraptor.core;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

public class URLParameterExtractorInterceptor implements Interceptor {

    public boolean accepts(ResourceMethod method) {
        return true;
    }

    public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
        Path path = method.getResource().getType().getAnnotation(Path.class);
        if (path != null) {
        }
        stack.next(method, resourceInstance);
    }

}
