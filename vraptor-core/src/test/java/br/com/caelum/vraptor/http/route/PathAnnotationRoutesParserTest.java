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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Head;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.interceptor.VRaptorMatchers;
import br.com.caelum.vraptor.proxy.DefaultProxifier;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.resource.ResourceClass;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.test.VRaptorMockery;

public class PathAnnotationRoutesParserTest {

    private VRaptorMockery mockery;
    private ResourceClass resource;
    private DefaultRouter router;
    private MutableRequest request;
    private Proxifier proxifier;

    @Before
    public void setup() {
        this.mockery = new VRaptorMockery();
        this.resource = mockery.resource(Clients.class);
        this.request = mockery.mock(MutableRequest.class);
        this.proxifier = new DefaultProxifier();
        this.router = new DefaultRouter(new NoRoutesConfiguration(), new PathAnnotationRoutesParser(proxifier, new NoTypeFinder()),
        		proxifier, null, new NoTypeFinder());
        router.register(resource);
    }

    @Test
    public void findsTheCorrectAnnotatedMethodIfThereIsNoWebMethodAnnotationPresent() throws SecurityException,
            NoSuchMethodException {
        ResourceMethod method = router.parse("/clients", HttpMethod.POST, request);
        assertThat(method.getMethod(), is(equalTo(Clients.class.getMethod("list"))));
        mockery.assertIsSatisfied();
    }


    @Test
    public void suportsTheDefaultNameForANonAnnotatedMethod() throws SecurityException,
            NoSuchMethodException {
        ResourceMethod method = router.parse("/clients", HttpMethod.POST, request);
        assertThat(method.getMethod(), is(equalTo(Clients.class.getMethod("list"))));
        mockery.assertIsSatisfied();
    }

    @Test
    public void ignoresTheControllerSuffixForANonAnnotatedMethod() throws SecurityException,
            NoSuchMethodException {
        ResourceMethod method = router.parse("/clients", HttpMethod.POST, request);
        assertThat(method.getMethod(), is(equalTo(Clients.class.getMethod("list"))));
        mockery.assertIsSatisfied();
    }
    @Test
    public void addsASlashWhenUserForgotIt() throws SecurityException,  NoSuchMethodException {
    	ResourceMethod method = router.parse("/noSlash", HttpMethod.POST, request);
    	assertThat(method.getMethod(), is(equalTo(Clients.class.getMethod("noSlash"))));
    	mockery.assertIsSatisfied();
    }

    @Test
    public void matchesWhenUsingAWildcard() throws SecurityException, NoSuchMethodException {
        ResourceMethod method = router.parse("/move/second/child", HttpMethod.POST, request);
        assertThat(method, is(VRaptorMatchers.resourceMethod(Clients.class.getMethod("move"))));
        mockery.assertIsSatisfied();
    }

    @Test
    public void returnsNullIfMethodNotFound() {
        ResourceMethod method = router.parse("/projects", HttpMethod.POST, request);
        assertThat(method, is(nullValue()));
        mockery.assertIsSatisfied();
    }

    @Test
    public void returnsNullIfMethodIsNotPublic() {
        ResourceMethod method = router.parse("/protectMe", HttpMethod.POST, request);
        assertThat(method, is(nullValue()));
        mockery.assertIsSatisfied();
    }

    @Test
    public void returnsNullIfMethodIsStatic() {
        ResourceMethod method = router.parse("/staticMe", HttpMethod.POST, request);
        assertThat(method, is(nullValue()));
        mockery.assertIsSatisfied();
    }

    @br.com.caelum.vraptor.Resource
    public static class Clients {
        @Path("/move/*/child")
        public void move() {
        }

        @Path("noSlash")
        public void noSlash() {
        }


        @Path("/clients")
        public void list() {
        }

        @Path("/clients/remove")
        @Delete
        public void remove() {
        }

        @Path("/clients/head")
        @Head
        public void head() {
        }

        public void add() {
        }

        @Path("/protectMe")
        protected void protectMe() {
        }

        @Path("/staticMe")
        public static void staticMe() {
        }

        public void toInherit() {
        }
    }

    @Test
    public void shouldFindNonAnnotatedNonStaticPublicMethodWithComponentNameInVariableCamelCaseConventionAsURI()
            throws Exception {
        ResourceMethod method = router.parse("/clients/add", HttpMethod.POST, request);
        assertThat(method, is(VRaptorMatchers.resourceMethod(Clients.class.getMethod("add"))));
        mockery.assertIsSatisfied();
    }

    @Test
    public void shouldIgnoreAResourceWithTheWrongWebMethod() throws SecurityException {
        ResourceMethod method = router.parse("/clients/remove", HttpMethod.POST, request);
        assertThat(method, is(Matchers.nullValue()));
        mockery.assertIsSatisfied();
    }

    @Test
    public void shouldAcceptAResultWithASpecificWebMethod() throws SecurityException, NoSuchMethodException {
        ResourceMethod method = router.parse("/clients/head", HttpMethod.HEAD, request);
        assertThat(method, is(VRaptorMatchers.resourceMethod(Clients.class.getMethod("head"))));
        mockery.assertIsSatisfied();
    }

    static class NiceClients extends Clients {
    }

    @Test
    public void findsInheritedMethodsWithDefaultNames() throws SecurityException, NoSuchMethodException {
        ResourceClass childResource = mockery.resource(NiceClients.class);
        router.register(childResource);
        ResourceMethod method = router.parse("/niceClients/toInherit", HttpMethod.POST, request);
        assertThat(method, is(VRaptorMatchers.resourceMethod(Clients.class.getMethod("toInherit"))));
        mockery.assertIsSatisfied();
    }

}
