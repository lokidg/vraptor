/***
 *
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
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
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package br.com.caelum.vraptor.http.ognl;

import java.util.List;
import java.util.Map;

import ognl.ListPropertyAccessor;
import ognl.OgnlException;

/**
 * This list accessor is responsible for setting null values up to the list
 * size.<br>
 * Compatibility issues might arrive (in previous vraptor versions, the object
 * was instantiated instead of null being set).
 * 
 * @author Guilherme Silveira
 * 
 */
public class ListAccessor extends ListPropertyAccessor {

    @SuppressWarnings("unchecked")
    public Object getProperty(Map map, Object target, Object object) throws OgnlException {
        try {
            return super.getProperty(map, target, object);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void setProperty(Map map, Object target, Object key, Object parent) throws OgnlException {
        List<?> list = (List<?>) target;
        int index = (Integer) key;
        for (int i = list.size(); i <= index; i++) {
            list.add(null);
        }
        super.setProperty(map, target, key, parent);
    }

}