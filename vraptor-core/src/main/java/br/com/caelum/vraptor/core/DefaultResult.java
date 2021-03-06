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

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.View;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.ioc.RequestScoped;

@RequestScoped
public class DefaultResult implements Result {

    private final HttpServletRequest request;
    private final Container container;
    private boolean went = false;

    public DefaultResult(HttpServletRequest request, Container container) {
        this.request = request;
        this.container = container;
    }

    public <T extends View> T use(Class<T> view) {
        this.went = true;
        return container.instanceFor(view);
    }

    public void include(String key, Object value) {
        request.setAttribute(key, value);
    }

    public boolean used() {
        return went;
    }

}
