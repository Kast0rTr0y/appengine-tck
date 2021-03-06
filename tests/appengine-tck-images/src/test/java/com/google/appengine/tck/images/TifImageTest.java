/*
 * Copyright 2013 Google Inc. All Rights Reserved.
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

package com.google.appengine.tck.images;

import java.io.IOException;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService.OutputEncoding;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.google.appengine.tck.env.Environment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests tiff file.
 *
 * @author hchen@google.com (Hannah Chen)
 */
@RunWith(Arquillian.class)
public class TifImageTest extends ImagesServiceTestBase {
    private static final String BEACH_TIF = "beach.tif";
    private static final OutputEncoding[] OUTPUT_ENCODE = {OutputEncoding.JPEG, OutputEncoding.PNG};

    @Test
    public void testResize() throws IOException {
        assumeEnvironment(Environment.APPSPOT, Environment.CAPEDWARF);
        int resizeWidth = 150;
        int resizeHeigh = 150;
        Transform transform = ImagesServiceFactory.makeResize(resizeWidth, resizeHeigh);
        assertTransformation(transform, "Resize");
    }

    @Test
    public void testRotate() throws IOException {
        assumeEnvironment(Environment.APPSPOT, Environment.CAPEDWARF);
        int rotageDegree = 90;
        Transform transform = ImagesServiceFactory.makeRotate(rotageDegree);
        assertTransformation(transform, "Rotate");
    }

    @Test
    public void testHorizontalFlip() throws IOException {
        assumeEnvironment(Environment.APPSPOT, Environment.CAPEDWARF);
        Transform transform = ImagesServiceFactory.makeHorizontalFlip();
        assertTransformation(transform, "HorizontalFlip");
    }

    @Test
    public void testVerticalFlip() throws IOException {
        assumeEnvironment(Environment.APPSPOT, Environment.CAPEDWARF);
        Transform transform = ImagesServiceFactory.makeVerticalFlip();
        assertTransformation(transform, "VerticalFlip");
    }

    @Test
    public void testCrop() throws IOException {
        assumeEnvironment(Environment.APPSPOT, Environment.CAPEDWARF);
        double cropLeftX = 0.0;
        double cropTopY = 0.0;
        double cropRightX = 0.5;
        double cropBottomY = 0.5;
        Transform transform = ImagesServiceFactory.makeCrop(
            cropLeftX,
            cropTopY,
            cropRightX,
            cropBottomY
        );
        assertTransformation(transform, "Crop");
    }

    @Test
    public void testFeelingLucky() throws IOException {
        assumeEnvironment(Environment.APPSPOT);
        Transform transform = ImagesServiceFactory.makeImFeelingLucky();
        assertTransformation(transform, "ImFeelingLucky");
    }

    private void assertTransformation(Transform transform, String transformType) throws IOException {
        String expectedImage = "beach" + transformType + ".";
        for (OutputEncoding outType : OUTPUT_ENCODE) {
            String expectImageFile = expectedImage + outType.toString().toLowerCase();
            Image expected = readImage(expectImageFile);
            Image image = readImage(BEACH_TIF);
            Image transImg = imagesService.applyTransform(transform, image, outType);

            assertImages(transform, expected, transImg);
        }
    }
}
