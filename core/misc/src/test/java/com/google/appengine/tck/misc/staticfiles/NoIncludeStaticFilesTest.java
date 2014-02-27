/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.appengine.tck.misc.staticfiles;

import java.net.URL;

import com.google.appengine.tck.base.TestContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:marko.luksa@gmail.com">Marko Luksa</a>
 */
@RunWith(Arquillian.class)
public class NoIncludeStaticFilesTest extends StaticFilesTestBase {

    @Deployment
    public static WebArchive getDeployment() {
        return getTckDeployment(new TestContext()
            .setAppEngineWebXmlFile("appengine-web-nostaticfiles.xml"))
            .addAsWebResource(new StringAsset("This is /foo.html"), "/foo.html")
            .addAsWebResource(new StringAsset("This is /foo/bar.html"), "/foo/bar.html")
            .addAsWebResource(new StringAsset("This is /excluded/bar.html"), "/excluded/bar.html")
            .addAsWebResource(new StringAsset("This is /foo/bar.excluded.html"), "/foo/bar.excluded.html");
    }

    @Test
    @RunAsClient
    public void testAllFilesIncludedByDefault(@ArquillianResource URL url) throws Exception {
        assertResponseEquals("This is /foo.html", url, "foo.html");
        assertResponseEquals("This is /foo/bar.html", url, "foo/bar.html");
    }

    @Test
    @RunAsClient
    public void testExcludedDir(@ArquillianResource URL url) throws Exception {
        assertPageNotFound(url, "excluded/bar.html");
    }

    @Test
    @RunAsClient
    public void testExcludedExtension(@ArquillianResource URL url) throws Exception {
        assertPageNotFound(url, "foo/bar.excluded.html");
    }


}
