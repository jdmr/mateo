/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.colportor.test;

import javax.servlet.RequestDispatcher;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockRequestDispatcher;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 *
 * @author jdmr
 */
public class GenericWebXmlContextLoader extends AbstractContextLoader {

    private final MockServletContext servletContext;

    public GenericWebXmlContextLoader() {
        this("src/main/resources", false);
    }
    
    public GenericWebXmlContextLoader(String warRootDir, boolean isClasspathRelative) {
        ResourceLoader resourceLoader = isClasspathRelative ? new DefaultResourceLoader() : new FileSystemResourceLoader();
        this.servletContext = initServletContext(warRootDir, resourceLoader);
    }

    private MockServletContext initServletContext(String warRootDir, ResourceLoader resourceLoader) {
        return new MockServletContext(warRootDir, resourceLoader) {
            // Required for DefaultServletHttpRequestHandler...

            @Override
            public RequestDispatcher getNamedDispatcher(String path) {
                return (path.equals("default")) ? new MockRequestDispatcher(path) : super.getNamedDispatcher(path);
            }
        };
    }

    @Override
    public ApplicationContext loadContext(MergedContextConfiguration mergedConfig) throws Exception {
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        context.getEnvironment().setActiveProfiles(mergedConfig.getActiveProfiles());
        prepareContext(context);
        new XmlBeanDefinitionReader(context).loadBeanDefinitions(mergedConfig.getLocations());
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
        context.refresh();
        context.registerShutdownHook();
        return context;
    }

    @Override
    public ApplicationContext loadContext(String... locations) throws Exception {
        GenericWebApplicationContext context = new GenericWebApplicationContext();
        prepareContext(context);
        new XmlBeanDefinitionReader(context).loadBeanDefinitions(locations);
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
        context.refresh();
        context.registerShutdownHook();
        return context;
    }

    protected void prepareContext(GenericWebApplicationContext context) {
        this.servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
        context.setServletContext(this.servletContext);
    }

    @Override
    protected String getResourceSuffix() {
        return "-context.xml";
    }
}