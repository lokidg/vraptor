/***
 * 
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. Neither the name of the
 * copyright holders nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package br.com.caelum.vraptor.view;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.MethodLookupBuilder;

/**
 * The default implementation of LogicResult.<br>
 * Uses cglib to provide proxies for client side redirect (url creation).
 * 
 * @author Guilherme Silveira
 */
public class DefaultLogicResult implements LogicResult {

    private final Container container;
    private final MethodLookupBuilder builder;
    private final HttpServletResponse response;

    public DefaultLogicResult(Container container, MethodLookupBuilder builder, HttpServletResponse response) {
        this.container = container;
        this.builder = builder;
        this.response = response;
    }

    public <T> T redirectServerTo(Class<T> type) {
        return container.instanceFor(type);
    }

    @SuppressWarnings("unchecked")
    public <T> T redirectClientTo(final Class<T> type) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object instance, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                String url = builder.urlFor(type, method, args);
                response.sendRedirect(url);
                return null;
            }
        });
        return (T) enhancer.create();
    }

}