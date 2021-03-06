/*
 * Copyright 2009-2012 Michael Tamm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.fightinglayoutbugs;

import com.googlecode.fightinglayoutbugs.helpers.ImageHelper;

import javax.annotation.Nonnull;
import java.util.Collection;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;

/**
 * <p>
 * Detects if there is text on the analyzed web page,
 * which is near or overlaps a vertical edge or
 * is truncated at a vertical edge.
 * </p><p>
 * Example:<br />
 * <img src="http://fighting-layout-bugs.googlecode.com/svn/wiki/TextNearOrOverlappingVerticalEdge_example_1.png" alt="example for text which overlaps a vertical edge" />
 * </p>
 */
public class DetectTextNearOrOverlappingVerticalEdge extends AbstractLayoutBugDetector {

    public Collection<LayoutBug> findLayoutBugsIn(@Nonnull WebPage webPage) {
        final boolean[][] text = webPage.getTextPixels();
        final int w = text.length;
        final int h = text[0].length;
        final boolean[][] textOutlines = ImageHelper.findOutlines(text);
        if (w > 0 && h > 0) {
            final boolean[][] verticalEdges = webPage.getVerticalEdges();
            assert verticalEdges.length == w;
            assert verticalEdges[0].length == h;
            final boolean[][] buggyPixels = new boolean[w][h];
            boolean foundBuggyPixel = false;
            for (int x = 0; x < w; ++x) {
                for (int y = 0; y < h; ++y) {
                    if ((text[x][y] || textOutlines[x][y]) && verticalEdges[x][y]) {
                        buggyPixels[x][y] = true;
                        foundBuggyPixel = true;
                    }
                }
            }
            if (foundBuggyPixel) {
                final LayoutBug layoutBug = createLayoutBug("Detected text near or overlapping vertical edge.", webPage, new SurroundBuggyPixels(buggyPixels));
                return singleton(layoutBug);
            } else {
                return emptyList();
            }
        } else {
            return emptyList();
        }
    }

}
